public class App {
    public static void main(String[] args) throws Exception {
        // Estado inicial y objetivo
        String estadoInicial = "1238 4765";
        String estadoObjetivo = "1284376 5";
        
        Nodo raiz = new Nodo(estadoInicial);
        Arbol puzzle = new Arbol(raiz);

        // --- Medir Búsqueda por Anchura ---
        System.out.println("--- Ejecutando Búsqueda por Anchura ---");
        long inicioAnchura = System.nanoTime();
        Nodo nAnchura = puzzle.busquedaxAnchura(estadoObjetivo);
        long finAnchura = System.nanoTime();
        imprimirResultados(nAnchura, inicioAnchura, finAnchura);

        // --- Medir Búsqueda por Costo Uniforme ---
        System.out.println("\n--- Ejecutando Búsqueda por Costo Uniforme ---");
        long inicioCosto = System.nanoTime();
        Nodo nCosto = puzzle.busquedaCostoUniforme(estadoObjetivo);
        long finCosto = System.nanoTime();
        imprimirResultados(nCosto, inicioCosto, finCosto);

        // --- Medir Búsqueda en Profundidad ---
        // NOTA: La profundidad puede tardar mucho o entrar en bucles infinitos 
        // si no tiene límite, úsalo con precaución.
        System.out.println("\n--- Ejecutando Búsqueda en Profundidad ---");
        long inicioProfun = System.nanoTime();
        Nodo nProfun = puzzle.busquedaEnProfunidad(estadoObjetivo);
        long finProfun = System.nanoTime();
        imprimirResultados(nProfun, inicioProfun, finProfun);
    }

    private static void imprimirResultados(Nodo n, long inicio, long fin) {
        if (n != null) {
            double tiempoMilis = (fin - inicio) / 1e6; // Convertir nanosegundos a milisegundos
            System.out.println("Estado encontrado: " + n.estado);
            System.out.println("Nivel (Profundidad): " + n.nivel);
            System.out.println("Costo total: " + n.costo);
            System.out.println("Tiempo de ejecución: " + tiempoMilis + " ms");
        } else {
            System.out.println("No se encontró solución.");
        }
    }
}