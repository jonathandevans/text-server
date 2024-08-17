import java.io.*;
import java.net.Socket;

/**
 * {@code SimpleClient} is used to open a socket and connect to a given server.
 * Text-based messages are sent and recieved along this socket.
 * 
 * @author 210016688
 */
public class SimpleClient {

    private static String host;
    private static int port;

    public static void main(String[] args) {

        if (!setHostAndPort(args))
            return;

        connectToServer();
    }

    /**
     * Used to convert user input into host and port, given the input is valid.
     * 
     * @param args the user input
     * @return whether the host and port is valid
     */
    public static boolean setHostAndPort(String[] args) {
        if (args.length == 2) {
            host = args[0];
            // Tries to parse the string input into an integer for the port
            try {
                port = Integer.parseInt(args[1]);
            } catch (Exception e) {
                System.out.println("Second argument provided is not an integer.");
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
     * Connects to the server using the given host and server.
     * Creates buffered readers and writers to send text from the user to the server
     * and receive a response.
     */
    private static void connectToServer() {
        // Connect to socket and creates bufferedreader and writer using
        // try-with-resources
        try (Socket socket = new Socket(host, port);
                BufferedReader kb = new BufferedReader(new InputStreamReader(System.in));
                BufferedWriter client = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader server = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {

            communicateWithServer(kb, client, server);
            close(socket, kb, client, server);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Used to send msgs from the stdin to the server and recieve responses.
     * The appropriate response is used to close the connection.
     * 
     * @param kb     the input from the user
     * @param client the stream from the client to the server
     * @param server the stream from the server to the client
     * @throws IOException an IO error has occured
     */
    private static void communicateWithServer(BufferedReader kb, BufferedWriter client, BufferedReader server)
            throws IOException {
        boolean communicating = true;
        while (communicating) {
            sendMessage(kb, client);

            // Reads server response
            String response = server.readLine();
            // GOODBYE is used to exit the loop and close the socket
            if (response.toLowerCase().equals("goodbye"))
                communicating = false;
            System.out.println(response);
        }
    }

    /**
     * Used to send a string from the client to the server.
     * 
     * @param kb     the input from the user
     * @param client the stream from the client to the server
     * @throws IOException an IO error has occured
     */
    private static void sendMessage(BufferedReader kb, BufferedWriter client) throws IOException {
        // Sends the keyboard input to the server
        client.write(kb.readLine());
        // New line is required for it to work
        client.newLine();
        client.flush();
    }

    /**
     * Used to close the socket, buffered reader and writer.
     * 
     * @param socket the socket to the server
     * @param kb     the input from the user
     * @param client the stream from the client to the server
     * @param server the stream from the server to the client
     * @throws IOException an IO error has occured
     */
    private static void close(Socket socket, BufferedReader kb, BufferedWriter client, BufferedReader server)
            throws IOException {
        socket.close();
        kb.close();
        client.close();
        server.close();
    }
}
