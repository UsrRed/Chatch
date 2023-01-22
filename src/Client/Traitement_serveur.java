package Client;

import tools.Connection_Codes;
import tools.Connection_format;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
/**
 * @author : Eliott LEBOSSE et Yohann DENOYELLE
 * Cette classe Threadé permet de traiter les messages entrants du serveur. (réponses aux requetes)
 * Ainsi il traite les réponses recus et interagit avec l'interface graphique.
 */
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
                                Thread_Client.connexion.send(Connection_Codes.RECUPERATION_DISCUSSIONS);
                                if (annex.size() > 0) {
                                    client.success((String) annex.get(0));
                                }
                                break;
                            case CONNEXION_KO:
                                System.out.println("Connexion KO");
                                if (annex != null) {
                                    System.out.println(annex.get(0));
                                } else {
                                    fen.error("Connection échouée !");
                                }
                                break;
                            case DECONNEXION_OK:
                                System.out.println("Deconnexion OK");
                                client.dispose();
                                Thread_Client.connexion.close();
                                InetAddress host = socket.getInetAddress();
                                int port = socket.getPort();
                                this.socket.close();
                                new Interface_Connection(host, port);
                                break;
                            case DECONNEXION_KO:
                                System.out.println("Deconnexion KO");
                                client.dispose();
                                Thread_Client.connexion.close();
                                this.socket.close();
                                break;
                            case CREATION_DISCUSSION_OK:
                                System.out.println("Creation discussion OK");
                                if (annex != null) {
                                    // client.addDiscussion((String) annex.get(0));
                                }
                                break;
                            case CREATION_DISCUSSION_KO:
                                System.out.println("Creation discussion KO");
                                if (annex != null) {
                                    client.error((String) annex.get(0));
                                } else {
                                    client.error("Création de discussion échouée !");
                                }
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
                                client.addMessage((ArrayList<Object>) annex.get(0));
                                break;
                            case ENVOI_MESSAGE_KO:
                                System.out.println("Envoi message KO");
                                client.error(annex.get(0).toString());
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
                            case CREATION_UTILISATEUR_OK:
                                System.out.println("Creation utilisateur OK");
                                fen.cardLayout.show(fen.cards, "Connexion");
                                if (annex != null) {
                                    fen.success(annex.get(0).toString());
                                }
                                break;
                            case CREATION_UTILISATEUR_KO:
                                System.out.println("Creation utilisateur KO");
                                if (annex != null) {
                                    fen.error(annex.get(0).toString());
                                } else {
                                    fen.error("Erreur lors de la création de l'utilisateur");
                                }
                                break;
                            case SUPPRESSION_UTILISATEUR_OK:
                                System.out.println("Suppression utilisateur OK");
                                Thread_Client.connexion.send(Connection_Codes.DECONNEXION);
                                client.dispose();
                                break;
                            case SUPPRESSION_UTILISATEUR_KO:
                                System.out.println("Suppression utilisateur KO");
                                if (annex != null) {
                                    fen.error(annex.get(0).toString());
                                } else {
                                    fen.error("Erreur lors de la suppression de l'utilisateur");
                                }
                                break;
                            case MODIFICATION_UTILISATEUR_OK:
                                System.out.println("Modification utilisateur OK");
                                if (annex != null) {
                                    fen.success(annex.get(0).toString());
                                }
                                break;
                            case MODIFICATION_UTILISATEUR_KO:
                                System.out.println("Modification utilisateur KO");
                                if (annex != null) {
                                    fen.error(annex.get(0).toString());
                                } else {
                                    fen.error("Erreur lors de la modification de l'utilisateur");
                                }
                                break;
                            case CREATION_ADMIN_DISCUSSION_OK:
                                System.out.println("Creation admin groupe OK");
                                break;
                            case CREATION_ADMIN_DISCUSSION_KO:
                                System.out.println("Creation admin groupe KO");
                                break;
                            case SUPPRESSION_ADMIN_DISCUSSION_OK:
                                System.out.println("Suppression admin groupe OK");
                                break;
                            case SUPPRESSION_ADMIN_DISCUSSION_KO:
                                System.out.println("Suppression admin groupe KO");
                                break;
                            case MODIFICATION_ADMIN_DISCUSSION_OK:
                                System.out.println("Modification admin groupe OK");
                                break;
                            case MODIFICATION_ADMIN_DISCUSSION_KO:
                                System.out.println("Modification admin groupe KO");
                                break;
                            case RECUPERATION_MESSAGES_OK:
                                System.out.println("Recuperation messages OK");
                                client.setMessages(annex);
                                break;
                            case RECUPERATION_MESSAGES_KO:
                                System.out.println("Recuperation messages KO");
                                break;
                            case RECUPERATION_DISCUSSIONS_OK:
                                System.out.println("Recuperation discussions OK");
                                client.setDiscussions(annex);
                                break;
                            case RECUPERATION_DISCUSSIONS_KO:
                                System.out.println("Recuperation discussions KO");
                                client.error("Erreur lors de la récupération des discussions");
                                break;
                            case RECUPERATION_DISCUSSION_OK:
                                System.out.println("Recuperation discussion OK");
                                client.set_discussion(annex);
                                break;
                            case RECUPERATION_DISCUSSION_KO:
                                System.out.println("Recuperation discussion KO");
                                if (annex != null) {
                                    client.error(annex.get(0).toString());
                                } else {
                                    client.error("Erreur lors de la récupération de la discussion");
                                }
                                break;
                            case RECUPERATION_UTILISATEURS_OK:
                                System.out.println("Recuperation utilisateur OK");
                                break;
                            case RECUPERATION_UTILISATEURS_KO:
                                System.out.println("Recuperation utilisateur KO");
                                client.error(annex.get(0).toString());
                                break;
                            case AJOUT_UTILISATEUR_DISCUSSION_OK:
                                System.out.println("Ajout utilisateur discussion OK");
                                if (annex != null) {
                                    client.success(annex.get(0).toString());
                                } else {
                                    client.success("Utilisateur ajouté à la discussion");
                                }
                                break;
                            case AJOUT_UTILISATEUR_DISCUSSION_KO:
                                System.out.println("Ajout utilisateur discussion KO");
                                if (annex != null) {
                                    client.error(annex.get(0).toString());
                                } else {
                                    client.error("Erreur lors de l'ajout de l'utilisateur à la discussion");
                                }
                                break;
                            case SUPPRESSION_UTILISATEUR_DISCUSSION_OK:
                                System.out.println("Suppression utilisateur discussion OK");
                                if (annex != null) {
                                    client.success(annex.get(0).toString());
                                } else {
                                    client.success("Utilisateur supprimé de la discussion");
                                }
                                break;
                            case SUPPRESSION_UTILISATEUR_DISCUSSION_KO:
                                System.out.println("Suppression utilisateur discussion KO");
                                if (annex != null) {
                                    client.error(annex.get(0).toString());
                                } else {
                                    client.error("Erreur lors de la suppression de l'utilisateur de la discussion");
                                }
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
