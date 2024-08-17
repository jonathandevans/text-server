import java.io.*;
import java.time.*;
import java.time.format.*;
import java.net.*;

/**
 * {@code SimpleServer} is used to open a socket and wait for a connection from
 * a client.
 * Text-based messages are sent and recieved along this socket.
 * 
 * @author 210016688
 */
public class SimpleServer {

    private static int port;

    public static void main(String[] args) {

        if (!setPort(args))
            return;

        connectToClient();
    }

    /**
     * Used to convert user input into a port, given the input is valid.
     * 
     * @param args the user input
     * @return whether the port is valid
     */
    public static boolean setPort(String[] args) {
        if (args.length == 1) {
            // Tries to parse the string input into an integer for the port
            try {
                port = Integer.parseInt(args[0]);
            } catch (Exception e) {
                System.out.println("Argument provided is not an integer.");
                return false;
            }
        } else {
            System.out.println("Incorrect number of arguments provided.");
            return false;
        }

        // Valid host and port
        return true;
    }

    /**
     * Opens a socket and waits for a client to connect.
     * Creates buffered readers and writers to recieve text from the user to create
     * a response.
     */
    private static void connectToClient() {
        // Opens socket, connects to sockets and creates bufferedreader and writer using
        // try-with-resources
        try (ServerSocket socket = new ServerSocket(port);
                Socket clientSocket = socket.accept();
                BufferedWriter server = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                BufferedReader client = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {

            // Don't need to keep the socket open as the server is only accepting one client
            socket.close();

            boolean greeting = true;
            while (greeting) {
                greeting = waitForGreeting(server, client);
            }

            boolean communicating = true;
            while (communicating) {
                communicating = chooseResponse(server, client);
            }

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Used to check for the 'HELLO' message from the client.
     * This message is required as the first message otherwise the program will
     * return true
     * 
     * @param server the stream from the server to the client
     * @param client the stream from the client to the server
     * @return whether the server is still waiting for a greeting
     * @throws IOException an IO error has occured
     */
    private static boolean waitForGreeting(BufferedWriter server, BufferedReader client) throws IOException {
        String input = client.readLine();

        if (input.toLowerCase().equals("hello")) {
            sendMessage(server, "HI NICE TO MEET YOU");
            return false;
        } else {
            sendMessage(server, "PARDON");
            return true;
        }
    }

    /**
     * Uses the message from the client to choose the appropriate output.
     * 
     * @param server the stream from the server to the client
     * @param client the stream from the client to the server
     * @throws IOException an IO error has occured
     */
    private static boolean chooseResponse(BufferedWriter server, BufferedReader client) throws IOException {
        String input = client.readLine();

        if (input.toLowerCase().equals("bye")) {
            endCommunication(server, client);
            return false;
        }

        String response;
        if (input.toLowerCase().equals("what is the time?"))
            response = getTime();
        else if (input.toLowerCase().startsWith("please add "))
            response = calculateAddition(input);
        else
            response = "PARDON";

        sendMessage(server, response);
        return true;
    }

    /**
     * Finds the local time, puts it in ISO 8601 format for a response
     * 
     * @return the response to the user
     */
    private static String getTime() {
        ZonedDateTime dateTime = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_TIME;
        String formattedTime = dateTime.format(formatter);

        return new String("THE TIME IS: " + formattedTime);
    }

    /**
     * Takes the addition msg from the user, converts the string to integers and
     * adds the value.
     * 
     * @param input the users input
     * @return the response msg
     */
    private static String calculateAddition(String input) {
        String valueString = input.substring(10);
        String[] valueArray = valueString.strip().split(" ");

        if (valueArray.length != 2)
            return "PARDON";

        try {
            int num1 = Integer.parseInt(valueArray[0]);
            int num2 = Integer.parseInt(valueArray[1]);
            int total = num1 + num2;
            return new String("THE SUM IS " + total);
        } catch (NumberFormatException e) {
            return "PARDON";
        }
    }

    /**
     * Used to send a string from the server to the client.
     * 
     * @param server   the stream from the server to the client
     * @param response the string to be sent
     * @throws IOException an IO error has occured
     */
    private static void sendMessage(BufferedWriter server, String response) throws IOException {
        server.write(response);
        // New line is required for it to work
        server.newLine();
        server.flush();
    }

    /**
     * Used to send the final messages and close the streams.
     * 
     * @param server the stream from the server to the client
     * @param client the stream from the client to the server
     * @throws IOException an IO error has occured
     */
    private static void endCommunication(BufferedWriter server, BufferedReader client) throws IOException {
        sendMessage(server, "GOODBYE");

        server.close();
        client.close();
    }
}
