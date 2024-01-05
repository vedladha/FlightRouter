import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BackendImplementation implements BackendInterface{

    private GraphADT<String, Integer> graph;

    public BackendImplementation(GraphADT<String, Integer> graph) {
        this.graph = graph;
    }

    int totalMiles = 0;

    @Override
    public void readDataFromFile(String filePath) throws FileNotFoundException {

        if(filePath == null){
           throw new FileNotFoundException("File Not Found");
        }

        File flightRouterFile = new File(filePath);

        if(!flightRouterFile.exists() || !flightRouterFile.isFile()){
            throw new FileNotFoundException("File could not be found or file is not a readable file");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;
            line = reader.readLine();
            while ((line = reader.readLine()) != null) {

                if (line.contains("[miles=")) {
                    //splitting the lines to extract information
                    String[] parts = line.split("\" -- \"|\"|\\[miles=|\\];");
                    if (parts.length >= 5) {

                        String initial = parts[1].trim();
                        graph.insertNode(initial);

                        String destination = parts[2].trim();
                        graph.insertNode(destination);


                        int miles = Integer.parseInt(parts[4].trim());
                        totalMiles = miles;

                        // Insert flights directly into the graph
                        graph.insertEdge(initial, destination, miles);

                    } else if (line.contains("[city=")) {
                        //if the line contains information about the city, stop executing
                        break;
                    } else {
                        System.out.println("Unexpected parts length: " + parts.length);
                        System.out.println("Line: " + line);
                    }
                }
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public PathInterface findShortestRoute(String startAirport, String destinationAirport) {

        // Checking if the start and the destination airports are present in the graph
        if (!graph.containsNode(startAirport) || !graph.containsNode(destinationAirport)) {
            throw new IllegalArgumentException("Start or Destination Airport not found in the graph");
        }

        List<String> shortestRoute = graph.shortestPathData(startAirport, destinationAirport);

        // Calculate total miles for the current route
        List<Integer> segmentMiles = new ArrayList<>();
        int totalMiles = 0;

        for (int i = 0; i < shortestRoute.size() - 1; i++) {
            String currentAirport = shortestRoute.get(i);
            String nextAirport = shortestRoute.get(i + 1);
            int miles = graph.getEdge(currentAirport, nextAirport);
            segmentMiles.add(miles);
            totalMiles += miles;
        }

        // Create a PathObject with the calculated route details
        PathObject path = new PathObject(shortestRoute, segmentMiles, totalMiles);

        // Return the shortest path
        return path;
    }


    public String getDatasetStatistics() {


        int nodes = graph.getNodeCount();
        int edges = graph.getEdgeCount();

        System.out.println(nodes + " " + edges);

        String numNodesString = Integer.toString(nodes);
        String numEdgesString = Integer.toString(edges);
        String totalMilesString = Integer.toString(totalMiles);

        return "Number of Nodes: " + numNodesString + "Number of Edges: " + numEdgesString + "Total Miles: " + totalMilesString;
    }


    /**
    Main class to run the application
    */

   public static void main(String[] args){

        Scanner scan = new Scanner(System.in);
        BackendImplementation backend = new BackendImplementation(new DijkstraGraph<>(new PlaceholderMap<>()));
        Frontend integratedApp = new Frontend(backend, scan);
        integratedApp.mainLoop();

}  

}


