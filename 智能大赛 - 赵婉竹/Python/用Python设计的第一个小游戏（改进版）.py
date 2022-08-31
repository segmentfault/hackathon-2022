# -*- coding: utf-8 -*-
"""用python设计第一个小游戏（改进版）
Created on Sun Jun 26 17:18:30 2022

@author: 86156
"""
import random
counts=3
answer=random.randint(1,10)
while counts>0:
 temp=input("我们来玩个游戏吧，不妨猜一下我现在心里想的是哪个数字：")
 guess=int(temp)
 if guess == answer:
    print("你是我肚子里的蛔虫吗喂喂喂")
    print("哼哼，不过猜中了也没奖励")
    break
 else:
    if guess<answer:
        print("小啦小啦")
    else:
        print("大啦")
 counts=counts-1
print("游戏结束，不玩啦！")
