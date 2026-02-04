package tree;

public class Nodo {
	// Valor/nombre del nodo
	int nombre;
	// Hijos
	Nodo izquierda;
	Nodo derecha;
	
	public Nodo(int nombre) {
		this.nombre = nombre;
		this.izquierda = null;
		this.derecha = null;
	}

	/* Getters y Setters*/
	public int getValor() {
		return nombre;
	}

	public void setValor(int valor) {
		this.nombre = valor;
	}

	public Nodo getIzquierda() {
		return izquierda;
	}

	public void setIzquierda(Nodo izquierda) {
		this.izquierda = izquierda;
	}

	public Nodo getDerecha() {
		return derecha;
	}

	public void setDerecha(Nodo derecha) {
		this.derecha = derecha;
	}
	
	
	
}
