from tensorflow.keras import layers, Model, Sequential
from tensorflow.keras.layers import *

DENSE_KERNEL_INITIALIZER = {
    'class_name': 'VarianceScaling',
    'config': {
        'scale': 1. / 3.,
        'mode': 'fan_out',
        'distribution': 'uniform'
    }
}


def minVgg(feature, im_height=128, im_width=128, num_classes=1000):
    input_image = layers.Input(shape=(im_height, im_width, 3), dtype="float32")
    x = feature(input_image)
    x = layers.Flatten()(x)
    x = layers.Dropout(rate=0.5)(x)
    x = layers.Dense(2048, activation='relu',
                     kernel_initializer=DENSE_KERNEL_INITIALIZER)(x)
    x = layers.Dropout(rate=0.5)(x)
    x = layers.Dense(2048, activation='relu',
                     kernel_initializer=DENSE_KERNEL_INITIALIZER)(x)
    x = layers.Dense(num_classes,
                     kernel_initializer=DENSE_KERNEL_INITIALIZER)(x)
    output = layers.Softmax()(x)
    model = Model(inputs=input_image, outputs=output)
    return model


def make_feature():
    feature_layers = []
    feature_layers.append(Conv2D(8, kernel_size=5, padding="SAME", activation="relu"))
    feature_layers.append(MaxPool2D(pool_size=(2, 2)))
    feature_layers.append(Conv2D(24, kernel_size=5, padding="SAME", activation="relu"))
    feature_layers.append(MaxPool2D(pool_size=(2, 2)))
    feature_layers.append(Conv2D(56, kernel_size=3, activation="relu"))
    feature_layers.append(MaxPool2D(pool_size=(2, 2)))
    feature_layers.append(Conv2D(128, kernel_size=3, activation="relu"))
    feature_layers.append(MaxPool2D(pool_size=(2, 2)))
    return Sequential(feature_layers, name="feature")


def minvgg(im_height=128, im_width=128, num_classes=1000):
    model = minVgg(make_feature(), im_height=im_height, im_width=im_width, num_classes=num_classes)
    return model
