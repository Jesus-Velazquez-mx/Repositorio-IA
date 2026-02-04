package tree;

public class App {
	/* Pruebas para verificar el funcionamiento del árbol */
	public static void main(String[] args) {
		Arbol arbol = new Arbol();
		arbol.insertar(8);
		arbol.insertar(3);
		arbol.insertar(10);
		arbol.insertar(1);
		arbol.insertar(6);
		arbol.insertar(14);
		arbol.insertar(4);
		arbol.insertar(7);
		arbol.insertar(13);
		arbol.imprimirArbol();
		int valorBuscar = 6;
		Nodo resultado = arbol.buscarNodo(valorBuscar);
		if (resultado != null) {
			System.out.println("\nEl valor " + valorBuscar + " SI existe en el árbol");
		} else {
			System.out.println("\nEl valor " + valorBuscar + " NO existe en el árbol");
		}
		System.out.println(arbol.vacio());
	}
}
