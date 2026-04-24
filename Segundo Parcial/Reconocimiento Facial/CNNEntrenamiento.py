import tensorflow as tf
from tensorflow.keras import layers, models
from tensorflow.keras.preprocessing.image import ImageDataGenerator

# Configuración
IMG_SIZE = (160, 160)
BATCH_SIZE = 32

# Rutas
TRAIN_DIR = r'C:\Users\jesu1\OneDrive\Documentos\Tecnológico de Culiacán\Semestre 8\Inteligencia Artificial\Repositorio IA\Segundo Parcial\Reconocimiento Facial\data\train'
VAL_DIR = r'C:\Users\jesu1\OneDrive\Documentos\Tecnológico de Culiacán\Semestre 8\Inteligencia Artificial\Repositorio IA\Segundo Parcial\Reconocimiento Facial\data\val'

# 1. Cargar Datos con Aumento
train_datagen = ImageDataGenerator(
    rescale=1./255, 
    rotation_range=20, 
    horizontal_flip=True
)
val_datagen = ImageDataGenerator(rescale=1./255)

train_gen = train_datagen.flow_from_directory(
    TRAIN_DIR, 
    target_size=IMG_SIZE, 
    batch_size=BATCH_SIZE, 
    class_mode='sparse',
    color_mode='rgb' 
)

val_gen = val_datagen.flow_from_directory(
    VAL_DIR, 
    target_size=IMG_SIZE, 
    batch_size=BATCH_SIZE, 
    class_mode='sparse',
    color_mode='rgb'
)

# 2. Construir Modelo
base_model = tf.keras.applications.MobileNetV2(
    input_shape=(160, 160, 3), 
    include_top=False, 
    weights='imagenet'
)
base_model.trainable = False # Congelamos los pesos base

model = models.Sequential([
    base_model,
    layers.GlobalAveragePooling2D(),
    layers.Dense(len(train_gen.class_indices), activation='softmax')
])

model.compile(
    optimizer='adam', 
    loss='sparse_categorical_crossentropy', 
    metrics=['accuracy']
)

# 3. Entrenar y Guardar
print("Iniciando el entrenamiento")
history = model.fit(train_gen, validation_data=val_gen, epochs=25)

# Guardar el modelo
model.save('modelo_facial_proyecto_real.h5')

# Guardar e imprimir nombres de las clases
class_names = list(train_gen.class_indices.keys())
print("Entrenamiento finalizado. Clases identificadas:", class_names)