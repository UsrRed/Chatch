
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur {

    private static final int PORT = 10000; // socket port
    private ServerSocket server = null;
    private Socket socket = null;

    public Serveur() throws IOException, ClassNotFoundException {
        server = new ServerSocket(PORT);
        int numeroClient = 1;
        while (true) {
            System.out.println("Attente Client");
            socket = server.accept();
            Packets t1 = new Packets(socket);
            t1.start();
            System.out.println("Client " + numeroClient + " se connecte !");
            numeroClient++;
        }

    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new Serveur();

    }

}