#pip install numpy
#pip install pandas
#pip install matplotlib
#pip install scikit-learn
#pip install tensorflow

import time
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from sklearn.metrics import confusion_matrix
from tensorflow.keras.preprocessing.image import ImageDataGenerator
from tensorflow.keras.applications import VGG16
from tensorflow.keras.models import Sequential
import tensorflow.keras.layers 
from tensorflow.keras.optimizers import Adam
from tensorflow.keras.metrics import Precision,Recall
from sklearn.model_selection import train_test_split
from tensorflow.keras.applications.vgg16 import preprocess_input
import os
from os import path

folderPath = "graficos2"


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
    model.add(tensorflow.keras.layers.Dense(num_classes, activation='sigmoid'))
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
num_classes = 1 
for i in rango:
    for j in range(3):
        # Leer el archivo CSV
        data = pd.read_csv(csv_file)

        # Agrupar los valores según sean iguales o menores que 3, y mayores que 3
        data['calidad'] = data['calidad'].apply(lambda x: 1 if x <= 3 else 0)
        data['calidad'] = data['calidad'].astype(str)
        # Dividir los datos en conjunto de entrenamiento y prueba
        train_data, test_data = train_test_split(data, test_size=0.2,stratify=data['calidad'])

        # Dividir el conjunto de prueba en validación y prueba
        validation_data, test_data = train_test_split(test_data, test_size=0.5,stratify=test_data['calidad'])


        # Crear un generador de imágenes para preprocesamiento y aumento de datos
        datagen = ImageDataGenerator(preprocessing_function=preprocess_input)  # Normalización y división de entrenamiento/validación

        # Cargar las imágenes del directorio y dividirlas en conjuntos de entrenamiento y validación
        # Crear generadores de imágenes para cada conjunto
        train_generator = datagen.flow_from_dataframe(
            dataframe=train_data,
            directory=image_dir,
            x_col='imagen',
            y_col='calidad',
            batch_size=32,
            shuffle=True,
            class_mode='binary',
            target_size=(224, 224)
        )

        validation_generator = datagen.flow_from_dataframe(
            dataframe=validation_data,
            directory=image_dir,
            x_col='imagen',
            y_col='calidad',
            batch_size=32,
            shuffle=True,
            class_mode='binary',
            target_size=(224, 224)
        )

        test_generator = datagen.flow_from_dataframe(
            dataframe=test_data,
            directory=image_dir,
            x_col='imagen',
            y_col='calidad',
            batch_size=32,
            shuffle=False,
            class_mode='binary',
            target_size=(224, 224)
        )

        model = crearModelo()
        # Compilar el modelo
        model.compile(optimizer=Adam(learning_rate=i), loss='binary_crossentropy', metrics=[Precision(name='precision'),Recall(name='recall')])

        # Entrenar el modelo
        history = model.fit(train_generator, validation_data=validation_generator, epochs=10)

        # Evaluar el modelo en el conjunto de prueba
        test_results = model.evaluate(test_generator)

        precision = test_results[1]

        recall = test_results[2]
        F1_Score = 0
        if(precision+recall != 0):
            F1_Score =  2 * (precision * recall) / (precision + recall)

        # Guardar el modelo
        filename = "modelo2-"+str(i)+'int'+str(j)+ '_' + str(F1_Score) + '_' + time.strftime("%Y%m%d-%H%M%S")
        model.save(path.join(folderPath, filename+'.h5'))
        
        # Obtener las probabilidades de predicción para el conjunto de prueba
        y_pred_test = model.predict(test_generator)

        # Redondear las probabilidades para obtener las clases predichas (0 o 1)
        y_pred_test_classes = np.round(y_pred_test).astype(int)

        # Obtener las etiquetas reales para el conjunto de prueba
        y_test_classes = test_generator.classes

        # Calcular la matriz de confusión
        cm = confusion_matrix(y_test_classes, y_pred_test_classes)

        # Definir los colores para la matriz de confusión
        plt.clf()
        fig, ax = plt.subplots()
        ax.matshow(cm, cmap=plt.cm.Blues)

        # Añadir los números de la matriz de confusión a la tabla
        for k in range(cm.shape[0]):
            for l in range(cm.shape[1]):
                ax.text(x=l, y=k, s=cm[k, l], va='center', ha='center')

        # Configurar los ejes x e y
        ax.set_xlabel('Predicted labels')
        ax.set_ylabel('True labels')
        tick_marks = np.arange(2)
        plt.xticks(tick_marks, ['APTO', 'NO_APTO'])
        plt.yticks(tick_marks, ['APTO', 'NO_APTO'])
        # Guardar la imagen
        plt.savefig(path.join(folderPath, filename+'.png'))
        
