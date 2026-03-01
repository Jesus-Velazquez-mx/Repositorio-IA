import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class NodeUtil {

    /**
     * Genera los estados sucesores de un estado dado, considerando los posibles
     * movimientos del espacio vacío (0) en un tablero de 5x5. Cada movimiento
     * implica intercambiar el espacio vacío con una pieza adyacente.
     */
    public static List<String> getSuccessors(String state) {
        List<String> successors = new ArrayList<>();
        int zeroPos = state.indexOf("0");
        /* Mapa de adyacencia para un tablero 5x5 (25 posiciones) */
        int[][] adjacentPositions = {
            {1, 5}, // pos 0: derecha, abajo
            {0, 2, 6}, // pos 1: izq, der, abajo
            {1, 3, 7}, // pos 2: izq, der, abajo
            {2, 4, 8}, // pos 3: izq, der, abajo
            {3, 9}, // pos 4: izq, abajo
            {0, 6, 10}, // pos 5: arriba, der, abajo
            {1, 5, 7, 11}, // pos 6: arriba, izq, der, abajo
            {2, 6, 8, 12}, // pos 7: arriba, izq, der, abajo
            {3, 7, 9, 13}, // pos 8: arriba, izq, der, abajo
            {4, 8, 14}, // pos 9: arriba, izq, abajo
            {5, 11, 15}, // pos 10: arriba, der, abajo
            {6, 10, 12, 16}, // pos 11: arriba, izq, der, abajo
            {7, 11, 13, 17}, // pos 12: arriba, izq, der, abajo
            {8, 12, 14, 18}, // pos 13: arriba, izq, der, abajo
            {9, 13, 19}, // pos 14: arriba, izq, abajo
            {10, 16, 20}, // pos 15: arriba, der, abajo
            {11, 15, 17, 21}, // pos 16: arriba, izq, der, abajo
            {12, 16, 18, 22}, // pos 17: arriba, izq, der, abajo
            {13, 17, 19, 23}, // pos 18: arriba, izq, der, abajo
            {14, 18, 24}, // pos 19: arriba, izq, abajo
            {15, 21}, // pos 20: arriba, der
            {16, 20, 22}, // pos 21: arriba, izq, der
            {17, 21, 23}, // pos 22: arriba, izq, der
            {18, 22, 24}, // pos 23: arriba, izq, der
            {19, 23} // pos 24: arriba, izq
        };

        for (int adjPos : adjacentPositions[zeroPos]) {
            successors.add(swapPositions(state, zeroPos, adjPos));
        }

        return successors;
    }

    /* Para intercambiar las posiciones */
    private static String swapPositions(String state, int pos1, int pos2) {
        char[] arr = state.toCharArray();
        char temp = arr[pos1];
        arr[pos1] = arr[pos2];
        arr[pos2] = temp;
        return new String(arr);
    }

    /* Para imprimir la solución */
    public static void printSolution(Node goalNode, Set<String> visitedNodes, Node root, int time) {
        int totalCost = 0;
        Stack<Node> solutionStack = new Stack<Node>();
        solutionStack.push(goalNode);

        while (!goalNode.getState().equals(root.getState())) {
            solutionStack.push(goalNode.getParent());
            goalNode = goalNode.getParent();
        }

        String sourceState = root.getState();
        String destinationState;

        for (int i = solutionStack.size() - 1; i >= 0; i--) {
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            destinationState = solutionStack.get(i).getState();

            if (!sourceState.equals(destinationState)) {
                System.out.println("Move " + destinationState.charAt(sourceState.indexOf('0'))
                        + " " + findTransition(sourceState, destinationState));
                /* Sumamos el valor numérico de la pieza movida al costo total */
                totalCost += Character.getNumericValue(destinationState.charAt(sourceState.indexOf('0')));
            }

            System.out.println("*******");
            /* Visualización de 5x5 */
            for (int j = 0; j < 25; j += 5) {
                System.out.println("* " + destinationState.substring(j, j + 5) + " *");
            }
            System.out.println("*******");
            sourceState = destinationState;
        }

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println("** Movimientos realizados: " + (solutionStack.size() - 1));
        System.out.println("** Nodos expandidos: " + time);
        System.out.println("** Costo total de la solucion: " + totalCost);
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    }

    public static MovementType findTransition(String source, String destination) {
        int zeroDiff = destination.indexOf('0') - source.indexOf('0');
        /* Se ha ajustado para 5 posiciones */
        switch (zeroDiff) {
            case -5:
                return MovementType.DOWN; // El espacio vacío subió, la pieza bajó
            case 5:
                return MovementType.UP;   // El espacio vacío bajó, la pieza subió
            case 1:
                return MovementType.LEFT; // El espacio vacío fue a la derecha, la pieza a la izquierda
            case -1:
                return MovementType.RIGHT;// El espacio vacío fue a la izquierda, la pieza a la derecha
        }
        return null;
    }

    /* Metodo silencioso que solo devuelve las metricas: {cantidad de movimientos, costo total} */
    public static int[] getSolutionMetrics(Node goalNode, Node root) {
        int totalCost = 0;
        Stack<Node> stack = new Stack<>();
        stack.push(goalNode);
        
        /* Rastreamos el camino de regreso a la raiz */
        while (!goalNode.getState().equals(root.getState())) {
            stack.push(goalNode.getParent());
            goalNode = goalNode.getParent();
        }
        
        String source = root.getState();
        for (int i = stack.size() - 1; i >= 0; i--) {
            String dest = stack.get(i).getState();
            if (!source.equals(dest)) {
                /* Calculamos el costo del movimiento */
                totalCost += Character.getNumericValue(dest.charAt(source.indexOf('0')));
            }
            source = dest;
        }
        /* Retorna [Movimientos, Costo Total] */
        return new int[] { stack.size() - 1, totalCost };
    }
}
