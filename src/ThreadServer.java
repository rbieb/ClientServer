import java.net.Socket;

public class ThreadServer extends Thread {

    Socket newClient = null;

    ThreadServer(Socket newClient) {
        this.newClient = newClient;
    }
}
