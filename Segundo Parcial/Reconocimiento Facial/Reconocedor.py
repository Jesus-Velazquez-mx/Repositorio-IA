import cv2
import numpy as np
import tensorflow as tf
import re
import time

# Variable global para mantener el ciclo activo
ejecutando = True

# 1. Configuración del Botón de Cerrar en OpenCV
btn_x, btn_y, btn_w, btn_h = 10, 10, 120, 40

def click_boton_cerrar(event, x, y, flags, param):
    global ejecutando
    # Si hacen clic izquierdo, verificamos si fue dentro de las coordenadas del botón
    if event == cv2.EVENT_LBUTTONDOWN:
        if btn_x <= x <= btn_x + btn_w and btn_y <= y <= btn_y + btn_h:
            ejecutando = False

# 2. Función para formatear el nombre (A1_JoanSebastian -> Joan Sebastian)
def limpiar_nombre(nombre_crudo):
    if '_' in nombre_crudo:
        nombre = nombre_crudo.split('_', 1)[1]
    else:
        nombre = nombre_crudo
    
    nombre_limpio = re.sub(r"([a-z])([A-Z])", r"\1 \2", nombre)
    return nombre_limpio

# Función para dibujar solo las esquinas del recuadro
def dibujar_esquinas(frame, x, y, w, h, color, grosor=2, longitud=25):
    cv2.line(frame, (x, y), (x + longitud, y), color, grosor)
    cv2.line(frame, (x, y), (x, y + longitud), color, grosor)
    cv2.line(frame, (x + w, y), (x + w - longitud, y), color, grosor)
    cv2.line(frame, (x + w, y), (x + w, y + longitud), color, grosor)
    cv2.line(frame, (x, y + h), (x + longitud, y + h), color, grosor)
    cv2.line(frame, (x, y + h), (x, y + h - longitud), color, grosor)
    cv2.line(frame, (x + w, y + h), (x + w - longitud, y + h), color, grosor)
    cv2.line(frame, (x + w, y + h), (x + w, y + h - longitud), color, grosor)

# 3. Cargar tu Modelo
model_path = r'C:\Users\jesu1\OneDrive\Documentos\Tecnológico de Culiacán\Semestre 8\Inteligencia Artificial\Repositorio IA\Segundo Parcial\Reconocimiento Facial\modelo_facial_profundo.h5'
print("Cargando modelo neuronal... por favor espera.")
model = tf.keras.models.load_model(model_path)
print("¡Modelo cargado exitosamente!")

# 4. Tus clases 
mis_clases = ['A1_JorgeAstorga', 'A2_JesusVelazquez', 'F1_AlisonSudol', 'F2_AaronAshmore', 'F3_AlexisBledel', 'F4_AliRose', 'F5_AllenLeech', 'F6_AlonaTal', 'F7_AlysonHannigan', 'F8_AlonzoBodden'] 

# 5. Inicializar la cámara y ventana
cap = cv2.VideoCapture(0)
face_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')

# Crear la ventana ANTES del bucle para poder asignarle el evento del mouse
cv2.namedWindow('Reconocimiento Facial')
cv2.setMouseCallback('Reconocimiento Facial', click_boton_cerrar)

# Configuraciones de la interfaz
box_size = 160
tiempo_espera = 3.0
tiempo_inicio_deteccion = None

