package Client;

import tools.Connect;

import java.io.IOException;
import java.lang.foreign.Addressable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private ServerSocket server = null;
    private Socket socket = null;
    public static Connect connexion = null;
    public Client(int PORT, InetAddress address, int id_utilisateur, String password) throws IOException {
        connexion = new Connect(PORT, address, id_utilisateur, password);
        server = new ServerSocket(PORT);
        Boolean state = true;
        while ( state ) {
            System.out.println("Attente Client");
            socket = server.accept();
            Traitement_serveur t1 = new Traitement_serveur(socket);
            t1.start();
            System.out.println("Le serveur répond !");
        }
    }

}
