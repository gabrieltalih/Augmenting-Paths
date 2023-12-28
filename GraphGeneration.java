import java.util.LinkedList;
import java.util.Queue;

public class GraphGeneration {

    public static Graph GenerateSinkSourceGraph(int n, double r, int upperCap) {
        // n is number of vertices, r is maximum distance between nodes sharing an edge,
        // upperCap is maximum capacity value

        Graph G = new Graph();
        G.n = n;
        G.r = r;
        G.upperCap = upperCap;
        G.vertices = new Vertex[n];

        for (int v = 0; v < n; v++) {
            G.vertices[v] = new Vertex();
            G.vertices[v].x = Math.random();
            G.vertices[v].y = Math.random();
        }

        for (int u = 0; u < n; u++) {

            for (int v = 0; v < n; v++) {

                if ((u != v) && (G.calculateSquaredDistance(u, v) <= r * r)) {

                    if (!G.hasEdge(u, v) && !G.hasEdge(v, u)) {

                        if (Math.random() < 0.5)
                            G.addEdge(u, v);
                        else
                            G.addEdge(v, u);
                    }
                }
            }
        }

        int totalEdges = 0;

        for (int u = 0; u < n; u++) {

            for (int v : G.getAdjacentVertices(u)) {

                totalEdges++;

                // random number from 1 to upperCap
                G.setEdgeCapacity(u, v, (int) (Math.random() * upperCap) + 1);
            }
        }

        G.totalEdges = totalEdges;

        if (totalEdges == 0) {
            System.out.println(
                    "\nGraph generated has no edges, therefore cannot maximize flow. No results will be shown\n" +
                            "\nThanks for checking out my program, have a good day :)\n");
            System.exit(0);
        }

        ChooseSinkAndSource(G);

        TxtWriter.writeGraphToText(G);

        return G;
    }

    private static void ChooseSinkAndSource(Graph G) {
        // Chooses random source, and makes sure it
        // has at least one edge leaving it.

        int n = G.n;
        int s;
        do {
            s = (int) (Math.random() * n);
        } while (G.getAdjacentVertices(s).length == 0);

        G.source = s;

        // applies breadth-first search, makes the a vertex of largest
        // distance to be the sink

        boolean[] visted = new boolean[n];
        int[] distance = new int[n];

        for (int i = 0; i < n; i++) {
            visted[i] = false;
            distance[i] = -1;
        }

        visted[s] = true;
        distance[s] = 0;

        Queue<Integer> Q = new LinkedList<>();
        Q.add(s);

        while (!Q.isEmpty()) {

            int u = Q.remove();

            for (int v : G.getAdjacentVertices(u)) {

                if (!visted[v]) {

                    visted[v] = true;
                    distance[v] = distance[u] + 1;
                    Q.add(v);
                }
            }
        }

        int sink = -1;
        int maxDistance = -1;

        for (int i = 0; i < distance.length; i++) {
            if (distance[i] > maxDistance) {
                sink = i;
                maxDistance = distance[i];
            }
        }

        G.sink = sink;

        // Stores the distance of this sink from the source
        G.longestAcyclicPathLength = maxDistance;
    }
}