while ejecutando:
    ret, frame = cap.read()
    if not ret:
        print("Error al acceder a la cámara")
        break
        
    frame = cv2.flip(frame, 1)
    frame_copy = frame.copy()
    altura, anchura = frame.shape[:2]
    
    # Dibujar el botón CERRAR en la esquina superior izquierda
    cv2.rectangle(frame_copy, (btn_x, btn_y), (btn_x + btn_w, btn_y + btn_h), (0, 0, 255), -1)
    cv2.putText(frame_copy, "CERRAR", (btn_x + 20, btn_y + 25), cv2.FONT_HERSHEY_SIMPLEX, 0.6, (255, 255, 255), 2)
    
    x_start = int(anchura/2 - box_size/2)
    y_start = int(altura/2 - box_size/2)
    x_end = x_start + box_size
    y_end = y_start + box_size
    
    cara_alineada = False
    cara_procesar_temporal = None
    
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    rostros = face_cascade.detectMultiScale(gray, scaleFactor=1.1, minNeighbors=5, minSize=(80, 80))
    
    for (x, y, w, h) in rostros:
        centro_cara_x = x + w/2
        centro_cara_y = y + h/2
        
        if (x_start < centro_cara_x < x_end) and (y_start < centro_cara_y < y_end):
            cara_alineada = True
            cara_procesar_temporal = frame[max(0, y-20):min(altura, y+h+20), max(0, x-20):min(anchura, x+w+20)]
            break

    if cara_alineada:
        color_cuadro = (0, 255, 0)
        
        if tiempo_inicio_deteccion is None:
            tiempo_inicio_deteccion = time.time()
            
        tiempo_transcurrido = time.time() - tiempo_inicio_deteccion
        segundos_restantes = max(1, int(np.ceil(tiempo_espera - tiempo_transcurrido)))
        texto_guia = f"Mantente quieto... {segundos_restantes}"
        
        if tiempo_transcurrido >= tiempo_espera:
            cara_procesar = cara_procesar_temporal
            tiempo_inicio_deteccion = None 
        else:
            cara_procesar = None
            
    else:
        color_cuadro = (0, 0, 255)
        texto_guia = "Coloca tu rostro aqui"
        tiempo_inicio_deteccion = None
        cara_procesar = None

    dibujar_esquinas(frame_copy, x_start, y_start, box_size, box_size, color_cuadro, grosor=2, longitud=25)
    cv2.putText(frame_copy, texto_guia, (x_start - 20, y_start - 15), cv2.FONT_HERSHEY_SIMPLEX, 0.6, color_cuadro, 2)

    cv2.imshow('Reconocimiento Facial', frame_copy)

    # 6. Procesar y mostrar resultado en UNA sola ventana
    if cara_procesar is not None and cara_procesar.size > 0:
        cara_rgb = cv2.cvtColor(cara_procesar, cv2.COLOR_BGR2RGB)
        cara_resized = cv2.resize(cara_rgb, (160, 160))
        img_array = cara_resized / 255.0
        img_array = np.expand_dims(img_array, axis=0)
        
        predictions = model.predict(img_array, verbose=0)
        score = np.max(predictions)
        class_idx = np.argmax(predictions)
        
        nombre_real = limpiar_nombre(mis_clases[class_idx])
        
        # --- CREAR EL PANEL DE RESULTADO COMBINADO ---
        # Creamos un lienzo negro de 200 de alto x 450 de ancho
        panel_resultado = np.zeros((200, 450, 3), dtype=np.uint8)
        
        # Pegamos la foto redimensionada en el lado izquierdo del panel
        panel_resultado[20:180, 20:180] = cv2.resize(cara_procesar, (160, 160))
        
        # Escribimos los textos en el lado derecho del panel
        cv2.putText(panel_resultado, "Identificado como:", (200, 60), cv2.FONT_HERSHEY_SIMPLEX, 0.6, (255, 255, 255), 1)
        cv2.putText(panel_resultado, nombre_real, (200, 100), cv2.FONT_HERSHEY_SIMPLEX, 0.8, (0, 255, 0), 2)
        cv2.putText(panel_resultado, f"Confianza: {100 * score:.2f}%", (200, 150), cv2.FONT_HERSHEY_SIMPLEX, 0.6, (255, 255, 255), 1)
        
        # Mostramos el panel (Esto pausa la cámara para que veas el resultado)
        cv2.imshow('Resultado', panel_resultado)
        cv2.waitKey(10000) # Se queda en pantalla 10 segundos (10000 ms)
        cv2.destroyWindow('Resultado') # Se cierra sola y la cámara continúa
        
        tiempo_inicio_deteccion = None 

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()