from PIL import Image
img = Image.open('/home/jetson/ros2_ws/snapshots/0d23ef1a-2ac7-11ed-acc6-e97e36b3a2f2_-0.0_1.0_.png')
img.show()
res = img.resize((int(img.width/10),int(img.height/10)))
print(type(res))
res.show()