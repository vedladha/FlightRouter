import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Frontend implements FrontendInterface {

    // Scanner that reads user input
    public Scanner input;

    // A refrence to the backend
    public BackendInterface backend;

    public boolean exit = false;


    public Frontend(BackendInterface backend, Scanner input){
        this.backend = backend;
        this.input = input;
    }

    @Override
    /**
     * Specify (and load) a data file
     */
    public void dataFile(String file) throws NullPointerException {
        if (file == null) throw new NullPointerException();
        try {
            backend.readDataFromFile(file);
        } catch (FileNotFoundException e){
            System.out.println("The file name is invalid");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    /**
     * Show stats about the dataset that includes number of airports, number of flights, and
     * total number of miles
     */
    public void getStatistics() {
        String stats = backend.getDatasetStatistics();
        System.out.println(stats);
    }

    @Override
    /**
     * Asks the user for a start and destination airport, then lists the shortest route
     * between those airports, including all airports on the way,the distance for each segment,
     * and the total number of miles from start to destination airport
     */
    public void tripInfo() {
        String startAirport = input.next();
        String destinationAirport = input.next();
        PathInterface shortestRoute = backend.findShortestRoute(startAirport, destinationAirport);

        if(shortestRoute == null) {
            System.out.println("shortestRoute is null");
            return;
        }

        List<String> route = shortestRoute.getRoute();
        List<Integer> miles = shortestRoute.getMilesPerSegment();
        String routeString = "";
        String mileString = "";

        for(int i = 0; i < route.size(); i++){
            routeString = routeString + " " + route.get(i);
        }

        for(int i = 0; i < miles.size(); i++){
            mileString = mileString + " " + miles.get(i);
        }

        System.out.println("Shortest Route: " + routeString + ", Distance: " + mileString + ", Total Number of Miles: " + shortestRoute.getTotalMiles());
    }

    @Override
    /**
     * Starts the command loop for user
     */
    public void mainLoop() {
        System.out.println("Welcome to the FlightRouter program.\n");
        boolean file = false;

        while (!file) {
            System.out.println("Type the name of your file:\n");
            String nextInput = input.next();
            try {
                dataFile(nextInput);
                file = true;
                commandLoop();
            } catch (Exception e) {
                System.out.println("Invalid file name, please input a valid file name:\n");
            }
        }
    }

    @Override
    /**
     * Broken down into separate methods for main loop and separate commands
     */
    public void commandLoop() {
        System.out.println("You can call methods by typing:\n");
        System.out.println("stats: To get statistic infomation\n");
        System.out.println("tripInfo, start point, and destination point: To get the travel information\n");
        System.out.println("exit: To exit the program\n");

        while (!exit){
            String nextInput = input.next();
            if (nextInput.equals("stats")){
                getStatistics();
            } else if (nextInput.equals("tripInfo")) {
                tripInfo();
            } else if (nextInput.equals("exit")){
                exit();
            } else {
                System.out.println("Invalid command\n");
            }
        }
    }

    @Override
    /**
     * Exit application
      */
    public void exit() {
        exit = true;
        input.close();
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        BackendImplementation backend = new BackendImplementation(new DijkstraGraph<>(new PlaceholderMap<>()));
        Frontend frontend = new Frontend(backend, input);
        frontend.mainLoop();
    }
}
