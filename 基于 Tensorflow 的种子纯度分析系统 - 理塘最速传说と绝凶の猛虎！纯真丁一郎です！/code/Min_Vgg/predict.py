import os
import imutils
import json
import cv2
from PIL import Image
import numpy as np
from model import minvgg
label = {'01': 0, '02': 0, '03': 0, '216': 0, 'daohuaxiang02': 0,'ruomi':0}
im_height = 128
im_width = 128
num_classes = 6
# read class_indict
json_path = './class_indices.json'
assert os.path.exists(json_path), "file: '{}' dose not exist.".format(json_path)

json_file = open(json_path, "r")
class_indict = json.load(json_file)

# create model
model = minvgg(im_height=im_height, im_width=im_width, num_classes=num_classes)
weights_path = "./save_weights/myMinVGG_14.h5"
model.load_weights(weights_path)
def pred(img_path):
    # load image
    img = Image.open(img_path)
    # resize image to 128x128
    img = img.resize((im_width, im_height))
    # scaling pixel value to (0-1)
    img = np.array(img) / 255.

    # Add the image to a batch where it's the only member.
    img = (np.expand_dims(img, 0))
    result = np.squeeze(model.predict(img))
    predict_class = np.argmax(result)
    result=class_indict[str(predict_class)]
    return result
def prepare(path):
    check={}
    PATH = path + '.0'
    pp = path + '.txt'
    img = cv2.imread(path)
    path2 = path.replace(".jpg", "_1.jpg")
    path3=path.replace(".jpg", "_2.jpg")
    cv2.imwrite(path2, img)
    cv2.imwrite(path3, img)
    imgg = cv2.imread(path2)
    imgg2 = cv2.imread(path3)
    img_grey = cv2.cvtColor(img, cv2.COLOR_RGB2GRAY)
    if os.path.exists(PATH) is False:
        os.makedirs(PATH)
    canny_img = cv2.Canny(img_grey, 100, 100)
    cnts = cv2.findContours(canny_img.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    cnts = imutils.grab_contours(cnts)
    mask = np.ones(img.shape[:2], dtype="uint8") * 255
    cv2.drawContours(mask, cnts, -1, 0, -1)
    img[mask == 255] = 0
    i = 0
    for c in cnts:
        (x, y, w, h) = cv2.boundingRect(c)
        #找到符合条件的检测目标
        if (w * h > 1500):
            xx=0
            liebiao=[]
            liebiao.append(x)
            liebiao.append(y)
            liebiao.append(w)
            liebiao.append(h)
            if check.get(x) is not None:
                if abs(check[x][1]-y)<100:
                    if check[x][2]*check[x][3]<w*h:
                        del check[x]
                else:
                    if check[x][1] > 40 and check[x][0] > 40:
                        crop = imgg2[check[x][1] - 40:check[x][1] + check[x][3] + 50,
                               check[x][0] - 40:check[x][0] + check[x][2] + 50]
                    else:
                        crop = imgg2[check[x][1]:check[x][1] + check[x][3], check[x][0]:x + check[x][2]]
                    cv2.rectangle(imgg, (check[x][0], check[x][1]),
                                  (check[x][0] + check[x][2], check[x][1] + check[x][3]), (0, 255, 0), 2)
                    image_name = "{}.jpg".format(i)
                    image_url = os.path.join(PATH, image_name)
                    cv2.imwrite(image_url, crop)
                    ss = pred(image_url)
                    label[ss] += 1
                    font = cv2.FONT_HERSHEY_SIMPLEX
                    if ss == "daohuaxiang02":
                        imgg = cv2.putText(imgg, '{}'.format("DHX"),(check[x][0], check[x][1]), font, 1, (100, 255, 0), 2,
                                       cv2.LINE_AA)
                    else:
                        imgg = cv2.putText(imgg, '{}'.format(ss),(check[x][0], check[x][1]), font, 1, (100, 255, 0), 2,
                                       cv2.LINE_AA)
                    i = i + 1
                    del check[x]
            for j in range(-100,100):
                x1=x+j

                if check.get(x1) is not None:
                    if (abs(y-check[x1][1])+abs(x1-x))<100:
                        if w*h<check[x1][2]*check[x1][3]:
                            xx=1
                        else:
                            del check[x1]
                            xx=0
            if xx==1:
                continue
            check[x] = liebiao
    kk=0
    for key in check:
        x = check[key][0]
        y = check[key][1]
        w = check[key][2]
        h = check[key][3]
        if y > 40 and x > 40:
            crop = imgg2[y - 40:y + h + 50, x - 40:x + w + 50]
        else:
            crop = imgg2[y:y + h, x:x + w]
        cv2.rectangle(imgg, (x, y), (x + w, y + h), (0, 255, 0), 2)
        image_name = "{}.jpg".format(i)
        image_url = os.path.join(PATH, image_name)
        cv2.imwrite(image_url, crop)
        ss = pred(image_url)
        label[ss] += 1
        font = cv2.FONT_HERSHEY_SIMPLEX
        if ss=="daohuaxiang02":
            imgg = cv2.putText(imgg, '{}'.format("DHX"), (x, y), font, 1, (100, 255, 0), 2, cv2.LINE_AA)
        else:
            imgg = cv2.putText(imgg, '{}'.format(ss), (x, y), font, 1, (100, 255, 0), 2, cv2.LINE_AA)
        i=i+1
        kk=kk+1
    cv2.imwrite(path + ' ' + 'result' + '.jpg', imgg)
    os.remove(path2)
    os.remove(path3)
    mmax = label['01'] + label['02'] + label['03'] + label['216'] + label['daohuaxiang02']+label['ruomi']
    a = []
    b = []
    for i in label.keys():
        a.append(i)
    for j in label.values():
        b.append(j)
    c = len(b)
    if os.path.exists(pp) is True:
        os.remove(pp)
    for i in range(0, c):
        if b[i] > 0:
            pur = b[i] / mmax
            pur = round(pur, 3)
            file_handle = open(pp, mode='a+')
            file_handle.write(a[i] + '\t' + str(pur) + '\n')
            file_handle.close()
if __name__ == '__main__':
    path = input("请输入图片的绝对路径")
    prepare(path)
