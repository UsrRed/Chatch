package Serveur;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur {

    private static final int PORT = 2009; // socket port
    private ServerSocket server = null;
    private Socket socket = null;

    public Serveur() throws IOException, ClassNotFoundException {
        Database data = new Database("10.195.25.15", "3306", "22104409t", "sae302");
        data.connect();

        server = new ServerSocket(PORT);
        Boolean state = true;
        while (state) {
            System.out.println("Attente Client");
            socket = server.accept();
            Traitement_client t1 = new Traitement_client(socket, data, PORT);
            t1.start();
        }
        // déconnexion de la BDD
        data.disconnect();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new Serveur();

    }

}