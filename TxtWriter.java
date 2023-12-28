import java.io.FileWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TxtWriter {

    public static void writeGraphToText(Graph G) {

        try (FileWriter fileWriter = new FileWriter(
                "Output Files/n " + G.n + "_r " + G.r + "_upperCap " + G.upperCap + ".txt")) {

            fileWriter.write(G.n + "\n");
            fileWriter.write(G.r + "\n");
            fileWriter.write(G.upperCap + "\n");

            fileWriter.write(G.source + "\n");
            fileWriter.write(G.sink + "\n");
            fileWriter.write(G.longestAcyclicPathLength + "\n");
            fileWriter.write(G.totalEdges + "");

            for (Vertex v : G.vertices) {

                String line = "\n" + v.x + "," + v.y;

                for (Edge e : v.edges)
                    line += "," + e.target + "," + e.capacity;

                fileWriter.write(line);
            }

        } catch (Exception e) {
        }
    }

    public static Graph readGraphFromText() {
        Scanner scanner = new Scanner(System.in);

        Graph G;

        // Keep asking for the file name until a valid file is provided
        while (true) {
            System.out.print("\nPlease enter the file name for the graph you want to generate (read from\n" +
                    "the Input Files folder): ");
            String fileName = scanner.nextLine();
            File file = new File("Input Files/" + fileName);

            try (Scanner fileScanner = new Scanner(file)) {
                G = new Graph();
                G.n = Integer.parseInt(fileScanner.nextLine());
                G.r = Double.parseDouble(fileScanner.nextLine());
                G.upperCap = Integer.parseInt(fileScanner.nextLine());

                G.source = Integer.parseInt(fileScanner.nextLine());
                G.sink = Integer.parseInt(fileScanner.nextLine());
                G.longestAcyclicPathLength = Integer.parseInt(fileScanner.nextLine());
                G.totalEdges = Integer.parseInt(fileScanner.nextLine());

                G.vertices = new Vertex[G.n];

                for (int i = 0; i < G.n; i++) {
                    String line = fileScanner.nextLine();
                    String[] parameters = line.split(",");

                    Vertex v = new Vertex();
                    v.x = Double.parseDouble(parameters[0]);
                    v.y = Double.parseDouble(parameters[1]);

                    for (int j = 2; j < parameters.length; j += 2) {
                        Edge e = new Edge();
                        e.target = Integer.parseInt(parameters[j]);
                        e.capacity = Integer.parseInt(parameters[j + 1]);
                        v.edges.add(e);
                    }

                    G.vertices[i] = v;
                }

                break;

            } catch (FileNotFoundException e) {
                System.out.println("File not found. Please check the file name and try again.");

            } catch (Exception e) {
                System.out.println("Improper file format. Please enter a valid file.");
            }
        }

        // Close the scanner
        scanner.close();

        return G;
    }
}
