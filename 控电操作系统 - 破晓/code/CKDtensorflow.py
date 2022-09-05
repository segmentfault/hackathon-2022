#导入必需的库

import tensorflow as tf
import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
import win32com.client


#加载样例数据

df  = pd.read_csv('Power consumption.csv')
df.info


#绘制

#print("Painting the correlations")
sns.scatterplot(df['centigrade'], df['Power consumption'])
plt.plot()
plt.show()

#训练模型

#print("Define input(X) and output(Y) variables")
X_train=df['centigrade']
y_train=df['Power consumption']

#创建神经网络

#print("Creating the model")
#现在，让我们使用“keras.Sequential”方法来创建一个神经网络，其中依次添加“layers”。每一个层（layer）都具有逐步提取输入数据以获得所需输出的功能。
# Keras  是一个用 Python 写的库，我们创建神经网络并使用不同的机器学习框架，例如 TensorFlow。
# 接下来，我们将使用“add”方法向模型添加一个层。
model = tf.keras.Sequential()
model.add(tf.keras.layers.Dense(units=1,input_shape=[1]))

#编译模型

#print("Compiling the model")
#我们将设置一个优化器和损失函数，它们会测量我们的模型的准确性.
#Adam 优化是一种基于第一次和第二次矩的自适应预算的随机梯度下降算法。
# 为此，我们将使用基于平均方差的损失函数，它测量了我们预测的平均方差。我们的模型的目标是最小化这个函数。
model.compile(optimizer=tf.keras.optimizers.Adam(1), loss='mean_squared_error')

#训练模型

print ("Training the model")
epochs_hist = model.fit(X_train, y_train, epochs = 250)

#评估模型

print("Evaluating the model")
print(epochs_hist.history.keys())
#graph
plt.plot(epochs_hist.history['loss'])
plt.title('Evolution of the error associated with the model')
plt.xlabel('Epoch')
plt.ylabel('Training Loss')
plt.legend('Training Loss')
plt.show()

#进行预测
lowtp = int(input("输入当日最低气温："))
hightp = int(input("输入当日最高气温："))
sum_predicted_Power_consumption = int(0)
for centigrade in range(lowtp,hightp+1):
    predicted_Power_consumption = model.predict([centigrade])
    sum_predicted_Power_consumption = sum_predicted_Power_consumption+predicted_Power_consumption
ave_predicted_Power_consumption = sum_predicted_Power_consumption/(hightp-lowtp)
print("ave_Power_consumption" + str(ave_predicted_Power_consumption))
speaker = win32com.client.Dispatch("SAPI.SpVoice")#SPEAKER的调用
speaker.Speak("根据今日气温分析 某地今日能耗线为{:}万千瓦时".format(ave_predicted_Power_consumption))#朗读程序


speaker = win32com.client.Dispatch("SAPI.SpVoice")#SPEAKER的调用
speaker.Speak("请注意，您已超过能耗线，是否需要断电处理")#朗读程序