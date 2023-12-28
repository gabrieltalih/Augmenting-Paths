class Graph {

    // attributes that mostly don't change per max flow simulation
    public int n;
    public double r;
    public int upperCap;
    public int source;
    public int sink;
    public int longestAcyclicPathLength;
    public int totalEdges;

    public Vertex[] vertices;

    // attributes that change per max flow simulation
    public int paths;
    public double meanLength;
    public double meanProportionalLength;
    public void addEdge(int u, int v) {

        Vertex vertex = vertices[u];

        Edge edge = new Edge();
        edge.target = v;

        vertex.edges.add(edge);
    }

    private Edge getEdge(int u, int v) {

        Vertex vertex = vertices[u];

        for (Edge edge : vertex.edges) {
            if (edge.target == v)
                return edge;
        }

        return null;
    }

    public boolean hasEdge(int u, int v) {

        return getEdge(u, v) != null;
    }

    public void increaseEdgeFlow(int u, int v, int flow) {

        getEdge(u, v).flow += flow;
    }

    public void setEdgeCapacity(int u, int v, int capacity) {

        getEdge(u, v).capacity = capacity;
    }

    public int getEdgeResidualCapacity(int u, int v) {

        Edge e = getEdge(u, v);

        return e.capacity - e.flow;
    }

    public double calculateSquaredDistance(int u, int v) {

        Vertex v1 = vertices[u];
        Vertex v2 = vertices[v];

        double x_dif = v1.x - v2.x;
        double y_dif = v1.y - v2.y;

        return x_dif * x_dif + y_dif * y_dif;
    }

    public int[] getAdjacentVertices(int u) {

        int[] adjacentVertices = new int[vertices[u].edges.size()];

        for (int i = 0; i < adjacentVertices.length; i++)
            adjacentVertices[i] = vertices[u].edges.get(i).target;

        return adjacentVertices;
    }

    public void resetFlows() {

        for (Vertex v : vertices) {
            for (Edge e : v.edges)
                e.flow = 0;
        }
    }
}
