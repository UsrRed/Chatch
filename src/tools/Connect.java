package tools;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;


public class Connect {
    private Socket socket = null;
    private OutputStream out = null;
    private ObjectOutputStream writer = null;
    private Connection_format msg = null;
    private String nom_utilisateur = null;
    private String password = null;

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

    public void close() {
        try {
            writer.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getNom_utilisateur() {
        return nom_utilisateur;
    }

    public String getPassword() {
        return password;
    }
}
