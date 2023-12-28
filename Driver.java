import java.util.Scanner;

public class Driver {

    public static void main(String[] args) {

        System.out.println("\n\nThe program has three modes:\n");

        System.out.println("Mode 1: An input file will be read with an already generated graph.\n");

        System.out.println("Mode 2: A random directed Euclidean neighbour graph will be generated\n" +
                "for a set of paramaters inputted the by user. It will additionally\n" +
                "create an output file for the graph generated.\n");

        System.out.println("Mode 3: Multiple random directed Euclidean neighbour graphs will be\n" +
                "generated for different sets of predetermined parameters. It will\n" +
                "additionally create an output file for each graph generated.\n");

        System.out.println("After, all graphs generated/read will be run on with the Ford Fulkerson\n" +
                "algorithm to maximize flow. This algorithm had 4 modes for different ways\n" +
                "for how the augmenting path will be generated.\n");

        System.out.println("And finally, all results will be tabulated for each different augmenting path\n" +
                "and set of parameters. The statistics displayed will be the number of\n" +
                "paths generated, the average length of the path, the average length\n" +
                "of the path proportional to the distance between the source and the\n" +
                "sink, and the total number of edges.\n");

        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter your choice of mode (1, 2 or 3): ");
        String choice = scanner.nextLine();

        while (!(choice.equals("1") || choice.equals("2") || choice.equals("3"))) {
            System.out.println("Invalid choice! Please try again.");
            System.out.print("Enter your choice (1, 2 or 3): ");
            choice = scanner.nextLine();
        }
        
        switch (choice) {
            case "1":
                getGraphFromInputFile();
                break;
            case "2":
                getGraphFromUserInput();
                break;
            case "3":
                getGraphsFromGivenTestValues();
                break;
        }

        System.out.println("\nThanks for checking out my program, have a good day :)\n");
        
        scanner.close();
    }

    private static void getGraphFromInputFile() {

        Graph G = TxtWriter.readGraphFromText();

        System.out.println("\nHere are the results:");

        tablulateSingleGraph(G);
    }

    private static void getGraphFromUserInput() {

        System.out.println("\nPlease input your parameters for the graph generation.");

        String n;
        String r;
        String upperCap;

        Scanner scanner = new Scanner(System.in);

        System.out.print("\nPlease enter an integer greater than 9 for the number of nodes: ");
        n = scanner.nextLine();
        
        while (!isInteger(n) || Integer.parseInt(n) <= 9) {
            System.out.print("\nInvalid input, please enter an integer greater than 9: ");
            n = scanner.nextLine();
        }

        System.out.print("\nPlease enter a real number between 0.1 and 1 for the max length of any edge: ");
        r = scanner.nextLine();

        while (!isDouble(r) || Double.parseDouble(r) < 0.1 || Double.parseDouble(r) > 1 ) {
            System.out.print("\nInvalid input, please enter a real number between 0.1 and 1: ");
            r = scanner.nextLine();
        } 

        System.out.print("\nPlease enter a positive integer for the max capacity of any edge: ");
        upperCap = scanner.nextLine();

        while (!isInteger(upperCap) || Integer.parseInt(upperCap) <= 0) {
            System.out.print("\nInvalid input, please enter a positive integer: ");
            
            upperCap = scanner.nextLine();
        }

        scanner.close();

        Graph G = GraphGeneration.GenerateSinkSourceGraph(Integer.parseInt(n), Double.parseDouble(r), Integer.parseInt(upperCap));

        tablulateSingleGraph(G);

        System.out.println("\nThe graph generated was stored in the Output Files folder.");
    }

    private static void tablulateSingleGraph(Graph G) {

        String[] algoNames = { "SAP", "DFS", "MaxCap", "Random" };

        System.out.println("\nAlgorithm   n     r     upperCap   Paths   ML      MPL    Total Edges");
        System.out.println("---------------------------------------------------------------------");

        for (int i = 0; i <= 3; i++) {
            AugmentingPathAlgos.FordFulkersonMethod(G, i);
            System.out.printf("%-11s %-5s %-5.1f %-10s %-7s %-7.2f %-6.2f %-9s\n",
                    algoNames[i], G.n, G.r, G.upperCap, G.paths, G.meanLength,
                    G.meanProportionalLength, G.totalEdges);
        }
    }

    private static void getGraphsFromGivenTestValues() {

        System.out.println("\nHere are the results for graphs with the following parameters:\n");

        double[][] testValues1 = {
                { 100, 0.2, 2 },
                { 200, 0.2, 2 },
                { 100, 0.3, 2 },
                { 200, 0.3, 2 },
                { 100, 0.2, 50 },
                { 200, 0.2, 50 },
                { 100, 0.3, 50 },
                { 200, 0.3, 50 }
        };

        tabulateMultipleTestValues(testValues1);

        System.out.println("\n\nAnd these values were chosen to highlight the differences\n" +
                "between the different algorithms:\n");

        double[][] testValues2 = {
                { 200, 0.4, 2 },
                { 200, 0.4, 50 }
        };

        tabulateMultipleTestValues(testValues2);

        System.out.println("\nAll graphs generated were stored in the Output Files folder.");
    }

    private static void tabulateMultipleTestValues(double[][] testValues) {

        String[] algoNames = { "SAP", "DFS", "MaxCap", "Random" };

        System.out.println("Algorithm   n     r     upperCap   Paths   ML      MPL    Total Edges");

        for (double[] testValue : testValues) {

            Graph G = GraphGeneration.GenerateSinkSourceGraph((int) testValue[0], testValue[1], (int) testValue[2]);

            System.out.println("---------------------------------------------------------------------");

            for (int i = 0; i <= 3; i++) {
                AugmentingPathAlgos.FordFulkersonMethod(G, i);
                System.out.printf("%-11s %-5s %-5.1f %-10s %-7s %-7.2f %-6.2f %-9s\n",
                        algoNames[i], G.n, G.r, G.upperCap, G.paths, G.meanLength,
                        G.meanProportionalLength, G.totalEdges);
            }
        }
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
