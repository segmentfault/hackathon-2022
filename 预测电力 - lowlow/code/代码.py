#!/usr/bin/env python
# coding: utf-8

# In[1]:


import matplotlib.pyplot as plt
import numpy as np
from sklearn.preprocessing import LabelEncoder
import seaborn as sns 
sns.set_style('darkgrid')
import pandas as pd 
#-*-coding:utf-8-*- 
#文件名: ch.py 
from pylab import mpl
mpl.rcParams['font.sans-serif'] = ['FangSong'] # 指定默认字体 
mpl.rcParams['axes.unicode_minus'] = False # 解决保存图像是负号'-'显示为方块的问题 
from sklearn.metrics import mean_absolute_error, mean_squared_error
from colorama import Fore


# In[2]:


label = pd.read_csv('附件1-区域15分钟负荷数据.csv')
label = label.rename(columns={'数据时间':'日期1'})
label['日期'] = label['日期1'].apply(lambda x: x.split(' ')[0])
label.loc[:, '日期'] = pd.to_datetime(label.loc[:, '日期'], format='%Y/%m/%d', errors='coerce')


# In[3]:


label=label.drop(["日期"],axis=1)
train=label.copy()


# In[4]:


#变换数据格式把date变成datetime
train["日期1"]=pd.to_datetime(train["日期1"])
trains=train.set_index("日期1",drop=True)
train=trains.copy()


# In[5]:


data_tra= train.filter(['总有功功率（kw）'])
#Convert the dataframe to a numpy array
train= data_tra.to_numpy()
trains=train.astype(float)


# In[ ]:





# In[6]:


import pywt
# Get data:
ecg=trains
# 生成心电信号
index = []
data = []
for i in range(len(ecg)-1):
    X = float(i)
    Y = float(ecg[i])
    index.append(X)
    data.append(Y)

# Create wavelet object and define parameters
w = pywt.Wavelet('db4')  # 选用Daubechies8小波
maxlev = pywt.dwt_max_level(len(data), w.dec_len)
print("maximum level is " + str(maxlev))
threshold = 0.02  # Threshold for filtering
# Decompose into wavelet components, to the level selected:
coeffs = pywt.wavedec(data, 'db4', level=maxlev)  # 将信号进行小波分解

plt.figure()
for i in range(1, len(coeffs)):
    coeffs[i] = pywt.threshold(coeffs[i], threshold*max(coeffs[i]))  # 将噪声滤波

datarec2 = pywt.waverec(coeffs, 'db4')  # 将信号进行小波重构

mintime = 0
maxtime = mintime + len(data) + 1

plt.figure(figsize=(15,8))
plt.subplot(2, 1, 1)
plt.plot(index[mintime:maxtime], data[mintime:maxtime])
plt.xlabel('time (s)')
plt.ylabel('microvolts (uV)')
plt.title("Raw signal")
plt.subplot(2, 1, 2)
plt.plot(index[mintime:maxtime], datarec2[mintime:maxtime-1])
plt.xlabel('time (s)')
plt.ylabel('microvolts (uV)')
plt.title("De-noised signal using wavelet techniques")

plt.tight_layout()
plt.show()


# In[7]:


trains=datarec2
print(len(trains))


# In[8]:


trains


# In[9]:


from sklearn.preprocessing import MinMaxScaler
scaler = MinMaxScaler(feature_range=(0, 1))
train_tra,train_test=trains[0:int(len(train)*0.9)],trains[int(len(train)*0.9):]


# In[10]:


train_tra


# In[11]:


train_tras=scaler.fit_transform(train_tra.reshape(-1,1))
train_tests=scaler.transform(train_test.reshape(-1,1))


# In[12]:


train_tras


# In[13]:


look_back=52
# Split into train and test sets
train, test = train_tras,train_tests

def create_dataset(dataset,look_back=1):
    X, Y = [], []
    for i in range(look_back, len(dataset)):
        a = dataset[i-look_back:i, 0]
        X.append(a)
        Y.append(dataset[i, 0])
    return np.array(X), np.array(Y)

x_train, y_train = create_dataset(train,look_back)
x_test, y_test = create_dataset(test,look_back)

# reshape input to be [samples, time steps, features]
x_train = np.reshape(x_train, (x_train.shape[0], 1, x_train.shape[1]))
x_test = np.reshape(x_test, (x_test.shape[0], 1, x_test.shape[1]))


# In[20]:


y_train 


# In[173]:


from keras.models import Sequential
from keras.layers import Dense, LSTM

#Build the LSTM model
model = Sequential()
model.add(LSTM(128, return_sequences=True, input_shape=(x_train.shape[1], x_train.shape[2])))
model.add(LSTM(64, return_sequences=False))
model.add(Dense(25))
model.add(Dense(1))


