#importamos el VGG16, para la red de prueba
from keras.applications.vgg16 import VGG16
#pip install tensorflow lite 
import tensorflow as tf

#Modelo VGG16 descargado
#model = VGG16(weights = 'imagenet')
model = tf.keras.models.load_model('BalGen_Fotos_Inpaint_Parcial_ResNet50V2_K5.h5')
print('Modelo creado')
converter = tf.lite.TFLiteConverter.from_keras_model(model)
tflite_model= converter.convert()
print('Modelo convertido')

with open('modeloConvertido.tflite', 'wb') as f:
    f.write(tflite_model)
print('Modelo TFLITE creado')
#otra forma de hacerlo seria, en la carpeta documentos, poner este comando  tflite_convert --output_file="model.tflite" --keras_model_file="model.h5"