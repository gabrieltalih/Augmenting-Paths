import java.util.ArrayList;

public class AugmentingPathAlgos {

    public static void FordFulkersonMethod(Graph G, int executionType) {

        int numberOfPaths = 0;
        int totalPathLength = 0;

        G.resetFlows();

        while (true) {

            int[] augmentingPath;

            // Chooses which augmenting path algo to use
            switch (executionType) {
                default:
                case 0:
                    augmentingPath = ShortestAugmentingPath(G);
                    break;
                case 1:
                    augmentingPath = DepthFirstSearchLike(G);
                    break;
                case 2:
                    augmentingPath = MaxCap(G);
                    break;
                case 3:
                    augmentingPath = RandomKey(G);
                    break;
            }

            if (augmentingPath == null)
                break;
            
            numberOfPaths++;
            totalPathLength += (augmentingPath.length - 1);

            // finds the minimum residual capacity along the augmenting path,
            // and adds it to the flow of all edges on said path

            int minResidualCapacity = G.getEdgeResidualCapacity(augmentingPath[0], augmentingPath[1]);

            for (int i = 1; i < augmentingPath.length - 1; i++) {

                int currentResidualCapacity = G.getEdgeResidualCapacity(augmentingPath[i], augmentingPath[i + 1]);

                if (currentResidualCapacity < minResidualCapacity)
                    minResidualCapacity = currentResidualCapacity;
            }

            for (int i = 0; i < augmentingPath.length - 1; i++)
                G.increaseEdgeFlow(augmentingPath[i], augmentingPath[i + 1], minResidualCapacity);
        }
        
        // Calculate the results before the algoritm terminates
        G.paths = numberOfPaths;
        G.meanLength = totalPathLength / (double) numberOfPaths;
        G.meanProportionalLength = totalPathLength / (double) (numberOfPaths * G.longestAcyclicPathLength);
    }

    // applies a regular dijkstra's algorithm search along the graph,
    // ignores edges which have 0 residual capacity
    private static int[] ShortestAugmentingPath(Graph G) {

        int n = G.n;

        int[] distances = new int[n];
        int[] predecessors = new int[n];

        for (int u = 0; u < n; u++) {
            distances[u] = Integer.MAX_VALUE;
            predecessors[u] = -1;
        }

        distances[G.source] = 0;

        PriorityQueue Q = new PriorityQueue();

        for (int u = 0; u < n; u++)
            Q.addElement(u, distances[u]);

        while (!Q.isEmpty()) {

            int u = Q.getMinimumElement();

            for (int v : G.getAdjacentVertices(u)) {

                if (distances[u] + 1 < distances[v] && distances[u] != Integer.MAX_VALUE
                        && G.getEdgeResidualCapacity(u, v) > 0) {

                    distances[v] = distances[u] + 1;
                    predecessors[v] = u;
                    Q.updateKey(v, distances[v]);
                }
            }
        }

        return ReturnPathToSink(predecessors, G.source, G.sink);
    }

    // Preforms dijkstra's algorithm but every time a node is first reached, it is
    // given the highest priority in the PriorityQueue using a decreasing counter.
    // Therefore it preforms like a depth first search.
    private static int[] DepthFirstSearchLike(Graph G) {

        int n = G.n;

        int[] distances = new int[n];
        int[] predecessors = new int[n];

        // abs of distance represents when a node was first reached, unless
        // it is Integer.MAX_VALUE, then node has yet to be visited by another node.
        for (int u = 0; u < n; u++) {
            distances[u] = Integer.MAX_VALUE;
            predecessors[u] = -1;
        }

        distances[G.source] = 0;

        int decreasingCounter = 0;

        PriorityQueue Q = new PriorityQueue();

        for (int u = 0; u < n; u++)
            Q.addElement(u, distances[u]);

        while (!Q.isEmpty()) {

            int u = Q.getMinimumElement();

            for (int v : G.getAdjacentVertices(u)) {

                // Ignores edges of zero residual capacity
                if (distances[v] == Integer.MAX_VALUE && G.getEdgeResidualCapacity(u, v) > 0) {

                    distances[v] = decreasingCounter;
                    predecessors[v] = u;
                    Q.updateKey(v, distances[v]);

                    decreasingCounter--;
                }
            }
        }

        return ReturnPathToSink(predecessors, G.source, G.sink);
    }

    // Attempts to choose path with max residual capacity along the edges, does this
    // by recursively making it so that any node reached has the maximum residual
    // capacity possible as its distance, and uses that to get the max residual of 
    // other nodes.
    private static int[] MaxCap(Graph G) {

        int n = G.n;

        int[] distances = new int[n];
        int[] predecessors = new int[n];

        // distance now represents whats the highest available residual capacity for
        // this node
        for (int u = 0; u < n; u++) {
            distances[u] = 0;
            predecessors[u] = -1;
        }

        distances[G.source] = Integer.MAX_VALUE;

        PriorityQueue Q = new PriorityQueue();

        // We want nodes with higher capacity to be of more priority, hence the negative
        // sign on the key.
        for (int u = 0; u < n; u++)
            Q.addElement(u, -distances[u]);

        while (!Q.isEmpty()) {

            int u = Q.getMinimumElement();

            for (int v : G.getAdjacentVertices(u)) {

                int availableResidualCapacity = Math.min(distances[u], G.getEdgeResidualCapacity(u, v));

                // Improves the residual capacity of a node whenever possible
                if (distances[v] < availableResidualCapacity) {

                    distances[v] = availableResidualCapacity;
                    predecessors[v] = u;
                    Q.updateKey(v, -distances[v]);
                }
            }
        }

        return ReturnPathToSink(predecessors, G.source, G.sink);
    }

    // As the name suggests, uses values rather than a decreasing counter for the
    // key, causes the graph to randomly choose the order of which nodes to search.
    private static int[] RandomKey(Graph G) {

        int n = G.n;

        int[] distances = new int[n];
        int[] predecessors = new int[n];

        for (int u = 0; u < n; u++) {
            distances[u] = Integer.MAX_VALUE;
            predecessors[u] = -1;
        }

        distances[G.source] = 0;

        PriorityQueue Q = new PriorityQueue();

        for (int u = 0; u < n; u++)
            Q.addElement(u, distances[u]);

        while (!Q.isEmpty()) {

            int u = Q.getMinimumElement();

            for (int v : G.getAdjacentVertices(u)) {

                if (distances[v] == Integer.MAX_VALUE && G.getEdgeResidualCapacity(u, v) > 0) {

                    distances[v] = (int) (Math.random() * Integer.MAX_VALUE);
                    predecessors[v] = u;
                    Q.updateKey(v, distances[v]);
                }
            }
        }

        return ReturnPathToSink(predecessors, G.source, G.sink);
    }

    // Finds a path from the source to the sink based on the predecessor array.
    // If the path returned doesnt start from the source, it returns.
    private static int[] ReturnPathToSink(int[] predecessors, int source, int sink) {

        ArrayList<Integer> reveresedPath = new ArrayList<>();
        reveresedPath.add(sink);

        int currentPredecessor = sink;

        while (predecessors[currentPredecessor] != -1 && reveresedPath.size() < predecessors.length) {
            reveresedPath.add(predecessors[currentPredecessor]);
            currentPredecessor = predecessors[currentPredecessor];
        }

        if (reveresedPath.size() < 2 || reveresedPath.get(reveresedPath.size() - 1) != source)
            return null;

        int[] path = new int[reveresedPath.size()];

        for (int i = 0; i < path.length; i++)
            path[i] = reveresedPath.get(path.length - 1 - i);

        return path;
    }
}
