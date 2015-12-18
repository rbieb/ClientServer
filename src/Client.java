import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException, InterruptedException {
        // Create scanner to read from system in
        Scanner input = new Scanner(System.in);

        try {
            while (true) {
                // Start the socket
                Socket client = new Socket("localhost", 4444); //ec2-54-229-5-118.eu-west-1.compute.amazonaws.com
                System.out.println("Client started!");

                System.out.print("Please enter the full path to the file you'd like to see: ");
                String preliminaryMessage = input.nextLine();

                // Exit closes the connection
                if ("exit".equals(preliminaryMessage)) {
                    System.out.println("Connection terminated");
                    break;
                    // More than 20 chars is too long
                } else if (preliminaryMessage.length() > 20) {
                    System.out.println("Message is too long");
                    // Spaces are forbidden
                } else if (preliminaryMessage.contains(" ")) {
                    System.out.println("Message must not contain spaces");
                } else {
                    // Put the message and the protocol info together
                    String finalMessageToServer = ("GET /" + preliminaryMessage + " " + "HTTP/1.0");

                    // These are needed for the messages being sent to the server
                    OutputStream out = client.getOutputStream();
                    PrintWriter writer = new PrintWriter(out);

                    // This is where we receive the messages from the server
                    InputStream in = client.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    // Actually send the message
                    writer.write(finalMessageToServer + "\n");
                    writer.flush();

                    // Print whatever the server sent until null is received
                    for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                        System.out.print(line + "\n");
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        } finally {
        }
    }
}
