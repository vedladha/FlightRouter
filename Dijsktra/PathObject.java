import java.util.ArrayList;
import java.util.List;

public class PathObject implements PathInterface {

    private List<String> route;
    private List<Integer> segmentMiles;
    private int totalMiles;

    public PathObject(List<String> route, List<Integer> segmentMiles, int totalMiles) {
        this.route = route;
        this.segmentMiles = segmentMiles;
        this.totalMiles = totalMiles;
    }

    @Override
    public List<String> getRoute() {
        return route;
    }

    @Override
    public List<Integer> getMilesPerSegment() {
        return segmentMiles;
    }

    @Override
    public int getTotalMiles() {
        return totalMiles;
    }

}