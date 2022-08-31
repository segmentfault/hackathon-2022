# -*-coding = utf-8 -*-
# @Time : 2021/10/16 0016 20:20
# @File : Deal.py
# @software : PyCharm
# code: UTF-8
import numpy as np
import mne
from mne.preprocessing import ICA
from mne.time_frequency import tfr_morlet
import matplotlib.pyplot as plt


def read_data():
    data_path = "1刘东鑫.cnt"
    raw = mne.io.read_raw_cnt(data_path, preload=True)
    print(raw)
    print(raw.info)

    #raw.plot(duration=5, n_channels=32, clipping=None)
    #raw.plot_psd(average=True)
    #plt.show()
    return raw


def trap_filter(raw, freq):
    """陷波滤波"""
    raw = raw.notch_filter(freqs=(freq))
    raw.plot_psd(average=True)


def l_and_h_filter(raw, l_freq, h_freq):
    """高低通滤波"""
    raw = raw.filter(l_freq=l_freq, h_freq=h_freq)
    #raw.plot_psd(average=True)
    return raw


def ica_analysis(raw):
    ica = ICA(max_iter='auto')
    raw_for_ica = raw.copy().filter(l_freq=1, h_freq=None)
    print("进行独立成分分析,请等待")
    ica.fit(raw_for_ica)
    choose = input("[1]各成分的时序信号图[2]各成分地形图")
    if choose == "1":
        ica.plot_sources(raw_for_ica)
        plt.show()
    elif choose == "2":
        ica.plot_components()
        plt.show()
    return ica, raw_for_ica


def exclude(raw, ica, raw_for_ica):
    exc = int(input("请输入要剔除的成分序号"))
    ica.plot_overlay(raw_for_ica, exclude=[exc])
    # 设定要剔除的成分序号
    ica.exclude = [exc]
    # 应用到脑电数据上
    ica.apply(raw)
    raw.plot(duration=5, n_channels=32, clipping=None)
    plt.show()


def annotations2events(raw):
    events, event_id = mne.events_from_annotations(raw)
    print(events.shape, event_id)
    return events


if __name__ == "__main__":
    raw = read_data()
    while True:
        choose = input("请问是否要陷波滤波去除工频：[1]是 [2]否")
        if choose == "1":
            freq = int(input("请输入滤波Hz:"))
            filter(raw, freq)
        elif choose == "2":
            break
    raw = l_and_h_filter(raw, 0.1, 30)
    ica, raw_for_ica = ica_analysis(raw)
    exclude(raw, ica, raw_for_ica)
    events = annotations2events(raw)