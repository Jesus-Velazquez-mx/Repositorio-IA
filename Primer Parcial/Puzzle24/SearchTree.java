import java.util.*;

public class SearchTree {

    private Node root;
    /* Nodo raíz del árbol */
    private String goalSate;
    /* Estado objetivo a alcanzar */
    private int nodesExpanded = 0;
    /* Contador para el análisis de rendimiento */
    private Node solutionNode = null;
    /* Variable para guardar la solución */

 /* * TABLA DE BUSQUEDA (Lookup Table) PARA OPTIMIZACION
     * El numero 256 cubre todos los caracteres ASCII posibles. 
     * En lugar de usar el metodo .indexOf() que es muy lento porque 
     * escanea el texto letra por letra, guardamos la posicion meta 
     * de cada pieza usando la letra misma como indice del arreglo. 
     * Ejemplo: goalPositions['A'] nos da su posicion al instante.
     * Esto le ahorra al procesador millones de busquedas de texto.
     */
    private final int[] goalPositions = new int[256];

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public String getGoalSate() {
        return goalSate;
    }

    public void setGoalSate(String goalSate) {
        this.goalSate = goalSate;
        /* Si se actualiza el estado objetivo, recalculamos las posiciones */
        for (int i = 0; i < this.goalSate.length(); i++) {
            goalPositions[this.goalSate.charAt(i)] = i;
        }
    }

    public SearchTree(Node root, String goalSate) {
        this.root = root;
        this.goalSate = goalSate;

        /* LLENADO DE LA TABLA DE BUSQUEDA (Un solo escaneo)
         * Recorremos la cadena meta (goalSate) una sola vez al crear el arbol.
         * Tomamos cada caracter (ej. la letra 'A') y guardamos su posicion (i).
         * Si la 'A' esta en el indice 10, internamente hace: goalPositions['A'] = 10;
         * Esto prepara la memoria para que las heuristicas consulten los datos al instante.
         */
        for (int i = 0; i < goalSate.length(); i++) {
            goalPositions[goalSate.charAt(i)] = i;
        }
    }

    /**
     * Algoritmo IDA* (Iterative Deepening A*). Utiliza búsqueda en profundidad
     * con un límite de costo f que aumenta.
     */

    /* El umbral (threshold) funciona como un 'presupuesto' de costo total (f = g + h). 
      * En cada iteración, el algoritmo realiza una búsqueda en profundidad pero detiene 
      * la exploración si un camino supera este límite, lo que evita el consumo excesivo 
      * de memoria RAM en el tablero de 5x5. 
      * Si no se encuentra la meta con el presupuesto actual, el umbral se actualiza
      * automáticamente al valor mínimo que excedió el límite anterior y la búsqueda se reinicia desde la raíz. 
     */
    public void idaStar(Heuristic heuristicType) {
        nodesExpanded = 0;
        solutionNode = null;
        Node rootNode = new Node(root.getState());
        /* El umbral inicial es la heurística del nodo raíz */
        int threshold = calculateHeuristic(rootNode.getState(), heuristicType);

        while (solutionNode == null) {
            System.out.println("Buscando con presupuesto (threshold): " + threshold);
            /* Ejecutamos la búsqueda. El resultado será el nuevo umbral o -1 si hallamos la meta */
            int result = recursiveSearch(rootNode, 0, threshold, heuristicType);

            if (result == -1) {
                /* Solución encontrada y guardada en solutionNode */
                NodeUtil.printSolution(solutionNode, new HashSet<>(), root, nodesExpanded);
                return;
            }

            if (result == Integer.MAX_VALUE) {
                System.out.println("No se pudo encontrar una solución.");
                return;
            }
            /* Incrementamos el umbral al valor mínimo que superó el anterior */
            threshold = result;
        }
    }

    /**
     * Búsqueda recursiva para IDA*. Devuelve el siguiente umbral o -1 si tiene
     * éxito.
     */
    private int recursiveSearch(Node node, int g, int threshold, Heuristic hType) {
        nodesExpanded++;
        int h = calculateHeuristic(node.getState(), hType);
        int f = g + h;
        /* Costo total f = g + h */

        /* Si el costo f supera el umbral actual, podamos esta rama */
        if (f > threshold) {
            return f;
        }
        /* Si llegamos al estado objetivo */
        if (node.getState().equals(goalSate)) {
            solutionNode = node;
            return -1;
            /* Flag para indicar éxito */
        }

        int min = Integer.MAX_VALUE;
        List<String> successors = NodeUtil.getSuccessors(node.getState());
        for (String s : successors) {
            /* Evitar ciclos regresando directamente al padre */
            if (node.getParent() != null && s.equals(node.getParent().getState())) {
                continue;
            }
            Node child = new Node(s);
            child.setParent(node);
            /* Costo de movimiento basado en el valor de la pieza */
            int moveCost = Character.getNumericValue(s.charAt(node.getState().indexOf('0')));
            /* Llamada recursiva incrementando el costo g acumulado */
            int result = recursiveSearch(child, g + moveCost, threshold, hType);
            if (result == -1) {
                return -1;
                /* Si el hijo encontró la meta, propagamos el éxito */
            }
            if (result < min) {
                min = result;
                /* Guardamos el f mínimo que superó el umbral */
            }
        }
        return min;
    }

