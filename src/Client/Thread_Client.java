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

    /**
     * Constructeur de la classe Thread_Client
     *
     * @param PORT            le port sur lequel le serveur écoute
     * @param address         l'adresse du serveur
     * @param nom_utilisateur le nom d'utilisateur
     * @param password        le mot de passe
     * @param fen             la fenêtre de connexion
     * @param exist           si l'utilisateur existe ou non
     * @param data            les données de l'utilisateur
     * @throws IOException
     */
    public Thread_Client(int PORT, InetAddress address, String nom_utilisateur, String password, Interface_Connection fen, Boolean exist, ArrayList<String> data) throws IOException {
        connexion = new Connect(PORT, address, nom_utilisateur, password, exist, data);
        server = new ServerSocket(PORT + 1);
        this.fen = fen;
    }

    /**
     * Cette méthode permet de lancer le thread qui va écouter les messages entrants du serveur.
     */
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

    /**
     * Cette méthode permet d'envoyer un message au serveur.
     *
     * @throws IOException
     */
    public void close() throws IOException {
        connexion.send(Connection_Codes.DECONNEXION);
        connexion.close();
        server.close();
    }
}
