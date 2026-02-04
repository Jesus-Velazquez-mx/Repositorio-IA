package tree;

public class Arbol {
	/* Raíz del Árbol */
	private Nodo raiz;

	public Arbol() {
		this.raiz = null;
	}

	public Nodo getRaiz() {
		return raiz;
	}

	/* Árbol vacío */
	public boolean vacio() {
		return raiz == null;
	}
	
	/* Buscar (Recursivo - Prefijo/Preorden) */
	public Nodo buscarNodo(int valor) {
	    return buscarPreorden(raiz, valor);
	}

	private Nodo buscarPreorden(Nodo actual, int valor) {
	    if (actual == null) {
	        return null;
	    }
	    if (actual.getValor() == valor) {
	        return actual;
	    }
	    Nodo encontrado = buscarPreorden(actual.getIzquierda(), valor);
	    if (encontrado != null) {
	        return encontrado;
	    }
	    return buscarPreorden(actual.getDerecha(), valor);
	}

	
	/* Insetar (Recursivo) */
	public void insertar(int valor) {
		if (raiz == null) {
			raiz = new Nodo(valor);
		} else {
			insertarRecursivo(raiz, valor);
		}
	}
	
	private void insertarRecursivo(Nodo actual, int valor) {
		if (valor < actual.getValor()) {
			if (actual.getIzquierda() == null) {
				actual.setIzquierda(new Nodo(valor));
			} else {
				insertarRecursivo(actual.getIzquierda(), valor);
			}
		} else 
			if (valor > actual.getValor()) {
				if (actual.getDerecha() == null) {
					actual.setDerecha(new Nodo(valor));
				} else {
					insertarRecursivo(actual.getDerecha(), valor);
				}
			}
	}
	
	/* Imprimir el árbol con distintos recorridos */
	public void imprimirArbol() {
	    System.out.print("Infijo:   ");
		imprimirInfijoRecursivo(raiz);
	    System.out.println();

	    System.out.print("Prefijo:  ");
		imprimirPrefijoRecursivo(raiz);
	    System.out.println();

	    System.out.print("Postfijo: ");
		imprimirPostfijoRecursivo(raiz);
	    System.out.println();
	}

	/* Imprimir Infijo recursivo (Izq - Raíz - Der) */
	private void imprimirInfijoRecursivo(Nodo raiz) {
		if (raiz == null) {
			return;
		}
		imprimirInfijoRecursivo(raiz.getIzquierda());
		System.out.print(raiz.getValor() + " ");
		imprimirInfijoRecursivo(raiz.getDerecha());
	}

	/* Imprimir Prefijo recursivo (Raíz - Izq - Der)*/
	private void imprimirPrefijoRecursivo(Nodo raiz) {
		if (raiz == null) {
			return;
		}
		System.out.print(raiz.getValor() + " ");
		imprimirPrefijoRecursivo(raiz.getIzquierda());
		imprimirPrefijoRecursivo(raiz.getDerecha());
	}

	/* Imprimir Postfijo recursivo (Izq - Der - Raíz) */
	private void imprimirPostfijoRecursivo(Nodo raiz) {
		if (raiz == null) {
			return;
		}
		imprimirPostfijoRecursivo(raiz.getIzquierda());
		imprimirPostfijoRecursivo(raiz.getDerecha());
		System.out.print(raiz.getValor() + " ");
	}

}