    /* Ayudante para seleccionar la función heurística.
     */
    private int calculateHeuristic(String state, Heuristic type) {
        if (type == Heuristic.H_TWO) {
            return heuristicTwo(state, goalSate);
        }
        if (type == Heuristic.H_THREE) {
            return linearConflict(state, goalSate);
        }
        return heuristicOne(state, goalSate);
    }

    /**
     * Heuristica 1: Piezas fuera de lugar (Es la mas sencilla, util para probar otros metodos).
     */
    private int heuristicOne(String currentState, String goalSate) {
        int difference = 0;
        for (int i = 0; i < currentState.length(); i++) {
            if (currentState.charAt(i) != goalSate.charAt(i) && currentState.charAt(i) != '0') {
                difference++;
            }
        }
        return difference;
    }

    /**
     * Heuristica 2: Distancia de Manhattan ajustada para tablero de 5x5. Suma
     * de las distancias verticales y horizontales de cada pieza.
     */
    private int heuristicTwo(String currentState, String goalState) {
        int distance = 0;
        for (int i = 0; i < currentState.length(); i++) {
            char tile = currentState.charAt(i);
            if (tile != '0') {
                /* Consulta directa en arreglo en lugar de indexOf */
                int targetIdx = goalPositions[tile]; // Posicion objetivo de la pieza actual al final del juego
                /* 
                 * Conversion de 1D a 2D: El tablero esta guardado como una sola 
                * cadena de 25 caracteres, pero fisicamente es una cuadricula de 5x5.
                * - El eje X (columna) se obtiene con el modulo (% 5).
                * - El eje Y (fila) se obtiene con la division entera (/ 5).
                * Formula de Manhattan: Calcula la ruta mas corta entre dos puntos 
                * sin permitir movimientos diagonales. La formula formal es:
                * d = |x_1 - x_2| + |y_1 - y_2|
                * Restamos las coordenadas actuales (current) menos las coordenadas 
                * objetivo (target) y usamos el valor absoluto (Math.abs) para obtener 
                * la cantidad exacta de movimientos horizontales y verticales que le 
                * faltan a esa pieza para llegar a su destino final.
                 */
                int currentX = i % 5;
                int currentY = i / 5;
                int targetX = targetIdx % 5;
                int targetY = targetIdx / 5;
                distance += Math.abs(currentX - targetX) + Math.abs(currentY - targetY);
            }
        }
        return distance;
    }

    /**
     * Heuristica 3: Conflicto Lineal. Extensión de la Distancia de Manhattan
     * con penalizaciones por piezas invertidas.
     */
    
