public class Puzzle8 {
    String estadoInicial = "1238 4765";
    String estadoFinal = "12 843765 ";

    void imprimirEstado(String e) {
        System.out.println(e.substring(0, 3));
        System.out.println(e.substring(3, 6));
        System.out.println(e.substring(6, 9));
        System.out.println("-----");
    }

}
