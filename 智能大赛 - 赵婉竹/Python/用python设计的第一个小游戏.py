"""用python设计第一个小游戏"""
temp=input("我们来玩个游戏吧，不妨猜一下我现在心里想的是哪个数字：")
guess=int(temp)
if guess==8:
    print("你是我肚子里的蛔虫吗喂喂喂")
    print("哼哼，不过猜中了也没奖励")
else:
    print("你没有猜中哦，我现在心里想的是8")
    print("游戏结束，不玩啦！")
    