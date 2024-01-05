// --== CS400 File Header Information ==--
// Name: Vedant Ladha
// Email:  vladha@wisc.edu
// Group and Team: G22
// Group TA: Robert Nagel
// Lecturer: Florian Heimerl
// Notes to Grader: <optional extra notes>

import java.util.PriorityQueue;
import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;


/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes. This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number>
        extends BaseGraph<NodeType, EdgeType>
        implements GraphADT<NodeType, EdgeType> {

    /**
     * While searching for the shortest path between two nodes, a SearchNode
     * contains data about one specific path between the start node and another
     * node in the graph. The final node in this path is stored in its node
     * field. The total cost of this path is stored in its cost field. And the
     * predecessor SearchNode within this path is referened by the predecessor
     * field (this field is null within the SearchNode containing the starting
     * node in its node field).
     * <p>
     * SearchNodes are Comparable and are sorted by cost so that the lowest cost
     * SearchNode has the highest priority within a java.util.PriorityQueue.
     */
    protected class SearchNode implements Comparable<SearchNode> {
        public Node node;
        public double cost;
        public SearchNode predecessor;

        public SearchNode(Node node, double cost, SearchNode predecessor) {
            this.node = node;
            this.cost = cost;
            this.predecessor = predecessor;
        }

        public int compareTo(SearchNode other) {
            if (cost > other.cost)
                return +1;
            if (cost < other.cost)
                return -1;
            return 0;
        }
    }

    /**
     * Constructor that sets the map that the graph uses.
     *
     * @param map the map that the graph uses to map a data object to the node
     *            object it is stored in
     */
    public DijkstraGraph(MapADT<NodeType, Node> map) {
        super(map);
    }

    /**
     * This helper method creates a network of SearchNodes while computing the
     * shortest path between the provided start and end locations. The
     * SearchNode that is returned by this method is represents the end of the
     * shortest path that is found: it's cost is the cost of that shortest path,
     * and the nodes linked together through predecessor references represent
     * all of the nodes along that shortest path (ordered from end to start).
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return SearchNode for the final end node within the shortest path
     * @throws NoSuchElementException when no path from start to end is found
     *                                or when either start or end data do not
     *                                correspond to a graph node
     */
    protected SearchNode computeShortestPath(NodeType start, NodeType end) throws NoSuchElementException {

        //checking if the start and the end nodes are in the graph
        if (!nodes.containsKey(start) || !nodes.containsKey(end)) {
            throw new NoSuchElementException("Error: No such path exists");
        }

        Node nodeToBeVisited = this.nodes.get(start);

        PlaceholderMap<NodeType, SearchNode> nodeVisited = new PlaceholderMap<NodeType, SearchNode>();

        //creating a priority queue to save the SearchNode of edge successors on the shortest path
        PriorityQueue<SearchNode> queue = new PriorityQueue<>();

        //checking the start node as being visited now
        nodeVisited.put(start, new SearchNode(nodeToBeVisited, 0, null));

        //going through all the edges of the SearchNode that leave the starting node
        for (Edge edges : nodeToBeVisited.edgesLeaving) {
            SearchNode prevNode = nodeVisited.get(edges.predecessor.data);
            SearchNode newNode = new SearchNode(this.nodes.get(edges.successor.data),
                    prevNode.cost + (Integer) edges.data, prevNode);
            queue.add(newNode);
        }

        //if the queue is not empty,execute the while loop

        while (!queue.isEmpty()) {

            SearchNode nodeWithMinCost = queue.poll();

            //marking nodes as being visited, if not visited
            if (!nodeVisited.containsKey(nodeWithMinCost.node.data)) {
                nodeToBeVisited = nodeWithMinCost.node;
                nodeVisited.put(nodeToBeVisited.data, nodeWithMinCost);

                for (Edge edges : nodeToBeVisited.edgesLeaving) {
                    SearchNode prevNode = nodeVisited.get(edges.predecessor.data);
                    SearchNode newNode = new SearchNode(this.nodes.get(edges.successor.data),
                            prevNode.cost + (Integer) edges.data, prevNode);
                    queue.add(newNode);
                }
            }
        }

        //if no path found from the start to the end node
        if (!nodeVisited.containsKey(end)) {
            throw new NoSuchElementException("Path is Invalid!");
        }
        return nodeVisited.get(end);

    }

    /**
     * Returns the list of data values from nodes along the shortest path
     * from the node with the provided start value through the node with the
     * provided end value. This list of data values starts with the start
     * value, ends with the end value, and contains intermediary values in the
     * order they are encountered while traversing this shorteset path. This
     * method uses Dijkstra's shortest path algorithm to find this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return list of data item from node along this shortest path
     */
    public List<NodeType> shortestPathData(NodeType start, NodeType end) throws NoSuchElementException {

        SearchNode currentNode = null;

        //using the protected helper method above to compute the shortest path
        try {
            currentNode = computeShortestPath(start, end);
        } catch (NoSuchElementException error) {
            throw new NoSuchElementException(error.getMessage());
        }

        //using a linkedList to add the shortest path found
        List<NodeType> shortestPathFound = new LinkedList<NodeType>();
        shortestPathFound.add(end);

        while (currentNode.predecessor != null) {
            shortestPathFound.add(0, currentNode.predecessor.node.data);
            currentNode = currentNode.predecessor;
        }
        return shortestPathFound;

    }

    /**
     * Returns the cost of the path (sum over edge weights) of the shortest
     * path freom the node containing the start data to the node containing the
     * end data. This method uses Dijkstra's shortest path algorithm to find
     * this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return the cost of the shortest path between these nodes
     */
    public double shortestPathCost(NodeType start, NodeType end) throws NoSuchElementException {

        try {
            return computeShortestPath(start, end).cost;
        } catch (NoSuchElementException error) {
            throw new NoSuchElementException(error.getMessage());
        }

    }


    /**
     * This test confirm that the results of our implementation match what we previously computed by hand.
     */

    @Test
    public void testLectureExample() {

        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());

        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");
        graph.insertNode("D");
        graph.insertNode("E");
        graph.insertNode("F");
        graph.insertNode("G");
        graph.insertNode("H");

        graph.insertEdge("A", "B", 4);
        graph.insertEdge("A", "C", 2);
        graph.insertEdge("A", "E", 15);
        graph.insertEdge("B", "E", 10);
        graph.insertEdge("B", "D", 1);
        graph.insertEdge("C", "D", 5);
        graph.insertEdge("D", "E", 3);
        graph.insertEdge("D", "F", 0);
        graph.insertEdge("F", "D", 2);
        graph.insertEdge("F", "H", 4);
        graph.insertEdge("G", "H", 4);


        //The shortest path chosen is ABD since the cost is lesser
        Assertions.assertEquals(5, graph.shortestPathCost("A", "D"));
        Assertions.assertEquals("[A, B, D]", graph.shortestPathData("A", "D").toString());


    }


    /**
     * This test check the cost and sequence of data along the shortest path between a different start and end node.
     */

    @Test
    public void testShortestPath() {

        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());

        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");
        graph.insertNode("D");
        graph.insertNode("E");
        graph.insertNode("F");
        graph.insertNode("G");
        graph.insertNode("H");

        graph.insertEdge("A", "B", 4);
        graph.insertEdge("A", "C", 2);
        graph.insertEdge("A", "E", 15);
        graph.insertEdge("B", "E", 10);
        graph.insertEdge("B", "D", 1);
        graph.insertEdge("C", "D", 5);
        graph.insertEdge("D", "E", 3);
        graph.insertEdge("D", "F", 0);
        graph.insertEdge("F", "D", 2);
        graph.insertEdge("F", "H", 4);
        graph.insertEdge("G", "H", 4);


        //checks the shortestDataCost and shortestDataPath when travelling from C to H.
        //The shortest path chosen is CDFH
        Assertions.assertEquals(9, graph.shortestPathCost("C", "H"));
        Assertions.assertEquals("[C, D, F, H]", graph.shortestPathData("C", "H").toString());


    }

    @Test
    public void behaviourOfNodes() {

        DijkstraGraph<String, Integer> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");
        graph.insertNode("D");
        graph.insertNode("E");
        graph.insertNode("F");
        graph.insertNode("G");
        graph.insertNode("H");

        graph.insertEdge("A", "B", 4);
        graph.insertEdge("A", "C", 2);
        graph.insertEdge("A", "E", 15);
        graph.insertEdge("B", "E", 10);
        graph.insertEdge("B", "D", 1);
        graph.insertEdge("C", "D", 5);
        graph.insertEdge("D", "E", 3);
        graph.insertEdge("D", "F", 0);
        graph.insertEdge("F", "D", 2);
        graph.insertEdge("F", "H", 4);
        graph.insertEdge("G", "H", 4);

        //throwing exception because there is no way to reach G
        Assertions.assertThrows(NoSuchElementException.class, () -> graph.computeShortestPath("A"
                , "G"));
        Assertions.assertThrows(NoSuchElementException.class, () -> graph.shortestPathCost("A"
                , "G"));
        Assertions.assertThrows(NoSuchElementException.class, () -> graph.shortestPathData("A"
                , "G"));

    }

}
