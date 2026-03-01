
public class Node {
    /* El algoritmo de IDA* no guarda una lista de hijos de cada nodo */
    private boolean visited;
    private String state;
    private Node parent;
    private int cost; // Representa el costo real acumulado (g)
    private int estimatedCostToGoal; // Representa la heurística (h)
    private int totalCost; // Representa el costo total (f = g + h)
    private int depth;

    /* Constructor */
    public Node(String state) {
        this.state = state;
    }

    /* Getters y Setters */
    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    /**
     * Ahora también guarda los valores individuales de
     * 'cost' y 'estimatedCostToGoal' para que no se pierdan.
     */
    public void setTotalCost(int cost, int estimatedCost) {
        this.cost = cost;
        this.estimatedCostToGoal = estimatedCost;
        this.totalCost = cost + estimatedCost;
    }

    public int getEstimatedCostToGoal() {
        return estimatedCostToGoal;
    }

    public void setEstimatedCostToGoal(int estimatedCostToGoal) {
        this.estimatedCostToGoal = estimatedCostToGoal;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}
