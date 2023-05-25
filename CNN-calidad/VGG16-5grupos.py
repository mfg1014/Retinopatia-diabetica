#pip install pandas
#pip install scipy
#pip install scikit-learn
import time
import pandas as pd
from tensorflow.keras.preprocessing.image import ImageDataGenerator
from tensorflow.keras.applications import VGG16
from tensorflow.keras.models import Sequential
import tensorflow.keras.layers 
from tensorflow.keras.optimizers import Adam
import cv2
from sklearn.model_selection import train_test_split
from tensorflow.keras.applications.vgg16 import preprocess_input
import matplotlib.pyplot as plt
import os
from os import path

folderPath = "graficos5"

def crearModelo():
# Crear un nuevo modelo secuencial y agregar la VGG16 y la capa clasificadora
    vgg16 = VGG16(include_top=False, weights='imagenet', input_shape=(224, 224, 3))
    
    # Congelar las capas de la red VGG16
    for layer in vgg16.layers:
        layer.trainable = False
    model = Sequential()
    model.add(vgg16)
    model.add(tensorflow.keras.layers.Flatten())
    model.add(tensorflow.keras.layers.Dense(64, activation='relu'))
    model.add(tensorflow.keras.layers.Dense(num_classes, activation='softmax'))
    return model



# Ruta del archivo CSV y el directorio de imágenes
if path.exists(folderPath):
    for filename in os.listdir(folderPath):
        filepath = path.join(folderPath, filename)
        os.remove(filepath)
else:
    os.mkdir(folderPath)


rango = [0.001,0.002,0.003,0.004,0.005,0.006,0.007,0.008,0.009,0.01]

csv_file = 'valor_calidad.csv'
image_dir = './dataset'
num_classes = 5

for i in rango:
    for j in range(3):

        # Leer el archivo CSV
        data = pd.read_csv(csv_file)
        # Dividir los datos en conjunto de entrenamiento y prueba
        train_data, test_data = train_test_split(data, test_size=0.2,stratify=data['calidad'])

        # Dividir el conjunto de prueba en validación y prueba
        validation_data, test_data = train_test_split(test_data, test_size=0.5,stratify=test_data['calidad'])
        # Crear un generador de imágenes para preprocesamiento y aumento de datos
        datagen = ImageDataGenerator(preprocessing_function=preprocess_input)  # Normalización y división de entrenamiento/validación

        train_data['calidad'] = train_data['calidad'].replace({1: 'nula', 2: 'mala', 3: 'baja', 4: 'decente', 5: 'buena'})
        test_data['calidad'] = test_data['calidad'].replace({1: 'nula', 2: 'mala', 3: 'baja', 4: 'decente', 5: 'buena'})
        validation_data['calidad'] = validation_data['calidad'].replace({1: 'nula', 2: 'mala', 3: 'baja', 4: 'decente', 5: 'buena'})
        
        
        # Cargar las imágenes del directorio y dividirlas en conjuntos de entrenamiento y validación
        # Crear generadores de imágenes para cada conjunto
        train_generator = datagen.flow_from_dataframe(
            dataframe=train_data,
            directory=image_dir,
            x_col='imagen',
            y_col='calidad',
            batch_size=32,
            shuffle=True,
            class_mode='categorical',
            target_size=(224, 224)
        )

        validation_generator = datagen.flow_from_dataframe(
            dataframe=validation_data,
            directory=image_dir,
            x_col='imagen',
            y_col='calidad',
            batch_size=32,
            shuffle=True,
            class_mode='categorical',
            target_size=(224, 224)
        )

        test_generator = datagen.flow_from_dataframe(
            dataframe=test_data,
            directory=image_dir,
            x_col='imagen',
            y_col='calidad',
            batch_size=32,
            shuffle=False,
            class_mode='categorical',
            target_size=(224, 224)
        )



        model = crearModelo()
        # Compilar el modelo
        model.compile(optimizer=Adam(learning_rate=i), loss='categorical_crossentropy', metrics=['accuracy','mse'])

        # Entrenar el modelo
        history = model.fit(train_generator, validation_data=validation_generator, epochs=10)

        # Evaluar el modelo en el conjunto de prueba
        test_loss, test_acc,test_mse = model.evaluate(test_generator)

        # Guardar el modelo
        filename = "modelo5-"+str(i)+'int'+str(j)+ '_' +  str(test_acc) + time.strftime("%Y%m%d-%H%M%S")
        model.save(path.join(folderPath, filename+'.h5'))

        #model.summary()
        plt.clf()
        plt.plot(history.history['accuracy'], label='accuracy')
        plt.plot(history.history['val_accuracy'], label = 'val_accuracy')
        plt.xlabel('Epoch')
        plt.ylabel('Accuracy')

        plt.legend(loc='lower right')
        #plt.show()
        
        plt.savefig(path.join(folderPath, filename+'.png'))