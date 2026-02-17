import java.util.LinkedList;

public class Nodo implements Comparable <Nodo> {

    String estado;
    int nivel;
    int costo;
    Nodo padre;

    public Nodo(String estado) {
        this.estado = estado;
        this.nivel = 0;
        this.costo = 0;
        this.padre = null;
    }

    public Nodo(String estado, int nivel) {
        this.estado = estado;
        this.nivel = nivel;
        this.costo = 0;
        this.padre = null;
    }

    public Nodo(String estado, int nivel, Nodo padre) {
        this.estado = estado;
        this.nivel = nivel;
        this.costo = 0;
        this.padre = padre;
    }

    public Nodo(String estado, int nivel, int costo, Nodo padre) {
        this.estado = estado;
        this.nivel = nivel;
        this.costo = costo;
        this.padre = padre;
    }

    LinkedList<Nodo> generarSucesores() {
        LinkedList<Nodo> sucesores = new LinkedList<>();
        int indice = this.estado.indexOf(' ');
        int nuevoNivel = this.nivel + 1;

        switch (indice) {
            case 0:
                // Puede ir a 1 (derecha) y 3 (abajo)
                sucesores.add(new Nodo(intercambiar(this.estado, 0, 1), nuevoNivel, this.costo + 1, this));
                sucesores.add(new Nodo(intercambiar(this.estado, 0, 3), nuevoNivel, this.costo + 1, this));
                break;
            case 1:
                // Puede ir a 0 (izq), 2 (der), 4 (abajo)
                sucesores.add(new Nodo(intercambiar(this.estado, 1, 0), nuevoNivel, this.costo + 1, this));
                sucesores.add(new Nodo(intercambiar(this.estado, 1, 2), nuevoNivel, this.costo + 1, this));
                sucesores.add(new Nodo(intercambiar(this.estado, 1, 4), nuevoNivel, this.costo + 1, this));
                break;
            case 2:
                // Puede ir a 1 (izq) y 5 (abajo)
                sucesores.add(new Nodo(intercambiar(this.estado, 2, 1), nuevoNivel, this.costo + 1, this));
                sucesores.add(new Nodo(intercambiar(this.estado, 2, 5), nuevoNivel, this.costo + 1, this));
                break;
            case 3:
                // Puede ir a 0 (arriba), 4 (der), 6 (abajo)
                sucesores.add(new Nodo(intercambiar(this.estado, 3, 0), nuevoNivel, this.costo + 1, this));
                sucesores.add(new Nodo(intercambiar(this.estado, 3, 4), nuevoNivel, this.costo + 1, this));
                sucesores.add(new Nodo(intercambiar(this.estado, 3, 6), nuevoNivel, this.costo + 1, this));
                break;
            case 4:
                // Puede ir a 1 (arriba), 3 (izq), 5 (der), 7 (abajo)
                sucesores.add(new Nodo(intercambiar(this.estado, 4, 1), nuevoNivel, this.costo + 1, this));
                sucesores.add(new Nodo(intercambiar(this.estado, 4, 3), nuevoNivel, this.costo + 1, this));
                sucesores.add(new Nodo(intercambiar(this.estado, 4, 5), nuevoNivel, this.costo + 1, this));
                sucesores.add(new Nodo(intercambiar(this.estado, 4, 7), nuevoNivel, this.costo + 1, this));
                break;
            case 5:
                // Puede ir a 2 (arriba), 4 (izq), 8 (abajo)
                sucesores.add(new Nodo(intercambiar(this.estado, 5, 2), nuevoNivel, this.costo + 1, this));
                sucesores.add(new Nodo(intercambiar(this.estado, 5, 4), nuevoNivel, this.costo + 1, this));
                sucesores.add(new Nodo(intercambiar(this.estado, 5, 8), nuevoNivel, this.costo + 1, this));
                break;
            case 6:
                // Puede ir a 3 (arriba) y 7 (der)
                sucesores.add(new Nodo(intercambiar(this.estado, 6, 3), nuevoNivel, this.costo + 1, this));
                sucesores.add(new Nodo(intercambiar(this.estado, 6, 7), nuevoNivel, this.costo + 1, this));
                break;
            case 7:
                // Puede ir a 4 (arriba), 6 (izq), 8 (der)
                sucesores.add(new Nodo(intercambiar(this.estado, 7, 4), nuevoNivel, this.costo + 1, this));
                sucesores.add(new Nodo(intercambiar(this.estado, 7, 6), nuevoNivel, this.costo + 1, this));
                sucesores.add(new Nodo(intercambiar(this.estado, 7, 8), nuevoNivel, this.costo + 1, this));
                break;
            case 8:
                // Puede ir a 5 (arriba) y 7 (izq)
                sucesores.add(new Nodo(intercambiar(this.estado, 8, 5), nuevoNivel, this.costo + 1, this));
                sucesores.add(new Nodo(intercambiar(this.estado, 8, 7), nuevoNivel, this.costo + 1, this));
                break;
        }
        return sucesores;
    }

    public void imprimirCamino() {
        if (this.padre != null) {
            this.padre.imprimirCamino();
        }
        for (int i = 0; i < 9; i++) {
            System.out.print(this.estado.charAt(i) + " ");
            if ((i + 1) % 3 == 0) {
                System.out.println();
            }
        }
        System.out.println("Nivel: " + this.nivel);
        System.out.println("----------------");
    }

    private String intercambiar(String estado, int i, int j) {
        char[] chars = estado.toCharArray();
        char temp = chars[i];
        chars[i] = chars[j];
        chars[j] = temp;
        return new String(chars);
    }

    @Override
    public int compareTo(Nodo otro) {
        return this.costo == otro.costo ? 0 : this.costo > otro.costo ? 1 : -1;
    }
}