# Compile the model
model.compile(optimizer='adam', loss="mean_squared_error")
history=model.fit(x_train, y_train, batch_size=16, epochs=14,validation_data=(x_test,y_test))
#Train the model


# In[181]:


import matplotlib.pyplot as plt


plt.plot(history.history['loss'])
plt.plot(history.history['val_loss'])
plt.title('model train vs validation loss')
plt.ylabel('loss')
plt.xlabel('epoch')
plt.legend(['train','validation'], loc='upper right')
plt.show()
plt.savefig('拟合曲线.png')


# In[178]:


test_predict=model.predict(x_test)
test_predict=scaler.inverse_transform(test_predict)

y_test=scaler.inverse_transform(test.reshape(-1,1))#反归一化
print(len(x_test))


# In[179]:


plt.figure(figsize=(20, 12))
#设一个新变量降维
Y=y_test[52:].flatten()
X=test_predict.flatten()
#取这个的前960个值

plt.figure(figsize=(16, 6))
dict_data = {
    'Predictions': X,
    '总有功功率（kw）': Y
}
data_pd = pd.DataFrame(dict_data)

plt.plot(data_pd[['总有功功率（kw）','Predictions']])
plt.legend()
plt.title('真实值与预测值')
plt.show()


# In[180]:


from sklearn import metrics


from sklearn.metrics import r2_score
print('R2-score:',r2_score(Y,X))
def mape(y_true, y_pred):
    return np.mean(np.abs((y_pred - y_true) / y_true))
 
y_true = np.array(data_pd['总有功功率（kw）'])
y_pred = np.array(data_pd['Predictions'])
 
print('MSE:',metrics.mean_squared_error(y_true, y_pred))
 
print('RMSE:',np.sqrt(metrics.mean_squared_error(y_true, y_pred)))
 
print('MAE:',metrics.mean_absolute_error(y_true, y_pred))
 
print('MAPE:',mape(y_true, y_pred))


# In[18]:


train


# In[19]:


#测试集训练集
from sklearn.preprocessing import MinMaxScaler
scalers= MinMaxScaler(feature_range=(0, 1))
train_tra2=trains[int(len(train)*0.1):]
train_tras2=scalers.fit_transform(train_tra2.reshape(-1,1))


# In[20]:


train_tra2.max()


# In[21]:


look_back=52
# Split into train and test sets
train = train_tras2

def create_dataset(dataset,look_back=1):
    X, Y = [], []
    for i in range(look_back, len(dataset)):
        a = dataset[i-look_back:i, 0]
        X.append(a)
        Y.append(dataset[i, 0])
    return np.array(X), np.array(Y)

x_train, y_train = create_dataset(train,look_back)

# reshape input to be [samples, time steps, features]
x_train = np.reshape(x_train, (x_train.shape[0], 1, x_train.shape[1]))


# In[65]:


from keras.models import Sequential
from keras.layers import Dense, LSTM

from keras.models import load_model
#Build the LSTM model
model3=load_model("model2.h5")

# Compile the model
history=model3.fit(x_train, y_train)

#Train the model


# In[182]:


model3.save("model.h5")


# In[183]:


result_date=pd.date_range(start="2018/1/15  00:00:00",end="2018/1/24  23:45:00",freq="15min")
dup_ts = pd.Series(np.arange(960), index=result_date)


# In[184]:


z=x_train[-1:]


# In[185]:


for i in np.arange(len(dup_ts)):
    predict_result=model3.predict(z)
    dup_ts.iloc[i]=predict_result[0][0]
    anlyse_data=z.ravel()#取最后一个并且降维
    anlyse_data=anlyse_data[1:]#剔除第一个
    anlyse_data=np.append(anlyse_data,predict_result)
#x_test = np.reshape(x_test, (x_test.shape[0], 1, x_test.shape[1]))
    anlyse_datas=anlyse_data.reshape(1,anlyse_data.shape[0])#一维转二维
    anlyse_datass=anlyse_datas.reshape(1,1,anlyse_data.shape[0])#二维转三维
    z=anlyse_datass


# In[186]:


for i in np.arange(len(dup_ts)):
    
    dup_ts.iloc[i]=dup_ts.iloc[i]*(train_tra2.max()-train_tra2.min())+train_tra2.min()


# In[71]:


dup_ts


# In[187]:


dup_ts=dup_ts.to_frame()


# In[87]:


dup_ts.to_csv('第一大问第一小问LSTM.csv',index=0)


# In[73]:


f, ax = plt.subplots(nrows=1, ncols=1, figsize=(10, 8))
plt.plot(dup_ts)
plt.show()


# In[88]:


plt.savefig('anser1.png')


# In[ ]:




