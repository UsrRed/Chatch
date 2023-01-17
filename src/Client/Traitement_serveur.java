package Client;

import tools.Connection_Codes;
import tools.Connection_format;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Traitement_serveur extends Thread {

    private Socket socket = null;
    private Interface_Connection fen = null;
    Interface client = null;

    public Traitement_serveur(Socket socket, Interface_Connection fen) {
        this.socket = socket;
        this.fen = fen;
    }

    public void run() {
        Connection_format message = null;
        InputStream inputStream = null;
        Boolean connected = false;
        try {
            System.out.println("Attente message");
            inputStream = socket.getInputStream();
            ObjectInputStream reader = null;
            reader = new ObjectInputStream(inputStream);
            do {
                try {
                    message = (Connection_format) reader.readObject();
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
                // Traitement
                if (message != null) {
                    Connection_Codes code = message.getContenu();
                    ArrayList<Object> annex = message.getAnnex();
                    if (code == Connection_Codes.CONNEXION) {
                        if (annex.get(0).equals("Serveur") && annex.get(1).equals("password")) {
                            System.out.println("Connexion au serveur valide");
                            connected = true;
                        } else {
                            System.out.println("Connexion au serveur invalide");
                        }
                    } else if (connected) {
                        switch (code) {
                            case CONNEXION_OK:
                                System.out.println("Connexion OK");
                                client = new Interface();
                                fen.dispose();
                                break;
                            case CONNEXION_KO:
                                System.out.println("Connexion KO");
                                fen.error("Connection échouée !");
                                break;
                            case DECONNEXION_OK:
                                System.out.println("Deconnexion OK");
                                break;
                            case DECONNEXION_KO:
                                System.out.println("Deconnexion KO");
                                break;
                            case CREATION_DISCUSSION_OK:
                                System.out.println("Creation discussion OK");
                                break;
                            case CREATION_DISCUSSION_KO:
                                System.out.println("Creation discussion KO");
                                break;
                            case SUPPRESSION_DISCUSSION_OK:
                                System.out.println("Suppression discussion OK");
                                break;
                            case SUPPRESSION_DISCUSSION_KO:
                                System.out.println("Suppression discussion KO");
                                break;
                            case MODIFICATION_DISCUSSION_OK:
                                System.out.println("Modification discussion OK");
                                break;
                            case MODIFICATION_DISCUSSION_KO:
                                System.out.println("Modification discussion KO");
                                break;
                            case ENVOI_MESSAGE_OK:
                                System.out.println("Envoi message OK");
                                break;
                            case ENVOI_MESSAGE_KO:
                                System.out.println("Envoi message KO");
                                break;
                            case SUPPRESSION_MESSAGE_OK:
                                System.out.println("Suppression message OK");
                                break;
                            case SUPPRESSION_MESSAGE_KO:
                                System.out.println("Suppression message KO");
                                break;
                            case MODIFICATION_MESSAGE_OK:
                                System.out.println("Modification message OK");
                                break;
                            case MODIFICATION_MESSAGE_KO:
                                System.out.println("Modification message KO");
                                break;
                            case CREATION_GROUPE_OK:
                                System.out.println("Creation groupe OK");
                                break;
                            case CREATION_GROUPE_KO:
                                System.out.println("Creation groupe KO");
                                break;
                            case SUPPRESSION_GROUPE_OK:
                                System.out.println("Suppression groupe OK");
                                break;
                            case SUPPRESSION_GROUPE_KO:
                                System.out.println("Suppression groupe KO");
                                break;
                            case MODIFICATION_GROUPE_OK:
                                System.out.println("Modification groupe OK");
                                break;
                            case MODIFICATION_GROUPE_KO:
                                System.out.println("Modification groupe KO");
                                break;
                            case CREATION_UTILISATEUR_OK:
                                System.out.println("Creation utilisateur OK");
                                fen.cardLayout.show(fen.cards, "Connexion");
                                fen.success("Inscription réussie !\nVous pouvez maintenant vous connecter.");
                                break;
                            case CREATION_UTILISATEUR_KO:
                                System.out.println("Creation utilisateur KO");
                                fen.error("Inscription échouée !\nVeuillez réessayer.");
                                break;
                            case SUPPRESSION_UTILISATEUR_OK:
                                System.out.println("Suppression utilisateur OK");
                                break;
                            case SUPPRESSION_UTILISATEUR_KO:
                                System.out.println("Suppression utilisateur KO");
                                break;
                            case MODIFICATION_UTILISATEUR_OK:
                                System.out.println("Modification utilisateur OK");
                                break;
                            case MODIFICATION_UTILISATEUR_KO:
                                System.out.println("Modification utilisateur KO");
                                break;
                            case CREATION_ADMIN_GROUPE_OK:
                                System.out.println("Creation admin groupe OK");
                                break;
                            case CREATION_ADMIN_GROUPE_KO:
                                System.out.println("Creation admin groupe KO");
                                break;
                            case SUPPRESSION_ADMIN_GROUPE_OK:
                                System.out.println("Suppression admin groupe OK");
                                break;
                            case SUPPRESSION_ADMIN_GROUPE_KO:
                                System.out.println("Suppression admin groupe KO");
                                break;
                            case MODIFICATION_ADMIN_GROUPE_OK:
                                System.out.println("Modification admin groupe OK");
                                break;
                            case MODIFICATION_ADMIN_GROUPE_KO:
                                System.out.println("Modification admin groupe KO");
                                break;
                            default:
                                System.out.println("Code inconnu");
                                break;
                        }
                    } else {
                        System.out.println("Erreur : Serveur non connecté");
                    }
                }
            } while (!message.getContenu().equals(Connection_Codes.DECONNEXION));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("deconnexion");
    }
}
