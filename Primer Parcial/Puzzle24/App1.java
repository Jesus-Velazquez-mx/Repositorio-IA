import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Clase principal para la ejecucion del resolvedor de 24-Puzzle (5x5). 
 * * JUSTIFICACION DEL ALGORITMO (Gestion de Memoria):
 * Debido al gran número de combinaciones de un tablero de 5x5 (1.55 x 10^25 permutaciones), 
 * se requiere el uso del algoritmo IDA*. 
 * * ¿Por que IDA* es superior a A* en este espacio de estados?
 * El algoritmo A* tradicional guarda en la memoria RAM todos los nodos que genera y 
 * explora. Su complejidad espacial es exponencial O(b^d), 
 * lo que significa que en un tablero de 5x5 llenaría la memoria del sistema (Out of Memory) 
 * en cuestion de segundos.
 * * Por su parte, IDA* (Iterative Deepening A*) realiza busquedas en profundidad limitadas 
 * por un presupuesto (threshold). Al hacerlo, solo necesita mantener en memoria la ruta 
 * actual que esta explorando. Esto reduce su complejidad a un nivel 
 * lineal O(bd), garantizando encontrar la ruta optima sin consumir todos los recursos de la maquina.
*/

public class App1 {

    /* Estado objetivo: orden ascendente con el espacio vacio (0) al final */
    final static private String GOAL_STATE = "123456789ABCDEFGHIJKLMNO0";

    /* Estados iniciales predefinidos con diferentes niveles de profundidad */
    final static private String EASY_STATE = "123456789ABCDEFGHIJKLM0NO";
    final static private String MEDIUM_STATE = "123456789ABCD0FGHIEKLMNJO";
    final static private String HARD_STATE = "123456089AB7DEFGCIJKLHMNO";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int selector = 0;

        /* Se inicializa con el estado facil por defecto */
        String rootState = EASY_STATE;

        System.out.println("--- INICIANDO AGENTE INTELIGENTE ---");
        System.out.println("Estado inicial actual: " + rootState);

