import java.util.LinkedList;

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

    LinkedList<Nodo> generarSucesores() {
        LinkedList<Nodo> sucesores = new LinkedList<>();
        int indiceVacio = this.estado.indexOf(' ');
        int nuevoNivel = this.nivel + 1;

        switch (indiceVacio) {
            case 0:
                // Puede ir a 1 (derecha) y 3 (abajo)
                sucesores.add(crearHijo(0, 1, nuevoNivel));
                sucesores.add(crearHijo(0, 3, nuevoNivel));
                break;
            case 1:
                // Puede ir a 0 (izq), 2 (der), 4 (abajo)
                sucesores.add(crearHijo(1, 0, nuevoNivel));
                sucesores.add(crearHijo(1, 2, nuevoNivel));
                sucesores.add(crearHijo(1, 4, nuevoNivel));
                break;
            case 2:
                // Puede ir a 1 (izq) y 5 (abajo)
                sucesores.add(crearHijo(2, 1, nuevoNivel));
                sucesores.add(crearHijo(2, 5, nuevoNivel));
                break;
            case 3:
                // Puede ir a 0 (arriba), 4 (der), 6 (abajo)
                sucesores.add(crearHijo(3, 0, nuevoNivel));
                sucesores.add(crearHijo(3, 4, nuevoNivel));
                sucesores.add(crearHijo(3, 6, nuevoNivel));
                break;
            case 4:
                // Puede ir a 1 (arriba), 3 (izq), 5 (der), 7 (abajo)
                sucesores.add(crearHijo(4, 1, nuevoNivel));
                sucesores.add(crearHijo(4, 3, nuevoNivel));
                sucesores.add(crearHijo(4, 5, nuevoNivel));
                sucesores.add(crearHijo(4, 7, nuevoNivel));
                break;
            case 5:
                // Puede ir a 2 (arriba), 4 (izq), 8 (abajo)
                sucesores.add(crearHijo(5, 2, nuevoNivel));
                sucesores.add(crearHijo(5, 4, nuevoNivel));
                sucesores.add(crearHijo(5, 8, nuevoNivel));
                break;
            case 6:
                // Puede ir a 3 (arriba) y 7 (der)
                sucesores.add(crearHijo(6, 3, nuevoNivel));
                sucesores.add(crearHijo(6, 7, nuevoNivel));
                break;
            case 7:
                // Puede ir a 4 (arriba), 6 (izq), 8 (der)
                sucesores.add(crearHijo(7, 4, nuevoNivel));
                sucesores.add(crearHijo(7, 6, nuevoNivel));
                sucesores.add(crearHijo(7, 8, nuevoNivel));
                break;
            case 8:
                // Puede ir a 5 (arriba) y 7 (izq)
                sucesores.add(crearHijo(8, 5, nuevoNivel));
                sucesores.add(crearHijo(8, 7, nuevoNivel));
                break;
        }
        return sucesores;
    }

    /* Método auxiliar para calcular el costo y crear el nuevo nodo */
    private Nodo crearHijo(int indiceVacio, int indiceDestino, int nuevoNivel) {
        // Extraemos el valor numérico de la ficha que se va a mover al espacio vacío
        int valorFicha = Character.getNumericValue(this.estado.charAt(indiceDestino));
        
        // El nuevo costo es el acumulado del padre + el valor de la ficha movida
        int nuevoCosto = this.costo + valorFicha;
        
        // Intercambiamos la posición en el string
        String nuevoEstado = intercambiar(this.estado, indiceVacio, indiceDestino);
        
        return new Nodo(nuevoEstado, nuevoNivel, nuevoCosto, this);
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
        System.out.println("Costo acumulado: " + this.costo);
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