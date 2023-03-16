#pip install tensorflow
import tensorflow as tf
modelo_hdf5 = tf.keras.models.load_model('modelo.hdf5')
converter = tf.lite.TFLiteConverter.from_keras_model(modelo_hdf5)
tflite_model= converter.convert()

with open('modeloConvertido.tflite', 'wb') as f:
    f.write(tflite_model)