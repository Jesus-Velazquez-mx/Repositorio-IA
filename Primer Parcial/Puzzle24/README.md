# Solucionador del 24-Puzzle (5x5)

Esta es una implementación avanzada y optimizada en Java para resolver el problema del 24-Puzzle en un tablero de 5x5.

Debido a la explosión combinatoria del tablero ($1.55 \times 10^{25}$ permutaciones), los algoritmos tradicionales como A* o BFS saturan rápidamente la memoria. Para solucionarlo, este proyecto utiliza el algoritmo **IDA*** (Iterative Deepening A*), el cual garantiza encontrar la ruta más corta manteniendo un consumo de memoria lineal.

# Algoritmos y Optimización

* **Búsqueda Principal:** Algoritmo IDA* guiado por la función de costo $f = g + h$. El costo $g$ es dinámico: mover una pieza cuesta el valor numérico de la misma, no un simple 1.
* **Heurísticas Implementadas:**
1. **Piezas descolocadas:** Conteo básico de piezas fuera de su lugar final.
2. **Distancia de Manhattan:** Cálculo de ruta óptima en 2D, excluyendo el espacio vacío ('0') para mantener la admisibilidad.
3. **Conflicto Lineal:** Extensión informada de Manhattan que penaliza con +2 movimientos a las piezas que se bloquean mutuamente en la misma fila o columna, reduciendo drásticamente los nodos expandidos.


* **Ingeniería de Memoria:** Uso de una Tabla de Búsqueda (Lookup Table) para consultar posiciones en tiempo $O(1)$ y sistema de poda para evitar ciclos infinitos de movimientos.

### Cómo ejecutar:

Solo necesitas compilar y correr la clase principal (`Main` / `App`). El programa despliega un menú interactivo en consola (CLI) que te permite:

* Definir estados iniciales y metas personalizados (cadenas de 25 caracteres).
* Generar estados aleatorios con dificultad controlada (garantizando que tengan solución).
* Ejecutar una **Tabla de Rendimiento** para comparar el tiempo, costo y nodos expandidos entre las diferentes heurísticas.
