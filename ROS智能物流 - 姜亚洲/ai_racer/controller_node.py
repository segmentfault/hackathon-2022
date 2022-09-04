import rclpy
from rclpy.node import Node
import keyboard
from std_msgs.msg import String


class MinimalPublisher(Node):

    def __init__(self):
        super().__init__('minimal_publisher')
        self.publisher_ = self.create_publisher(String, 'topic', 10)
        timer_period = 0.2  # seconds
        self.timer = self.create_timer(timer_period, self.timer_callback)
        self.i = 0

    def timer_callback(self):
        msg = String()

        if keyboard.is_pressed('w'):
            self.get_logger().info('w')
            msg.data = 'w'
            self.publisher_.publish(msg)

        elif keyboard.is_pressed('s'):
            self.get_logger().info('s')
            msg.data = 's'
            self.publisher_.publish(msg)

        elif keyboard.is_pressed('space'):
            self.get_logger().info('brake')
            msg.data = ' '
            self.publisher_.publish(msg)
        else:
            pass


        
        
        if keyboard.is_pressed('a'):
            self.get_logger().info('a')
            msg.data = 'a'
            self.publisher_.publish(msg)
        
        elif keyboard.is_pressed('d'):
            self.get_logger().info('d')
            msg.data = 'd'
            self.publisher_.publish(msg)
        else:
            self.get_logger().info('going straight')
            msg.data = 'go_straight'
            self.publisher_.publish(msg)

        if keyboard.is_pressed('k'):
            self.get_logger().info('k')
            msg.data = 'k'
            self.publisher_.publish(msg)
        
       


def main(args=None):
    rclpy.init(args=args)

    minimal_publisher = MinimalPublisher()

    rclpy.spin(minimal_publisher)

    # Destroy the node explicitly
    # (optional - otherwise it will be done automatically
    # when the garbage collector destroys the node object)
    minimal_publisher.destroy_node()
    rclpy.shutdown()


if __name__ == '__main__':
    main()
