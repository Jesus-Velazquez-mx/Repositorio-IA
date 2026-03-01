import java.util.*;

public class Arbol {

    Nodo raiz;

    public Arbol(Nodo raiz) {
        this.raiz = raiz;
    }

    Nodo busquedaxAnchura(String estadoObjetivo) {
        if (raiz == null) {
            return null;
        }
        int expandidos = 0;
        /* Guardamos los nodos visitados */
        HashSet<String> visitados = new HashSet<>();
        /* Lista de estados a visitar */
        Queue<Nodo> cola = new LinkedList<>();
        /* Inicio */
        cola.add(raiz);
        visitados.add(raiz.estado);

        while (!cola.isEmpty()) {
            /* Lo sacamos de la lista y lo guardamos en "actual" */
            Nodo actual = cola.poll();
            expandidos++;
            /* Si es el estado objetivo, lo devolvemos */
            if (actual.estado.equals(estadoObjetivo)) {
                actual.nodosExpandidos = expandidos;
                return actual;
            }
            /* Genereamos los sucesores y los agregamos a la lista */
            List<Nodo> sucesores = actual.generarSucesores();
            for (Nodo hijo : sucesores) {
                if (!visitados.contains(hijo.estado)) {
                    visitados.add(hijo.estado);
                    cola.add(hijo);
                }
            }
        }
        return null;
    }

    Nodo busquedaEnProfunidad(String estadoObjetivo) {
        if (raiz == null) {
            return null;
        }
        int expandidos = 0;
        /* Guardamos los nodos visitados */
        HashSet<String> visitados = new HashSet<>();
        /* Pila de estados a visitar */
        Stack<Nodo> pila = new Stack<>();
        /* Inicio */
        pila.push(raiz);
        visitados.add(raiz.estado);

        while (!pila.isEmpty()) {
            /* Lo sacamos de la pila y lo guardamos en "actual" */
            Nodo actual = pila.pop();
            expandidos++;
            /* Si es el estado objetivo, lo devolvemos */
            if (actual.estado.equals(estadoObjetivo)) {
                actual.nodosExpandidos = expandidos;
                return actual;
            }
            /* Genereamos los sucesores y los agregamos a la pila */
            List<Nodo> sucesores = actual.generarSucesores();
            for (Nodo hijo : sucesores) {
                if (!visitados.contains(hijo.estado)) {
                    visitados.add(hijo.estado);
                    pila.push(hijo);
                }
            }
        }
        return null;
    }

    Nodo busquedaCostoUniforme(String estadoObjetivo) {
        if (raiz == null) {
            return null;
        }
        int expandidos = 0;
        /* Guardamos los nodos visitados */
        HashSet<String> visitados = new HashSet<>();
        /* La PriorityQueue usará el compareTo de Nodo, que compara n.costo */
        PriorityQueue<Nodo> cola = new PriorityQueue<>();
        raiz.nivel = 0;
        raiz.costo = 0;
        cola.add(raiz);

        while (!cola.isEmpty()) {
            /* Extraemos el nodo con el menor costo acumulado */
            Nodo actual = cola.poll();
            expandidos++;

            if (visitados.contains(actual.estado)) {
                continue;
            }
            visitados.add(actual.estado);

            if (actual.estado.equals(estadoObjetivo)) {
                actual.nodosExpandidos = expandidos;
                return actual;
            }

            /* Generamos sucesores */
            List<Nodo> sucesores = actual.generarSucesores();
            for (Nodo hijo : sucesores) {
                if (!visitados.contains(hijo.estado)) {
                    /* Lógica de costo:
                   El hueco (' ') en el padre ahora tiene la ficha que se movió en el hijo.
                   Se suma el valor de la ficha movida al costo acumulado del padre para obtener el costo del hijo.
                     */
                    int idxHuecoPadre = actual.estado.indexOf(' ');
                    char fichaMovidaChar = hijo.estado.charAt(idxHuecoPadre);
                    int valorFicha = Character.getNumericValue(fichaMovidaChar);
                    hijo.costo = actual.costo + valorFicha;
                    cola.add(hijo);
                }
            }
        }
        return null;
    }

    Nodo busquedaCruz(String estadoObjetivo) {
        if (raiz == null) {
            return null;
        }
        int expandidos = 0;
        /* Guardamos los nodos visitados */
        HashSet<String> visitados = new HashSet<>();
        /* La PriorityQueue usará el compareTo de Nodo, que compara n.costo */
        PriorityQueue<Nodo> cola = new PriorityQueue<>();
        raiz.costo = 0;
        cola.add(raiz);

        while (!cola.isEmpty()) {
            /* Extraemos el nodo con el menor costo acumulado */
            Nodo actual = cola.poll();
            expandidos++;

            if (visitados.contains(actual.estado)) {
                continue;
            }
            visitados.add(actual.estado);

            if (actual.estado.equals(estadoObjetivo)) {
                actual.nodosExpandidos = expandidos;
                return actual;
            }

            /* Generamos sucesores */
            List<Nodo> sucesores = actual.generarSucesores();
            for (Nodo hijo : sucesores) {
                if (!visitados.contains(hijo.estado)) {
                    /* Lógica de costo:
                   El hueco (' ') en el padre ahora tiene la ficha que se movió en el hijo.
                   Se suma el valor de la ficha movida al costo acumulado del padre para obtener el costo del hijo.
                   Además, se suma la heurística de cruz para el hijo.
                   */
                    int idxHuecoPadre = actual.estado.indexOf(' ');
                    char fichaMovidaChar = hijo.estado.charAt(idxHuecoPadre);
                    int valorFicha = Character.getNumericValue(fichaMovidaChar);
                    hijo.costo = actual.costo + valorFicha + hijo.cruzheuristica(estadoObjetivo);
                    cola.add(hijo);
                }
            }
        }
        return null;
    }

}
