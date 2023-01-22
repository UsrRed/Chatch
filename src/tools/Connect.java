package tools;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author : Eliott LEBOSSE et Yohann DENOYELLE
 * Cette classe permet de créer une connexion serveur/client ou client/serveur.
 * Très pratique pour formaliser les échanges entre les deux.
 */
public class Connect {
    private Socket socket = null;
    private OutputStream out = null;
    private ObjectOutputStream writer = null;
    private Connection_format msg = null;
    private String nom_utilisateur = null;
    private String password = null;

    /**
     * Constructeur de la classe Connect qui permet de créer une connexion serveur/client ou client/serveur.
     *
     * @param PORT            le port sur lequel le serveur écoute
     * @param address         l'adresse du serveur
     * @param nom_utilisateur le nom d'utilisateur
     * @param password        le mot de passe
     * @param exist           si l'utilisateur existe ou non
     * @param data            les données de l'utilisateur
     * @throws IOException
     */
    public Connect(int PORT, InetAddress address, String nom_utilisateur, String password, Boolean exist, ArrayList<String> data) throws IOException {
        this.nom_utilisateur = nom_utilisateur;
        this.password = password;
        try {
            socket = new Socket(address, PORT);
            System.out.println("connecté " + socket);
            out = socket.getOutputStream();
            writer = new ObjectOutputStream(out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ArrayList<Object> message = new ArrayList<>();
        if (exist) {
            // envoie du message "connection"
            message.add(nom_utilisateur);
            message.add(password);
            try {
                send(Connection_Codes.CONNEXION, message);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            // envoie du message "CREATION_UTILISATEUR"
            message.add("nom_utilisateur");
            message.add(nom_utilisateur);
            message.add("motdepasse");
            message.add(password);
            // le mail
            message.add(data.get(0));
            // la description
            message.add(data.get(1));
            try {
                send(Connection_Codes.CREATION_UTILISATEUR, message);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Cette méthode permet d'envoyer un message au serveur/ au client.
     *
     * @param code    le code de la requête
     * @param message le message à envoyer
     * @throws IOException
     */
    public void send(Connection_Codes code, ArrayList<Object> message) throws IOException {
        System.out.println("    -Envoie du message " + code);
        try {
            msg = new Connection_format(code, message);
            writer.writeObject(msg);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Cette méthode permet d'envoyer un message au serveur/ au client.
     *
     * @param code le code de la requête
     * @throws IOException
     */
    public void send(Connection_Codes code) throws IOException {
        System.out.println("    -Envoie du message " + code);
        try {
            msg = new Connection_format(code);
            writer.writeObject(msg);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Cette méthode permet de fermer la connexion.
     */
    public void close() {
        try {
            writer.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Cette méthode permet de récupérer le nom d'utilisateur.
     *
     * @return le nom d'utilisateur
     */
    public String getNom_utilisateur() {
        return nom_utilisateur;
    }

    /**
     * Cette méthode permet de récupérer le mot de passe.
     *
     * @return le mot de passe
     */
    public String getPassword() {
        return password;
    }
}
