
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Nodo implements Comparable<Nodo> {

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

    public LinkedList<Nodo> generarSucesores() {
        LinkedList<Nodo> sucesores = new LinkedList<>();
        int indice = this.estado.indexOf(' ');
        int nuevoNivel = this.nivel + 1;
        switch (indice) {
            case 0:
                sucesores.add(new Nodo(intercambiar(this.estado, 0, 1), nuevoNivel, this));
                sucesores.add(new Nodo(intercambiar(this.estado, 0, 3), nuevoNivel, this));
                break;

            case 1:
                sucesores.add(new Nodo(intercambiar(this.estado, 1, 0), nuevoNivel, this));
                sucesores.add(new Nodo(intercambiar(this.estado, 1, 2), nuevoNivel, this));
                sucesores.add(new Nodo(intercambiar(this.estado, 1, 4), nuevoNivel, this));
                break;

            case 2:
                sucesores.add(new Nodo(intercambiar(this.estado, 2, 1), nuevoNivel, this));
                sucesores.add(new Nodo(intercambiar(this.estado, 2, 5), nuevoNivel, this));
                break;

            case 3:
                sucesores.add(new Nodo(intercambiar(this.estado, 3, 0), nuevoNivel, this));
                sucesores.add(new Nodo(intercambiar(this.estado, 3, 4), nuevoNivel, this));
                sucesores.add(new Nodo(intercambiar(this.estado, 3, 6), nuevoNivel, this));
                break;

            case 4:
                sucesores.add(new Nodo(intercambiar(this.estado, 4, 1), nuevoNivel, this));
                sucesores.add(new Nodo(intercambiar(this.estado, 4, 3), nuevoNivel, this));
                sucesores.add(new Nodo(intercambiar(this.estado, 4, 5), nuevoNivel, this));
                sucesores.add(new Nodo(intercambiar(this.estado, 4, 7), nuevoNivel, this));
                break;

            case 5:
                sucesores.add(new Nodo(intercambiar(this.estado, 5, 2), nuevoNivel, this));
                sucesores.add(new Nodo(intercambiar(this.estado, 5, 4), nuevoNivel, this));
                sucesores.add(new Nodo(intercambiar(this.estado, 5, 8), nuevoNivel, this));
                break;

            case 6:
                sucesores.add(new Nodo(intercambiar(this.estado, 6, 3), nuevoNivel, this));
                sucesores.add(new Nodo(intercambiar(this.estado, 6, 7), nuevoNivel, this));
                break;

            case 7:
                sucesores.add(new Nodo(intercambiar(this.estado, 7, 4), nuevoNivel, this));
                sucesores.add(new Nodo(intercambiar(this.estado, 7, 6), nuevoNivel, this));
                sucesores.add(new Nodo(intercambiar(this.estado, 7, 8), nuevoNivel, this));
                break;

            case 8:
                sucesores.add(new Nodo(intercambiar(this.estado, 8, 5), nuevoNivel, this));
                sucesores.add(new Nodo(intercambiar(this.estado, 8, 7), nuevoNivel, this));
                break;
        }
        return sucesores;
    }

    public void imprimirCamino() {
        List<Nodo> camino = new ArrayList<>();
        Set<Nodo> vistos = new HashSet<>();
        Nodo actual = this;
        while (actual != null) {
            if (vistos.contains(actual)) {
                break;
            }
            vistos.add(actual);
            camino.add(actual);
            actual = actual.padre;
        }
        Collections.reverse(camino);
        for (Nodo n : camino) {
            for (int i = 0; i < 9; i++) {
                System.out.print(n.estado.charAt(i) + " ");
                if ((i + 1) % 3 == 0) {
                    System.out.println();
                }
            }
            System.out.println("Nivel: " + n.nivel
                    + " | Costo: " + n.costo);
            System.out.println("----------------");
        }
    }

    private String intercambiar(String estado, int i, int j) {
        char[] chars = estado.toCharArray();
        char temp = chars[i];
        chars[i] = chars[j];
        chars[j] = temp;
        return new String(chars);
    }

    /* Contamos los errores en una cruz (2,4,5,6 y 8) */
    public int cruzheuristica(String estadoObjetivo) {
        int e = 0;
        int[] posiciones = {2, 4, 5, 6, 8};

        for (int i : posiciones) {
            char actual = this.estado.charAt(i);
            char objetivo = estadoObjetivo.charAt(i);
            /* El ' ' no es un error */
            if (actual != ' ' && actual != objetivo) {
                e++;
            }
        }
        return e;
    }

    @Override
    public int compareTo(Nodo otro) {
        return Integer.compare(this.costo, otro.costo);
    }
}
