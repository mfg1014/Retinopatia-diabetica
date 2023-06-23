#pip install numpy
#pip install pandas
#pip install matplotlib
#pip install scikit-learn
#pip install tensorflow

from keras.applications.vgg16 import VGG16

import tensorflow as tf


file = "./"
nombre = "modelo2-0.003int2_0.9166666720476415_20230603-003149"
fileSalida = "./"

model = tf.keras.models.load_model(file+nombre+'.h5')
print('Modelo cargado')
converter = tf.lite.TFLiteConverter.from_keras_model(model)
tflite_model= converter.convert()
print('Modelo convertido')
nombreSalida = "calidad.tflite"
with open(fileSalida+nombre+'.tflite', 'wb') as f:
    f.write(tflite_model)
print('Modelo TFLITE creado')
#otra forma de hacerlo seria, en la carpeta documentos, poner este comando  tflite_convert --output_file="model.tflite" --keras_model_file="model.h5"