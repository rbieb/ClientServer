import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        Socket newConnection = null;

        try {
            // Create the server socket
            ServerSocket server = new ServerSocket(4444);

            // Ausgabe zur Information
            System.out.println("Server listening on port 4444");

            // Server needs to always be ready for new input
            while (true) {
                Socket client = server.accept();
                new ThreadServer(client).start();

                // Client has successfully been connected
                System.out.println("Client connection has been accepted");

                // Receive messages from the client
                InputStream in = client.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                // Send messages back to the client
                OutputStream out = client.getOutputStream();
                PrintWriter writer = new PrintWriter(out);

                String receivedMessage = null;

                // As long as the message isn't empty, work with it!
                while ((receivedMessage = reader.readLine()) != null) {

                    // Split off the "GET" part of the message
                    String[] parts = receivedMessage.split("/", 2);
                    // Split off the "HTTP/1.0" part of the message
                    String[] parts2 = parts[1].split("\\s+");
                    String fileName = parts2[0];

                    // Try to read the file
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(fileName));
                        String line;
                        // Keep working as long as there are lines in the file
                        while ((line = br.readLine()) != null) {
                            // Send the HTTP stuff and the actual line
                            writer.write("HTTP/1.0 200 OK" + "\n\n");
                            writer.write(line + "\n");
                            writer.flush();
                        }

                        // The requested file does not exist
                    } catch (FileNotFoundException e) {
                        System.out.println(parts2[0]);
                        String fileNotFoundError = ("HTTP/1.0 500 ERROR" + "\n\n" + "File " + parts2[0] + " not found" + "\n");
                        writer.write(fileNotFoundError + "\n");
                        writer.flush();
                    }
                    // Close the connection
                    client.close();
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Connection was terminated because of an error! \n" + e);
        } finally {
        }
    }
}
