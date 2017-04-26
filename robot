#!/bin/bash

source /opt/ros/indigo/setup.bash
export JAVA_HOME=/usr/java/jdk1.8.0_131/
export ROS_MASTER_URI=http://192.168.43.32:11311
export ROS_IP=192.168.43.52
python robotNavigat.py
