#!/bin/bash

source /opt/ros/indigo/setup.bash
export JAVA_HOME=/usr/java/jdk1.8.0_131/
export ROS_MASTER_URI=http://localhost:11311
export ROS_HOSTNAME=localhost
python robotNavigat.py
