import java.util.List;
import java.io.IOException;

public class BackendPlaceholder implements BackendInterface, PathInterface {

    GraphADT<String, Integer> graph;

    public BackendPlaceholder() {} // for tests
    public BackendPlaceholder(GraphADT<String, Integer> graph) {
	this.graph = graph;
    }

    public void readDataFromFile(String filePath) throws IOException, NullPointerException {

    }

    public PathInterface findShortestRoute(String startAirport, String destinationAirport) {
	return null;
    }

    public String getDatasetStatistics() {
	return "";
    }

    public List<String> getRoute() {
	return null;
    }

    public List<Integer> getMilesPerSegment() {
	return null;
    }

    public int getTotalMiles() {
	return 0;
    }

}
