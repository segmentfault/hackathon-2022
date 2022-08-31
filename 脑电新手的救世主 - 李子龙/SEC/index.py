# -*-coding = utf-8 -*-
# @Time : 2021/10/16 0016 22:19
# @File : index.py
# @software : PyCharm
from tkinter import *
from tkinter import messagebox
from tkinter import ttk
import Deal


class FirstFrame(Frame):
    """程序首界面，用于选择执行的系统"""
    def __init__(self, master=None):
        super().__init__(master)  # super()代表了父类的定义
        self.master = master
        self.pack(anchor='n')  # 创建frame时将其放置在master父模块上
        '''建造时创建组件'''
        # 初始化组件的属性
        self.label_tit = Label(self, text='脑电分析系统', width=24, height=1, fg='black', font=('黑体', 25))
        self.btn_stu = Button(self, text='单被试分析', width=18, height=1, bg='gray', font=('黑体', 15))
        self.btn_adm = Button(self, text='多被试分析', width=18, height=1, bg='gray', font=('黑体', 15))
        self.btnQuit = Button(self, text='退出', width=18, height=1, font=('黑体', 15), command=Exit)
        # 组件的放置位置
        self.label_tit.grid(row=0, column=2, columnspan=3, pady=30)
        self.btn_stu.grid(row=1, column=0, columnspan=3, pady=30, padx=50)
        self.btn_adm.grid(row=1, column=4, columnspan=3, pady=30, padx=50)
        self.btnQuit.grid(row=2, column=2, columnspan=3, pady=30)


def Windows(width, height, app='FirstFrame(master=root)'):
    x = root.winfo_screenwidth()
    y = root.winfo_screenheight()
    align_str = '%dx%d+%d+%d' % (width, height, (x - width) / 2, (y - height) / 2)
    root.geometry(align_str)

    root.title('脑电分析系统')
    eval(app)
    root.protocol('WM_DELETE_WINDOW', Exit)
    root.mainloop()


def Save():
    messagebox.showinfo(title='提示消息', message='欢迎下次使用！')


def Exit():
    # True or 'False'
    res = messagebox.askokcancel(title='提示消息', message='是否退出系统？')
    if res:
        Save()
        root.destroy()


if __name__ == "__main__":
    '''当在该界面运行时的操作'''
    root = Tk()
    Windows(800, 600)