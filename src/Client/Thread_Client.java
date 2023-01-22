package Client;

import tools.Connect;
import tools.Connection_Codes;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
/**
 * @author : Eliott LEBOSSE et Yohann DENOYELLE
 * Cette classe permet de créer un thread qui va écouter les messages entrants du serveur (réponses aux requetes).
 * Ainsi que de créer un thread qui va nous permettre d'envoyer des messages au serveur (requetes).
 */
public class Thread_Client extends Thread {
    private ServerSocket server = null;
    private Socket socket = null;
    public static Connect connexion = null;
    private Interface_Connection fen = null;

    public Thread_Client(int PORT, InetAddress address, String nom_utilisateur, String password, Interface_Connection fen, Boolean exist, ArrayList<String> data) throws IOException {
        connexion = new Connect(PORT, address, nom_utilisateur, password, exist, data);
        server = new ServerSocket(PORT + 1);
        this.fen = fen;
    }

    public void run() {
        System.out.println("Attente Serveur");
        try {
            socket = server.accept();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Serveur connecté");
        Traitement_serveur t1 = new Traitement_serveur(socket, fen);
        t1.start();
        System.out.println("Le serveur répond !");
    }

    public void close() throws IOException {
        connexion.send(Connection_Codes.DECONNEXION);
        connexion.close();
        server.close();
    }
}
