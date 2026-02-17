public class App {
    public static void main(String[] args) throws Exception {
        Nodo raiz = new Nodo("1238 4765");
        Arbol puzzle = new Arbol(raiz);
        Nodo n = puzzle.busquedaEnProfunidad("1284376 5");
        System.out.println(n.estado);
        System.out.println(n.nivel);
        //n.imprimirCamino();
    }
}
