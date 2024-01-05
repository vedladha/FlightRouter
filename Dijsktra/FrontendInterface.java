public interface FrontendInterface { 


 // public FrontendInterface(BackendInterface backend, Scanner scnr);

    /**
     * specify (and load) a data file
     */
    public void dataFile(String file);

    /**
     * show stats about the dataset that includes number of airports, number o>     * total number of miles
     */
    public void getStatistics();

    /**
     * asks the user for a start and destination airport, then lists the short>     * between those airports, including all airports on the way,the distance >     * and the total number of miles from start to destination airport
     */
    public void tripInfo();

    /**
     * starts command loop for user
     */
    public void mainLoop();

    /**
     * broken down into separate method for main loop and separate commands
     */
    public void commandLoop();

    /**
     * exit application
     */
    public void exit();

}
