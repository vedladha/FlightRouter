import java.io.FileNotFoundException;

/**
 * Public interface for backend flight router. It expands the instructions of the backend
 * implementation.
 *
 * BackendInterface backend = new BackendInterface();
 * Basic constructor
 * public Backend() {
 *
 * }
 * Constructor using GraphInterface implementation
 * public Backend(GraphObj graph) {
 *  this.graph = graph;
 * }
 */
public interface Backend {
    /**
     * Read data from a file and load it into the graph
     * @param filePath The parth to filePath
     */
    void readDataFromFile(String filePath) throws FileNotFoundException;

    /**
     * Find the shortest route from a start airport to a destination airport in the dataset
     * @param startAirport The starting point
     * @param destinationAirport The end point
     * @return An instance of GraphInterface with the shortest route
     */
    GraphInterface findShortestRoute(String startAirport, String destinationAirport);

    /**
     * Get statistics about the dataset (number of nodes, edges, and total miles)
     * @return A string with dataset
     */
    String getDatasetStatistics();
}