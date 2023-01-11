package Client;

import tools.Connect;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Client extends Thread {
    private ServerSocket server = null;
    private Socket socket = null;
    public static Connect connexion = null;
    public Client(int PORT, InetAddress address, int id_utilisateur, String password) throws IOException {
        connexion = new Connect(PORT, address, id_utilisateur, password);
        server = new ServerSocket(PORT+1);
    }

    public void run() {
        while ( true ) {
            System.out.println("Attente Serveur");
            try {
                socket = server.accept();
            } catch ( IOException e ) {
                throw new RuntimeException(e);
            }
            System.out.println("Serveur connecté");
            Traitement_serveur t1 = new Traitement_serveur(socket);
            t1.start();
            System.out.println("Le serveur répond !");
        }
    }

}
