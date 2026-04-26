import tensorflow as tf
from tensorflow.keras import layers, models
from tensorflow.keras.preprocessing.image import ImageDataGenerator

# --- CONFIGURACIÓN (MANTENEMOS TUS VALORES) ---
IMG_SIZE = (160, 160)
BATCH_SIZE = 32

# RUTAS DE TU PROYECTO REAL (Asegúrate de verificarlas)
TRAIN_DIR = r'C:\Users\jesu1\OneDrive\Documentos\Tecnológico de Culiacán\Semestre 8\Inteligencia Artificial\Repositorio IA\Segundo Parcial\Reconocimiento Facial\data\train'
VAL_DIR = r'C:\Users\jesu1\OneDrive\Documentos\Tecnológico de Culiacán\Semestre 8\Inteligencia Artificial\Repositorio IA\Segundo Parcial\Reconocimiento Facial\data\val'

# --- 1. CARGAR DATOS CON DATA AUGMENTATION ROBUSTECIDA ---
# Hemos añadido zoom, shift y brillo para simular condiciones reales de webcam.
train_datagen = ImageDataGenerator(
    rescale=1./255, 
    rotation_range=30,          # Aumentado para manejar cabezas inclinadas
    horizontal_flip=True,
    width_shift_range=0.2,     # Añadido para simular caras no centradas
    height_shift_range=0.2,    
    zoom_range=0.2,            # Añadido para simular distancias variables
    brightness_range=[0.8, 1.2] # Añadido para simular cambios de iluminación
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

# --- 2. CONSTRUIR MODELO BASE (PRE-ENTRENADO) ---
base_model = tf.keras.applications.MobileNetV2(
    input_shape=(160, 160, 3), 
    include_top=False, 
    weights='imagenet'
)

# --- FASE 1: CONGELAR BASE Y ENTRENAR CLASIFICADOR ---
# Congelamos toda la base para estabilizar el clasificador final.
print("\n--- INICIANDO FASE 1: ESTABILIZACIÓN DEL CLASIFICADOR ---")
base_model.trainable = False 

model = models.Sequential([
    base_model,
    layers.GlobalAveragePooling2D(),
    layers.Dense(128, activation='relu'), # Capa extra para más capacidad
    layers.Dropout(0.5), # Añadido Dropout para evitar sobreajuste
    layers.Dense(len(train_gen.class_indices), activation='softmax')
])

# Compilación estándar para la fase 1
model.compile(
    optimizer='adam', 
    loss='sparse_categorical_crossentropy', 
    metrics=['accuracy']
)

# Entrenamos solo el clasificador durante pocas épocas para estabilizarlo
model.fit(train_gen, validation_data=val_gen, epochs=10) # 10 épocas son suficientes aquí

# --- FASE 2: FINE-TUNING DE CAPAS SUPERIORES CON LR BAJO ---
print("\n--- INICIANDO FASE 2: FINE-TUNING PROFUNDO ---")

# Descongelamos la base completa para poder elegir cuáles capas descongelar
base_model.trainable = True

# Determinamos desde qué capa vamos a descongelar (las últimas capas son menos genéricas)
# Para MobileNetV2, descongelar desde la capa 100 en adelante es una buena táctica.
fine_tune_at = 100 
print(f"Número de capas en el modelo base: {len(base_model.layers)}")
print(f"Descongelando desde la capa {fine_tune_at} en adelante.")

# Congelamos las primeras 100 capas y descongelamos el resto
for layer in base_model.layers[:fine_tune_at]:
    layer.trainable = False

# RE-COMPILAMOS CON UNA TASA DE APRENDIZAJE EXTREMADAMENTE BAJA.
# Es crucial que sea muy baja (1e-5 o 1e-4) para no "destruir" las características pre-entrenadas.
model.compile(
    optimizer=tf.keras.optimizers.Adam(learning_rate=0.00001), # Learning Rate muy bajo
    loss='sparse_categorical_crossentropy', 
    metrics=['accuracy']
)

# Continuamos el entrenamiento profundizando en las capas descongeladas
total_epochs = 35 # Sumaremos 25 épocas más a las 10 iniciales
history_fine = model.fit(
    train_gen, 
    validation_data=val_gen, 
    epochs=total_epochs,
    initial_epoch=10 # Indica dónde empezar de la fase 1
)

# Guardamos el modelo definitivo
model.save('modelo_facial_profundo.h5')

class_names = list(train_gen.class_indices.keys())
print("Entrenamiento finalizado. Modelo guardado como 'modelo_facial_profundo.h5'. Clases identificadas:", class_names)