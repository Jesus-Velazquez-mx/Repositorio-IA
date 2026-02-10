public class App {
    public static void main(String[] args) throws Exception {
        Puzzle8 puzzle =  new Puzzle8();
        puzzle.generarSucesores("1238 4765");
        puzzle.imprimirEstado("1238 4765");
    }
}
