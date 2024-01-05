import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class FrontendDeveloperTests {

        /**
         * This method aims to verify the dataFile method, specifically that it throws an exception when null is passed in
         */
        @Test
        public void test1() {
                BackendInterface backend = new BackendPlaceholder();
                Scanner input = new Scanner(System.in);
                Frontend frontend = new Frontend(backend, input);
                String str = null;
                try {
                        frontend.dataFile(str);
                        Assertions.fail("Method should have thrown exception");
                } catch (NullPointerException e) {}
        }

        /**
         * This method aims to verify the commandLoop method, specifically when it is fed gibberish input
         */
        @Test
        public void test2() {
                BackendInterface backend = new BackendPlaceholder();
                Scanner input = new Scanner(System.in);
                Frontend frontend = new Frontend(backend, input);
                frontend.commandLoop(); // Assumes commandLoop method waits for input, invalid input should return message
                TextUITester tester = new TextUITester("aogyu");
                String actual = tester.checkOutput();
                String expected = ""; // "Option not valid"?
                Assertions.assertEquals(expected, actual);
        }

        /**
         * This method aims to verify the mainLoop method, specifically to see if it produces the welcome message
         */
        @Test
        public void test3() {
                BackendInterface backend = new BackendPlaceholder();
                Scanner input = new Scanner(System.in);
                Frontend frontend = new Frontend(backend, input);
                frontend.mainLoop();
                TextUITester tester = new TextUITester("");
                String actual = tester.checkOutput();
                String expected = "Welcome to the FlightRouter program.\n";
                Assertions.assertEquals(expected, actual);
        }

        /**
         * This method aims to verify the getStatistics method, specifically that it's output is correct
         */
        @Test
        public void test4() {
                BackendInterface backend = new BackendPlaceholder();
                Scanner input = new Scanner(System.in);
                Frontend frontend = new Frontend(backend, input);
                TextUITester tester = new TextUITester("stats"); // Might be a different call command to showStatistics method
                frontend.getStatistics();
                String actual = tester.checkOutput();
                String expected = "\n"; // Need to change once backend is given
                Assertions.assertEquals(expected, actual);
        }

        /**
         * This method aims to verify the dataFile method, specifically when called on a non-existent file
         */
        @Test
        public void test5() {
                BackendInterface backend = new BackendPlaceholder();
                Scanner input = new Scanner(System.in);
                Frontend frontend = new Frontend(backend, input);
                frontend.dataFile("non-existent file");
                TextUITester tester = new TextUITester("non-existent file");
                String actual = tester.checkOutput();
                String expected = ""; // "The file name is invalid"?
                Assertions.assertEquals(expected, actual);
        }

        /**
         * This method aims to verifiy the findShortestRoute method, specifically that it returns null when one airport is null
         */
        @Test
        public void integrationTest1() {
                BackendInterface backend = new BackendPlaceholder();
                PathInterface result = backend.findShortestRoute("ORD", null);
                if (result != null) Assertions.fail("Method should have returned null");
        }

        /**
         * This method aims to verify the getDatasetStatistics method, specifically that the output of it is correct
         */
        @Test
        public void integrationTest2() {
                BackendInterface backend = new BackendPlaceholder();
                String result = backend.getDatasetStatistics();
                if (result != "") Assertions.fail("Method doesn't return proper output"); // Need to updat eonce code becomes available
        }
}

/**
 * This class can be used to test text based user interactions by 1) specifying
 * a String of text input (that will be fed to System.in as if entered by the user),
 * and then 2) capturing the output printed to System.out and System.err in String
 * form so that it can be compared to the expect output.
 * @author dahl
 * @date 2021.10
 */
class TextUITester {

    /**
     * This main method demonstrates the use of a TextUITester object to check
     * the behavior of the following run method.
     * @param args from the commandline are not used in this example
     */
    public static void main(String[] args) {

        // 1. Create a new TextUITester object for each test, and
        // pass the text that you'd like to simulate a user typing as only argument.
        TextUITester tester = new TextUITester("apple\n3.14\nq\n");

        // 2. Create a Scanner, and run then code that you want to test here:
        run(); // (this code should read from System.in and write to System.out)

        // Note hat you cannot see output printed by your program between the
        // constructor call above and the checkOutput call below.

        // 3. Check whether the output printed to System.out matches your expectations.
        String output = tester.checkOutput();
        if(output.startsWith("Welcome to the run program.") &&
           output.contains("apple4.14"))
            System.out.println("Test passed.");
        else
            System.out.println("Test FAILED.");
    }

    /**
     * This is the code being tested by the main method above.
     * It 1) prints out a welcome message,
     *    2) reads a String, a double, and a character from System.in, and then
     *    3) prints out the string followed by a number that is one greater than that double,
     *       if the character that it read in was a (lower case) 'q'.
     */
    public static void run() {
        // Note to avoid instantiating more than one Scanner reading from System.in in your code!
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome to the run program.");
        System.out.println("Enter a string, a double, and then q to quit: ");
        String s = in.nextLine();
        double d = in.nextDouble(); in.nextLine(); // read newline after double
        char c = in.nextLine().charAt(0);
        if(c == 'q')
            System.out.println(s + (d + 1.0));
        in.close();
    }

    // Below is the code that actually implements the redirection of System.in and System.out,
    // and you are welcome to ignore this code: focusing instead on how the constructor and
    // checkOutput() method is used int he example above.

    private PrintStream saveSystemOut; // store standard io references to restore after test
    private PrintStream saveSystemErr;
    private InputStream saveSystemIn;
    private ByteArrayOutputStream redirectedOut; // where output is written to durring the test
    private ByteArrayOutputStream redirectedErr;

    /**
     * Creates a new test object with the specified string of simulated user input text.
     * @param programInput the String of text that you want to simulate being typed in by the user.
     */
    public TextUITester(String programInput) {
        // backup standard io before redirecting for tests
        saveSystemOut = System.out;
        saveSystemErr = System.err;
        saveSystemIn = System.in;
        // create alternative location to write output, and to read input from
        System.setOut(new PrintStream(redirectedOut = new ByteArrayOutputStream()));
        System.setErr(new PrintStream(redirectedErr = new ByteArrayOutputStream()));
        System.setIn(new ByteArrayInputStream(programInput.getBytes()));
    }

    /**
     * Call this method after running your test code, to check whether the expected
     * text was printed out to System.out and System.err.  Calling this method will
     * also un-redirect standard io, so that the console can be used as normal again.
     *
     * @return captured text that was printed to System.out and System.err durring test.
     */
    public String checkOutput() {
        try {
            String programOutput = redirectedOut.toString() + redirectedErr.toString();
            return programOutput;
        } finally {
            // restore standard io to their pre-test states
            System.out.close();
            System.setOut(saveSystemOut);
            System.err.close();
            System.setErr(saveSystemErr);
            System.setIn(saveSystemIn);
        }
    }
}
