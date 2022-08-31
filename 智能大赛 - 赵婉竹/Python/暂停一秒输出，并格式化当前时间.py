# -*- coding: utf-8 -*-
"""暂停一秒输出，并格式化当前时间
Created on Mon Aug  8 09:09:41 2022

@author: 86156
"""
import time
 
print (time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time())))
 
# 暂停一秒
time.sleep(1)
 
print (time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time())))
