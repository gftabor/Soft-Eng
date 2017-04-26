#!/usr/bin/env python

import rospy, tf, numpy, math
import csv
from geometry_msgs.msg import Pose
from geometry_msgs.msg import PoseStamped
from tf.transformations import euler_from_quaternion


def navToPose(x,y):
    #use global x,y,and theta
    print 'saw pos'
    global xPosition
    global yPosition
    global theta
    global move_pub
    #get desired positions from goal from rviz click
    goalPose = PoseStamped()
    goalPose.pose.position.x = 1
    goalPose.pose.position.y = 0
    goalPose.pose.orientation.x = goalPose.pose.orientation.y = goalPose.pose.orientation.z = 0
    goalPose.pose.orientation.w = 1
    goalPose.header.frame_id="map"
   

    move_pub.publish(goalPose)
    distance =1
    while(math.fabs(distance)>0.1):
        rospy.sleep(1)
        move_pub.publish(goalPose)
        distance = math.sqrt((goalPose.pose.position.x - xPosition)**2 + (goalPose.pose.position.y - yPosition)**2)
        #print distance
    rospy.sleep(.15)


#timer callback, executes every .01s in rospy.Timer in main
def timerCallback(event):
   
    global pose
    global xPosition
    global yPosition
    global theta
    #obtain odometry data from frame 'odom' to frame 'base_footprint'
    odom_list.waitForTransform('map','base_footprint', rospy.Time(0), rospy.Duration(100.0))
    #store the information in position and orientation
    (position, orientation) = odom_list.lookupTransform('map','base_footprint', rospy.Time(0))
    pose.position.x=position[0]
    pose.position.y=position[1]
    #these are repetitive but it helps to have them stored globally (navToPose uses them)
    xPosition=position[0]
    yPosition=position[1]

    odomW=orientation
    q = [odomW[0], odomW[1], odomW[2], odomW[3]]
    roll, pitch, yaw = euler_from_quaternion(q) #get angles from the orientation odometry data

    pose.orientation.z = yaw
    theta = math.degrees(yaw)


# This is the program's main function
if __name__ == '__main__':
    rospy.init_node('faulknerHospital')
    print "faulknerHospital init"
    #global variables
    global pub
    global pose
    global odom_tf
    global odom_list
    global header_pub
    global move_pub
    pose = Pose()
    
    #Publishers and Subscribers
    move_pub = rospy.Publisher('/move_base_simple/goal',PoseStamped,queue_size=1)

    # Use this object to get the robot's Odometry 
    odom_list = tf.TransformListener()
    
    rospy.Timer(rospy.Duration(.01), timerCallback) #update odometry data captured in timerCallback every .01ms
    rospy.sleep(.25)

    
    with open('path.txt') as csvfile:
        reader = csv.reader(csvfile, delimiter=',', quotechar='|')
        for row in spamreader:
            print('-'.join(row))
            navToPose(row[0],row[1])
    
    while (not rospy.is_shutdown()):
     
        rospy.sleep(1)

    print "Lab 2 complete!"
