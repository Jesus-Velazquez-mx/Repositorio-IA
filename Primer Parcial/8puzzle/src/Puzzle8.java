public class Puzzle8 {
    String estadoInicial = "1238 4765";
    String estadoFinal = "12 843765 ";

    String [] generarSucesores (String estadoActual){
        /* Dependiendo de en que índice se encuentre el espacio vacío,
        se generaran n número de sucesores */

        /* Aquí se busca la posición del vacío */
        int indice = estadoActual.indexOf(' ');
        String[] hijos = new String[4];

        /* Contador de hijos (child) */
        int c = 0;

        switch (indice) {
            case 0:
                // Puede ir a 1 (derecha) y 3 (abajo)
                hijos[c++] = intercambiar(estadoActual, 0, 1);
                hijos[c++] = intercambiar(estadoActual, 0, 3);
                break;

            case 1:
                // Puede ir a 0 (izq), 2 (der), 4 (abajo)
                hijos[c++] = intercambiar(estadoActual, 1, 0);
                hijos[c++] = intercambiar(estadoActual, 1, 2);
                hijos[c++] = intercambiar(estadoActual, 1, 4);
                break;

            case 2:
                // Puede ir a 1 (izq) y 5 (abajo)
                hijos[c++] = intercambiar(estadoActual, 2, 1);
                hijos[c++] = intercambiar(estadoActual, 2, 5);
                break;

            case 3:
                // Puede ir a 0 (arriba), 4 (der), 6 (abajo)
                hijos[c++] = intercambiar(estadoActual, 3, 0);
                hijos[c++] = intercambiar(estadoActual, 3, 4);
                hijos[c++] = intercambiar(estadoActual, 3, 6);
                break;

            case 4:
                // Puede ir a 1 (arriba), 3 (izq), 5 (der), 7 (abajo)
                hijos[c++] = intercambiar(estadoActual, 4, 1);
                hijos[c++] = intercambiar(estadoActual, 4, 3);
                hijos[c++] = intercambiar(estadoActual, 4, 5);
                hijos[c++] = intercambiar(estadoActual, 4, 7);
                break;

            case 5:
                // Puede ir a 2 (arriba), 4 (izq), 8 (abajo)
                hijos[c++] = intercambiar(estadoActual, 5, 2);
                hijos[c++] = intercambiar(estadoActual, 5, 4);
                hijos[c++] = intercambiar(estadoActual, 5, 8);
                break;

            case 6:
                // Puede ir a 3 (arriba) y 7 (der)
                hijos[c++] = intercambiar(estadoActual, 6, 3);
                hijos[c++] = intercambiar(estadoActual, 6, 7);
                break;

            case 7:
                // Puede ir a 4 (arriba), 6 (izq), 8 (der)
                hijos[c++] = intercambiar(estadoActual, 7, 4);
                hijos[c++] = intercambiar(estadoActual, 7, 6);
                hijos[c++] = intercambiar(estadoActual, 7, 8);
                break;

            case 8:
                // Puede ir a 5 (arriba) y 7 (izq)
                hijos[c++] = intercambiar(estadoActual, 8, 5);
                hijos[c++] = intercambiar(estadoActual, 8, 7);
                break;

            default:
                break;
        }
        return hijos;
    }

    private String intercambiar(String estado, int i, int j) {
    char a = estado.charAt(i);
    char b = estado.charAt(j);
    String resultado = "";
    for (int k = 0; k < estado.length(); k++) {
        if (k == i) {
            resultado += b;
        } else if (k == j) {
            resultado += a;
        } else {
            resultado += estado.charAt(k);
        }
    }
    return resultado;
}

    void imprimirEstado(String e) {
        System.out.println(e.substring(0, 3));
        System.out.println(e.substring(3, 6));
        System.out.println(e.substring(6, 9));
        System.out.println("-----");
    }

}
