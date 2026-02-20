import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        /* Configuración inicial del Scanner y estados del puzzle */
        Scanner input = new Scanner(System.in);
        String configuracionInicial = "5674 8321";
        String configuracionObjetivo = "1238 4765";

        int seleccionUsuario;

        do {
            /* Impresión del menú principal */
            System.out.println("\nBIENVENIDO AL SOLUCIONADOR DE PUZZLE 8");
            System.out.println("Estado inicial : " + configuracionInicial);
            System.out.println("Estado objetivo: " + configuracionObjetivo);
            System.out.println("Selecciona el metodo que deseas ejecutar");
            System.out.println("----------------------");
            System.out.println("1. Busqueda en Anchura (BFS)");
            System.out.println("2. Busqueda en Profundidad (DFS)");
            System.out.println("3. Busqueda por Costo Uniforme (UCS)");
            System.out.println("4. Busqueda con Heurística de Cruz (A*)");
            System.out.println("5. Tabla comparativa de todos");
            System.out.println("6. Salir");
            System.out.print("Elije una opcion: ");

            seleccionUsuario = input.nextInt();

            /* Lógica de navegación del menú */
            if (seleccionUsuario >= 1 && seleccionUsuario <= 4) {
                ejecutarMetodoBusqueda(seleccionUsuario, configuracionInicial, configuracionObjetivo);
            } else if (seleccionUsuario == 5) {
                generarTablaComparativa(configuracionInicial, configuracionObjetivo);
            } else if (seleccionUsuario == 6) {
                System.out.println("Saliendo del programa...");
            } else {
                System.out.println("Opción inválida.");
            }

        } while (seleccionUsuario != 6);

        input.close();
    }

    private static void ejecutarMetodoBusqueda(int opcion, String inicio, String fin) {
        /* Inicialización de las estructuras de datos para la búsqueda */
        Nodo nodoRaiz = new Nodo(inicio);
        Arbol arbolBusqueda = new Arbol(nodoRaiz);
        Nodo nodoResultado = null;

        /* Captura del tiempo inicial en nanosegundos para mayor precisión */
        long tiempoInicio = System.nanoTime();
        switch (opcion) {
            case 1:
                nodoResultado = arbolBusqueda.busquedaxAnchura(fin);
                break;
            case 2:
                nodoResultado = arbolBusqueda.busquedaEnProfunidad(fin);
                break;
            case 3:
                nodoResultado = arbolBusqueda.busquedaCostoUniforme(fin);
                break;
            case 4:
                nodoResultado = arbolBusqueda.busquedaCruz(fin);
                break;
        }

        long tiempoFin = System.nanoTime();
        /* Conversión de nanosegundos a milisegundos con decimales */
        double tiempoTotalMs = (tiempoFin - tiempoInicio) / 1_000_000.0;

        if (nodoResultado != null) {
            System.out.println("\n===== SOLUCION ENCONTRADA =====");
            nodoResultado.imprimirCamino();
            System.out.println("Profundidad (nivel): " + nodoResultado.nivel);
            System.out.printf("Tiempo de ejecucion: %.2f ms\n", tiempoTotalMs);
        } else {
            System.out.println("No se encontro solucion.");
            System.out.printf("Tiempo de ejecucion: %.2f ms\n", tiempoTotalMs);
        }
    }

    private static void generarTablaComparativa(String inicio, String fin) {
        /* Encabezado de la tabla de rendimiento (comparativa) */
        System.out.println("\n=========================== Comparación ===========================");

        String[] etiquetasMetodos = {
                "BFS (Anchura)",
                "DFS (Profundidad)",
                "UCS (Costo Uniforme)",
                "A* (Heurística H2)"
        };

        for (int i = 1; i <= 4; i++) {
            Nodo nodoRaiz = new Nodo(inicio);
            Arbol arbolBusqueda = new Arbol(nodoRaiz);
            Nodo nodoResultado = null;

            long tiempoInicio = System.nanoTime();

            switch (i) {
                case 1: nodoResultado = arbolBusqueda.busquedaxAnchura(fin); break;
                case 2: nodoResultado = arbolBusqueda.busquedaEnProfunidad(fin); break;
                case 3: nodoResultado = arbolBusqueda.busquedaCostoUniforme(fin); break;
                case 4: nodoResultado = arbolBusqueda.busquedaCruz(fin); break;
            }

            long tiempoFin = System.nanoTime();
            double tiempoTotalMs = (tiempoFin - tiempoInicio) / 1_000_000.0;

            /* Impresión de resultados con formato alineado */
            if (nodoResultado != null) {
                System.out.printf("%-20s | Prof: %-3d | Costo: %-5d | Tiempo: %8.2f ms\n",
                        etiquetasMetodos[i - 1],
                        nodoResultado.nivel,
                        nodoResultado.costo,
                        tiempoTotalMs
                );
            } else {
                System.out.printf("%-20s | Sin solución           | Tiempo: %8.2f ms\n",
                        etiquetasMetodos[i - 1],
                        tiempoTotalMs
                );
            }
        }
        System.out.println("=========================================================================\n");
    }
}