        while (selector != 7) {
            System.out.println("\n*****************************************");
            System.out.println("       RESOLVEDOR DE 24-PUZZLE (5x5)     ");
            System.out.println("*****************************************");
            System.out.println("Estado objetivo (Meta): " + GOAL_STATE);
            System.out.println("Estado inicial:         " + rootState);
            System.out.println("-----------------------------------------");
            System.out.println("1. Ingresar estado inicial manualmente");
            System.out.println("2. Elegir estado inicial predefinido (Facil, Medio, Dificil)");
            System.out.println("3. Generar estado inicial aleatorio");
            System.out.println("4. Resolver con IDA* - Distancia de Manhattan");
            System.out.println("5. Resolver con IDA* - Conflicto Lineal");
            System.out.println("6. Ver tabla de rendimiento");
            System.out.println("7. Salir");
            System.out.print("\nSeleccione una opcion: ");

            /* Validacion de entrada para evitar errores con caracteres no numericos */
            if (scanner.hasNextInt()) {
                selector = scanner.nextInt();
            } else {
                scanner.next();
                /* Limpiar el buffer */
                System.out.println("Por favor, ingrese un numero del 1 al 9.");
                continue;
            }

            /* MENU DE OPERACIONES */
            switch (selector) {
                case 1:
                    System.out.println("Ingrese la cadena de 25 caracteres (1-9, A-O, y 0):");
                    rootState = scanner.next();
                    if (rootState.length() != 25) {
                        System.out.println("Error: La cadena debe tener exactamente 25 caracteres.");
                        rootState = EASY_STATE;
                    }
                    break;

                case 2:
                    System.out.println("Seleccione la dificultad:");
                    System.out.println("1. Facil");
                    System.out.println("2. Medio");
                    System.out.println("3. Dificil");
                    System.out.print("Opcion: ");
                    if (scanner.hasNextInt()) {
                        int diff = scanner.nextInt();
                        switch (diff) {
                            case 1:
                                rootState = EASY_STATE;
                                break;
                            case 2:
                                rootState = MEDIUM_STATE;
                                break;
                            case 3:
                                rootState = HARD_STATE;
                                break;
                            default:
                                System.out.println("Opcion invalida, manteniendo el estado actual.");
                                break;
                        }
                    } else {
                        scanner.next();
                        System.out.println("Entrada invalida.");
                    }
                    System.out.println("Estado actualizado a: " + rootState);
                    break;

                case 3:
                    rootState = generateSolvableState(GOAL_STATE);
                    System.out.println("Nuevo estado aleatorio generado: " + rootState);
                    break;

                case 4:
                    long startTime3 = System.currentTimeMillis();
                    SearchTree search3 = new SearchTree(new Node(rootState), GOAL_STATE);
                    System.out.println("Buscando solucion con Manhattan... por favor espere.");
                    search3.idaStar(Heuristic.H_TWO);
                    long totalTime3 = System.currentTimeMillis() - startTime3;
                    System.out.println("\nTiempo de ejecucion: " + totalTime3 + " ms");
                    break;

                case 5:
                    long startTime4 = System.currentTimeMillis();
                    SearchTree search4 = new SearchTree(new Node(rootState), GOAL_STATE);
                    System.out.println("Buscando solucion con Conflicto Lineal... por favor espere.");
                    search4.idaStar(Heuristic.H_THREE);
                    long totalTime4 = System.currentTimeMillis() - startTime4;
                    System.out.println("\nTiempo de ejecucion: " + totalTime4 + " ms");
                    break;

                case 6:
                    /* Ejecucion de ambas heuristicas para la tabla comparativa */
                    System.out.println("\nEjecutando algoritmos para la tabla de rendimiento. Por favor espere...");

                    /* Distancia de Manhattan */
                    long t1Start = System.currentTimeMillis();
                    SearchTree st1 = new SearchTree(new Node(rootState), GOAL_STATE);
                    /* Llama al nuevo metodo que devuelve el arreglo de datos */
                    int[] metrics1 = st1.idaStarMetrics(Heuristic.H_TWO);
                    long t1 = System.currentTimeMillis() - t1Start;

                    /* Conflicto Lineal */
                    long t2Start = System.currentTimeMillis();
                    SearchTree st2 = new SearchTree(new Node(rootState), GOAL_STATE);
                    /* Llama al nuevo metodo que devuelve el arreglo de datos */
                    int[] metrics2 = st2.idaStarMetrics(Heuristic.H_THREE);
                    long t2 = System.currentTimeMillis() - t2Start;

                    /* Impresion de la tabla formateada */
                    System.out.println("\n================================ TABLA DE RENDIMIENTO ================================");
                    System.out.printf("%-20s | %-12s | %-16s | %-12s | %-12s\n", "Heuristica", "Tiempo (ms)", "Nodos Expandidos", "Movimientos", "Costo Total");
                    System.out.println("--------------------------------------------------------------------------------------");
                    System.out.printf("%-20s | %-12d | %-16d | %-12d | %-12d\n", "Dist. Manhattan", t1, metrics1[0], metrics1[1], metrics1[2]);
                    System.out.printf("%-20s | %-12d | %-16d | %-12d | %-12d\n", "Conflicto Lineal", t2, metrics2[0], metrics2[1], metrics2[2]);
                    System.out.println("======================================================================================\n");
                    break;

                case 7:
                    System.out.println("Nos vemos! Gracias por usar el resolvedor de 24-Puzzle.");
                    break;

                default:
                    System.out.println("Opcion no valida. Elija un numero del 1 al 7.");
                    break;
            }
        }
        scanner.close();
    }

    /* Metodo para generar un estado aleatorio con dificultad controlada */
    private static String generateSolvableState(String goal) {
        /* Podemos ajustar este valor para generar estados más o menos difíciles. Si se pone muy alto, los estados generados serán mas complejos y costosos */
        /* Aquí lo manejamos como un random de 3-8 (cantidad de numeros posibles) */
        int numRandomMoves = (int)(Math.random() * 6) + 3;
        String currentState = goal;
        String previousState = "";

        java.util.Random rand = new java.util.Random();

        for (int i = 0; i < numRandomMoves; i++) {
            /* Obtenemos los movimientos validos desde la posicion actual usando NodeUtil */
            List<String> successors = NodeUtil.getSuccessors(currentState);

            /* Filtramos para no deshacer el movimiento que acabamos de hacer (A -> B -> A) */
            List<String> validMoves = new ArrayList<>();
            for (String s : successors) {
                if (!s.equals(previousState)) {
                    validMoves.add(s);
                }
            }

            /* Respaldo por si se queda sin opciones (muy raro en el puzzle, pero seguro) */
            if (validMoves.isEmpty()) {
                validMoves = successors;
            }

            /* Elegimos un sucesor aleatorio y actualizamos */
            previousState = currentState;
            currentState = validMoves.get(rand.nextInt(validMoves.size()));
        }

        return currentState;
    }
}
