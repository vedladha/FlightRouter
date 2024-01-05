import java.util.List;
/**
 * Interface of GraphInterface, will implement GraphADT.
 * Getter methods for route, list of miles, and total miles
 *
 * Sample Constructor:
 * public GraphObj() {
 *
 * }
 *
 * GraphInterface graph = new GraphInterface();
 *
 */
public interface GraphInterface{
    /**
     * Get the route as a list of airports
     *
     * @return List of airports
     */
    List<String> getRoute();

    /**
     * Get the miles to travel per segment
     *
     * @return List of miles per segment
     */
    List<Integer> getMilesPerSegment();

    /**
     * Get the total miles
     *
     * @return Total miles for the route
     */
    int getTotalMiles();
}
