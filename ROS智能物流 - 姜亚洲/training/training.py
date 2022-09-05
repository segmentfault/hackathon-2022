import tensorflow as tf
from tensorflow import keras
import numpy as np
from PIL import Image
import glob

def data_generation():
    image_list = glob.glob('/home/jetson/ros2_ws/snapshots/*.png')
    size = len(image_list)
    image_list = np.array(image_list)
    image_list = np.array_split(image_list,size)
    for batch in image_list:
        batch_list = []
        lable_list = []
        for item in batch:
            img = Image.open(item)
            resized_img = img.resize((int(img.width/3),int(img.height/3)))
            batch_list.append(np.array(resized_img))
            lable = item.split('_')
            lable_list.append([float(lable[2]),float(lable[3])])# turn and throttle
        
        yield (np.asarray(batch_list).astype(np.float32),np.asarray(lable_list).astype(np.float32))


def data_generation_valid():
    image_list = glob.glob('/home/jetson/ros2_ws/validation/*.png')
    size = len(image_list)
    image_list = np.array(image_list)
    image_list = np.array_split(image_list,size)
    for batch in image_list:
        batch_list = []
        lable_list = []
        for item in batch:
            img = Image.open(item)
            resized_img = img.resize((int(img.width/3),int(img.height/3)))
            batch_list.append(np.array(resized_img))
            lable = item.split('_')
            lable_list.append([float(lable[2]),float(lable[3])])# turn and throttle
        
        yield (np.asarray(batch_list).astype(np.float32),np.asarray(lable_list).astype(np.float32))


model  = keras.Sequential([tf.keras.layers.Conv2D(20,(3,3),activation='relu'),tf.keras.layers.MaxPool2D((2,2)),
tf.keras.layers.Conv2D(20,(3,3),activation='relu'),
tf.keras.layers.MaxPool2D((2,2)),
tf.keras.layers.Conv2D(20,(3,3),activation='relu'),
tf.keras.layers.Flatten(),
tf.keras.layers.Dense(64,activation='relu'),
tf.keras.layers.Dense(2,activation='linear')])
model.compile(optimizer='adam',loss='mean_squared_error',metrics=['accuracy'])
model.fit(x=data_generation(),validation_data=data_generation_valid())

