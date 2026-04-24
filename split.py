import os
import shutil
import random

# --- CONFIGURACIÓN DE RUTAS ---
# 1. Pon la ruta de tu carpeta actual (la que tiene las 300-400 fotos de todas las clases)
ruta_train = r'C:\Users\jesu1\OneDrive\Documentos\Tecnológico de Culiacán\Semestre 8\Inteligencia Artificial\Repositorio IA\Segundo Parcial\Reconocimiento Facial\data\train'

# 2. Pon la ruta donde quieres que se cree la nueva carpeta de validación
ruta_val = r'C:\Users\jesu1\OneDrive\Documentos\Tecnológico de Culiacán\Semestre 8\Inteligencia Artificial\Repositorio IA\Segundo Parcial\Reconocimiento Facial\data\val'

# Porcentaje que se irá a validación (20%)
porcentaje_val = 0.2

print("Iniciando separación de imágenes...")

# Crear la carpeta 'val' principal si no existe
if not os.path.exists(ruta_val):
    os.makedirs(ruta_val)

# Iterar sobre cada subcarpeta (cada persona/clase dentro de tu train)
for clase in os.listdir(ruta_train):
    ruta_clase_train = os.path.join(ruta_train, clase)

    # Asegurarnos de que estamos leyendo una carpeta y no un archivo suelto
    if os.path.isdir(ruta_clase_train):
        # Crear la subcarpeta correspondiente en 'val' (ej. val/Ana, val/Beto)
        ruta_clase_val = os.path.join(ruta_val, clase)
        if not os.path.exists(ruta_clase_val):
            os.makedirs(ruta_clase_val)

        # Obtener todas las fotos que hay actualmente de esta persona
        imagenes = os.listdir(ruta_clase_train)
        
        # Calcular exactamente cuántas son el 20%
        cantidad_a_mover = int(len(imagenes) * porcentaje_val)

        # Seleccionar imágenes al azar para asegurar variedad en el examen
        imagenes_a_mover = random.sample(imagenes, cantidad_a_mover)

        # MOVER (Cortar y pegar) las imágenes de train a val
        for img in imagenes_a_mover:
            origen = os.path.join(ruta_clase_train, img)
            destino = os.path.join(ruta_clase_val, img)
            shutil.move(origen, destino)

        print(f"✔️ Clase '{clase}': {cantidad_a_mover} fotos movidas a validación.")

print("\n¡Proceso terminado! Tus fotos ya están separadas 80/20.")