    /* 
     * La Distancia de Manhattan es optimista y asume que las piezas pueden
     * atravesarse. El Conflicto Lineal corrige esto añadiendo una penalizacion.
     * * Existe un conflicto si dos piezas cumplen 3 reglas:
     * 1. Estan en la misma fila (o columna).
     * 2. El destino final de ambas es esa misma fila (o columna).
     * 3. Estan invertidas (la que deberia ir a la derecha esta a la izquierda).
     * * Para desatorarlas, una pieza debe salir de la fila (+1 mov) y luego
     * volver a entrar (+1 mov). Por eso cada conflicto suma 2 al costo total.
     * Esto vuelve a la heuristica mucho mas exacta y reduce los nodos expandidos.
     */
    private int linearConflict(String currentState, String goalSate) {
        int manhattan = heuristicTwo(currentState, goalSate);
        int conflicts = 0;

        /* Conflictos en Filas */
        for (int row = 0; row < 5; row++) {  // Eje y (filas)
            /* Comparamos todas las combinaciones de pares de piezas en la misma fila.
             * Al usar j = i + 1, aseguramos que la pieza 1 (t1) siempre este a la 
             * izquierda fisica de la pieza 2 (t2).
             */
            for (int i = 0; i < 5; i++) { // Eje x (columnas) 
                for (int j = i + 1; j < 5; j++) {
                    /* Convertimos las coordenadas 2D del ciclo (fila, columna) al indice 1D del String */
                    int idx1 = row * 5 + i;
                    int idx2 = row * 5 + j;
                    /* Extraemos las piezas actuales de esas posiciones */
                    char t1 = currentState.charAt(idx1);
                    char t2 = currentState.charAt(idx2);
                    /* Ignoramos el espacio vacio (0) porque no genera conflictos de bloqueo */
                    if (t1 != '0' && t2 != '0') {
                        /* Buscamos cual deberia ser el indice final de ambas piezas */
                        int gIdx1 = goalPositions[t1];
                        int gIdx2 = goalPositions[t2];
                        /* REGLA DEL CONFLICTO:
                         * 1. (gIdx1 / 5 == row): El destino de t1 es esta misma fila.
                         * 2. (gIdx2 / 5 == row): El destino de t2 es esta misma fila.
                         * 3. (gIdx1 > gIdx2): Aunque t1 esta a la izquierda de t2 fisicamente, 
                         * su destino final le exige estar a la derecha de t2. Chocan
                         */
                        if (gIdx1 / 5 == row && gIdx2 / 5 == row && gIdx1 > gIdx2) {
                            conflicts++; /* Sumamos el conflicto encontrado */
                        }
                    }
                }
            }
        }

        /* Conflictos en Columnas: Escaneamos el tablero columna por columna (0 al 4) */
        for (int col = 0; col < 5; col++) { // Eje x (columnas)
            /* Comparamos todas las combinaciones de pares de piezas en la misma columna.
             * Al usar j = i + 1, aseguramos que la pieza 1 (t1) siempre este fisicamente 
             * arriba de la pieza 2 (t2).
             */
            for (int i = 0; i < 5; i++) { // Eje y (filas)
                for (int j = i + 1; j < 5; j++) {
                    /* Convertimos las coordenadas 2D del ciclo (fila, columna) al indice 1D del String 
                     * Nota que aqui multiplicamos 'i' y 'j' (las filas) por 5 y 
                     * le sumamos la columna actual.
                     */
                    int idx1 = i * 5 + col;
                    int idx2 = j * 5 + col;
                    /* Extraemos las piezas actuales de esas posiciones */
                    char t1 = currentState.charAt(idx1);
                    char t2 = currentState.charAt(idx2);
                    /* Ignoramos el espacio vacio (0) porque no genera bloqueos */
                    if (t1 != '0' && t2 != '0') {
                        /* Buscamos en tiempo O(1) cual deberia ser el indice meta de ambas piezas */
                        int gIdx1 = goalPositions[t1];
                        int gIdx2 = goalPositions[t2];
                        /* REGLA DEL CONFLICTO VERTICAL:
                         * 1. (gIdx1 % 5 == col): Usamos modulo para ver si el destino de t1 es esta columna.
                         * 2. (gIdx2 % 5 == col): Vemos si el destino de t2 tambien es esta columna.
                         * 3. (gIdx1 > gIdx2): Aunque t1 esta arriba de t2 fisicamente, su destino 
                         * final le exige estar abajo de t2. ¡Se van a estorbar en el pasillo!
                         */
                        if (gIdx1 % 5 == col && gIdx2 % 5 == col && gIdx1 > gIdx2) {
                            conflicts++; /* Sumamos el embotellamiento vertical encontrado */
                        }
                    }
                }
            }
        }
        
        /* RESULTADO FINAL DE LA HEURISTICA:
         * Retornamos la Distancia de Manhattan base, pero le sumamos 2 movimientos 
         * de penalizacion por CADA conflicto encontrado (+1 por salir de la linea 
         * para dejar pasar a la otra pieza, y +1 por volver a entrar).
         */
        return manhattan + (2 * conflicts);
    }

    /* Metodo para la tabla de rendimiento que devuelve {nodosExpandidos, movimientos, costoTotal} */
    public int[] idaStarMetrics(Heuristic heuristicType) {
        nodesExpanded = 0;
        solutionNode = null;
        Node rootNode = new Node(root.getState());
        int threshold = calculateHeuristic(rootNode.getState(), heuristicType);

        while (solutionNode == null) {
            int result = recursiveSearch(rootNode, 0, threshold, heuristicType);
            if (result == -1) {
                /* Extraemos los movimientos y el costo usando NodeUtil */
                int[] metrics = NodeUtil.getSolutionMetrics(solutionNode, root);
                return new int[]{nodesExpanded, metrics[0], metrics[1]};
            }
            if (result == Integer.MAX_VALUE) {
                return new int[]{0, 0, 0};
                /* No se encontro solucion */
            }
            threshold = result;
        }
        return new int[]{0, 0, 0};
    }

}
