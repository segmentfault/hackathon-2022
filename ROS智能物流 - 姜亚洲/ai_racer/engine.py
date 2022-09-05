
from multiprocessing.spawn import prepare
import rclpy
from rclpy.node import Node
from adafruit_servokit import ServoKit
from std_msgs.msg import String
from time import *
from sensor_msgs.msg import Joy
import subprocess
from jetcam.csi_camera import CSICamera
import traitlets
import uuid

from PIL import Image


kit = ServoKit(channels=16)
kit.servo[0].angle = 72



class MinimalSubscriber(Node):

    def __init__(self):
        super().__init__('minimal_subscriber')
        self.isCollecting = True
        self.prepareDataCollection()
        self.camera = CSICamera(width=328, height=246,capture_fps=60)
        self.camera.running=True
        self.camera.observe(self.cameraCallback,names='value')
        self.turn_value = 0.0
        self.throttle_value = 0.0

        self.subscription = self.create_subscription(
            Joy,
            'joy',
            self.joy_CallBack,
            10)

        self.subscription

    def cameraCallback(self,change):
        #self.get_logger().info(str(change['new']))
        if self.turn_value!=0.0 or self.throttle_value!=0.0:
            self.saveData(self.turn_value,self.throttle_value,change['new'])

    def saveData(self,steering,throttle,image_data):
        if self.isCollecting:
            file_path = 'snapshots/' + str(uuid.uuid1()) +'_'+str(round(steering,5))+'_'+str(round(throttle,5))+ '_.png'
            im = Image.fromarray(image_data)
            im.save(file_path)
            self.get_logger().info('saved data:'+file_path)

    def joy_CallBack(self,msg):
        self.get_logger().info('Button:'+str(msg.buttons))
        self.get_logger().info('Axes:'+str(msg.axes))
        self.turn_value = msg.axes[2]
        self.throttle_value = msg.axes[1]

        if msg.buttons[0]==1:
            self.prepareEsc()
        #turn
        turn = msg.axes[2]*-1.0
        y=turn/(1.0/40.0)
        y=72+y
        kit.servo[0].angle = int(y)
        #throttle
        the = msg.axes[1]
        t= the/(1.0/7.0)
        if the==0.0 or the==-0.0:
            kit.servo[1].angle = 90
        else:
            if the > 0:
                kit.servo[1].angle = 90+t
            else:
                kit.servo[1].angle = 90+t-8

    
    def prepareEsc(self):
        self.get_logger().info("电调自检验开始")
        for i in range(50):
            kit.servo[1].angle = 90
            sleep(0.1)
        self.get_logger().info("电调自检验完成")

    def prepareDataCollection(self):
        subprocess.call(['rm', '-r','-f', 'snapshots'])
        subprocess.call(['mkdir', '-p', 'snapshots'])
    


def main(args=None):
    rclpy.init(args=args)

    minimal_subscriber = MinimalSubscriber()

    rclpy.spin(minimal_subscriber)

    # Destroy the node explicitly
    # (optional - otherwise it will be done automatically
    # when the garbage collector destroys the node object)
    minimal_subscriber.destroy_node()
    rclpy.shutdown()




if __name__ == '__main__':
    main()
