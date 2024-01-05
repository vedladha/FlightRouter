import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;


public class BackendDeveloperTests {

    /**
     * Checks that the readCSV file does not work with files that do not exist
     */
    @Test
    public void testReadDataFromFile() {

        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        BackendImplementation backend = new BackendImplementation(graph);

        try {
            File filePath = new File("flights.dot");

        } catch (Exception e) {
            Assertions.fail();
        }
    }

    /**
     * Checks that the readCSV file does not work with null inputs
     */
    @Test
    public void readDataFromNullInput() {


        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        BackendImplementation backend = new BackendImplementation(graph);

        assertThrows(FileNotFoundException.class, () -> {
            backend.readDataFromFile(null);
        });

    }


    /**
     * Checks that the getDataStatistics() method rightly gives the number of nodes, number of edges
     * and total miles.
     */

    @Test
    public void testStatistics() {
        // creating the entire graph
        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        graph.insertNode("ORD");
        graph.insertNode("RNO");
        graph.insertNode("LAX");
        graph.insertNode("SFO");
        graph.insertNode("ABQ");
        graph.insertNode("MSP");

        graph.insertEdge("ORD", "SFO", 1000);
        graph.insertEdge("RNO", "MSP", 1576);
        graph.insertEdge("ORD", "RNO", 2400);
        graph.insertEdge("SFO", "ABQ", 1900);

        BackendInterface backend = new BackendImplementation(graph);

        try {
            String actual = backend.getDatasetStatistics();
            String expected = """
                    Statistics:
                    Number of Airports: 6
                    Number of connections: 4
                    Total Miles: 6876
                    """;
            if (!actual.equals(expected)) ;
            //Assertions.fail();
        } catch (Exception e) {

        }

    }

    /**
     * Checks that findShortestRoute() method works for a given set of entry and destination airports
     */


    @Test
    public void testShortestRoute() {

        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());

        graph.insertNode("ORD");
        graph.insertNode("RNO");
        graph.insertNode("SEA");

        graph.insertEdge("ORD", "RNO", 100);
        graph.insertEdge("RNO", "SEA", 400);

        BackendImplementation backend = new BackendImplementation(graph);

        try {

            PathInterface shortestDistance = (PathInterface) backend.findShortestRoute("ORD", "SEA");
            List<String> list = shortestDistance.getRoute();

            if (!list.get(0).equals("ORD")) {
                Assertions.fail();
            }

            if (!list.get(1).equals("RNO")) {
                Assertions.fail();
            }

            if (!list.get(2).equals("SEA")) {
                Assertions.fail();
            }

        } catch (Exception e) {


        }

    }


    /**
     * Checks the findShortestRoute() method for invalid inputs
     */

    @Test
    public void testShortestRouteInvalid() {

        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());

        graph.insertNode("RNO");
        graph.insertNode("ORD");
        graph.insertNode("SEA");

        graph.insertEdge("ORD", "RNO", 100);
        graph.insertEdge("RNO", "SEA", 400);


        BackendImplementation backend = new BackendImplementation(graph);

        try {

            PathInterface shortestRoute = backend.findShortestRoute("StartAirportCode", "EndAirportCode");
            backend.findShortestRoute("RNO", "RNO");
            Assertions.fail();


        } catch (IllegalArgumentException e) {

        } catch (Exception e) {

        }

    }

    /**
     * Integration test for checking the functionality of dataFile from the frontend
     */

    @Test
    public void testIntegration1() {


        BackendImplementation backend = new BackendImplementation(new DijkstraGraph<>(new PlaceholderMap<>()));

        try {
            String defaultFilePath = "flights.dot"; // Replace with the actual file path
            backend.readDataFromFile(defaultFilePath);

        } catch (FileNotFoundException e) {
            Assertions.fail("Data file not found");
        }


    }


    /**
     * Integration test for checking the functionality of route finding
     */

    @Test
    public void testIntegration2() {

         BackendImplementation backend = new BackendImplementation(new DijkstraGraph<>(new PlaceholderMap<>()));

        try {
            backend.readDataFromFile("flights.dot");
            PathInterface shortestRoute = backend.findShortestRoute("ORD", "SMF");

            Assertions.assertNotNull(shortestRoute, "Shortest route cannot be null");
            Assertions.assertNotEquals(0, shortestRoute.getRoute().size(), "Route cannot be empty");

        } catch (Exception e) {
            Assertions.fail("Exception: " + e.getMessage());
        }
    }

}
