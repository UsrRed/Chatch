package Serveur;

import tools.Connect;
import tools.Connection_Codes;
import tools.Connection_format;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Traitement_client extends Thread {
    private static final AtomicInteger ID_FACTORY = new AtomicInteger();
    private final int count;
    private int id = -1;
    private Socket socket = null;
    private Database data = null;
    private BDD_Query query = null;
    private int PORT;
    Connect client = null;

    public Traitement_client(Socket socket, Database data, int PORT) {
        count = ID_FACTORY.getAndIncrement();
        System.out.println("Client " + count + " : se connecte !");
        this.PORT = PORT;
        this.socket = socket;
        this.data = data;
        query = new BDD_Query(data);
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
                while (client == null) {
                    try {
                        client = new Connect(PORT + 1, socket.getInetAddress(), "Serveur", "password", true, null);
                        Thread.sleep(1000);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                // Traitement
                if (message != null) {
                    // initialisation des variables
                    Connection_Codes code = message.getContenu();
                    ArrayList<Object> annex = message.getAnnex();
                    if (annex != null) {
                        for (int i = 0; i < annex.size(); i++) {
                            if (annex.get(i) instanceof String) {
                                // sanitize les réponses utilisateur
                                annex.set(i, Objects.requireNonNull(annex.get(i)).toString().replaceAll("[^a-zA-Z0-9_@.]", ""));
                                // ajouter des ' ' autour des Strings
                                if (!annex.get(i).toString().equals("")) {
                                    annex.set(i, "'" + annex.get(i) + "'");
                                }
                            }
                        }
                    }
                    try {
                        ArrayList<String> extract = Extraction(annex);

                        // trie des différents codes
                        if (code == Connection_Codes.CONNEXION) {
                            Afficher("Connexion");
                            query.setQueryAsk("SELECT * FROM utilisateur WHERE nom_utilisateur=" + annex.get(0) + " AND motdepasse=" + annex.get(1) + ";");
                            ArrayList<Object> result = query.getQueryResult();
                            if (result.size() > 0) {
                                client.send(Connection_Codes.CONNEXION_OK);
                                // récupération de l'id de l'utilisateur
                                ArrayList cl = (ArrayList) result.get(0);
                                id = (int) cl.get(0);
                                connected = true;
                            } else {
                                client.send(Connection_Codes.CONNEXION_KO);
                            }
                        } else if (connected) {
                            switch (code) {
                                case CONNEXION:
                                    break;
                                case CREATION_DISCUSSION:
                                    Afficher("Creation discussion");
                                    if (extract != null) {
                                        query.setQueryExecute("INSERT INTO discussion (" + extract.get(0) + ") VALUES(" + extract.get(1) + ");");
                                        query.setQueryAsk("SELECT id_discussion FROM discussion WHERE " + Search(annex) + ";");
                                        ArrayList<Object> result = query.getQueryResult();
                                        if (result.size() > 0) {
                                            int tentatives = 0;
                                            do {
                                                // créer le groupe de discussion et ajoute l'utilisateur
                                                query.setQueryExecute("INSERT INTO groupe_discussion (id_utilisateur, id_discussion) VALUES(" + id + ", " + result.get(0) + " FROM discussion));");
                                                query.setQueryAsk("SELECT id_groupe FROM groupe_discussion WHERE id_utilisateur=" + id + " AND id_discussion=" + result.get(0) + ";");
                                                tentatives++;
                                            } while (query.getQueryResult().size() == 0 && tentatives < 10);
                                            if (query.getQueryResult().size() > 0) {
                                                client.send(Connection_Codes.CREATION_DISCUSSION_OK);
                                            } else {
                                                client.send(Connection_Codes.CREATION_DISCUSSION_KO);
                                                query.setQueryExecute("DELETE FROM discussion WHERE " + Search(annex) + ";");
                                            }
                                        } else {
                                            ArrayList<Object> error = new ArrayList<>();
                                            error.add("Erreur lors de la création de la discussion");
                                            client.send(Connection_Codes.CREATION_DISCUSSION_KO, error);
                                        }
                                    } else {
                                        ArrayList<Object> error = new ArrayList<>();
                                        error.add("Erreur de format");
                                        client.send(Connection_Codes.CREATION_DISCUSSION_KO, error);
                                    }
                                    break;
                                case SUPPRESSION_DISCUSSION:
                                    Afficher("Suppression discussion");
                                    query.setQueryExecute("DELETE FROM discussion WHERE id_discussion = " + annex.get(0) + ";");
                                    query.setQueryAsk("SELECT * FROM discussion WHERE id_discussion = " + annex.get(0) + ";");
                                    if (query.getQueryResult().size() == 0) {
                                        client.send(Connection_Codes.SUPPRESSION_DISCUSSION_OK);
                                    } else {
                                        ArrayList<Object> error = new ArrayList<>();
                                        error.add("Erreur lors de la suppression de la discussion");
                                        client.send(Connection_Codes.SUPPRESSION_DISCUSSION_KO, error);
                                    }
                                    break;
                                case MODIFICATION_DISCUSSION:
                                    Afficher("Modification discussion");
                                    if (extract != null) {
                                        query.setQueryExecute("UPDATE FROM discussion SET colone = " + extract.get(0) + " WHERE id_discussion = " + extract.get(1) + ";");
                                        query.setQueryAsk("SELECT * FROM discussion WHERE " + Search(annex) + ";");
                                        if (query.getQueryResult().size() > 0) {
                                            client.send(Connection_Codes.MODIFICATION_DISCUSSION_OK);
                                        } else {
                                            ArrayList<Object> error = new ArrayList<>();
                                            error.add("Erreur lors de la modification de la discussion");
                                            client.send(Connection_Codes.MODIFICATION_DISCUSSION_KO, error);
                                        }
                                    } else {
                                        ArrayList<Object> error = new ArrayList<>();
                                        error.add("Erreur de format");
                                        client.send(Connection_Codes.MODIFICATION_DISCUSSION_KO, error);
                                    }
                                    break;
                                case ENVOI_MESSAGE:
                                    Afficher("Envoi message");
                                    if (extract != null) {
                                        // voir pour insérer l'objet message et l'id de l'utilisateur (auto)
                                        query.setQueryExecute("INSERT INTO message (" + extract.get(0) + ") VALUES(" + extract.get(1) + ");");
                                        query.setQueryAsk("SELECT id_message FROM message WHERE " + Search(annex) + ";");
                                        if (query.getQueryResult().size() > 0) {
                                            client.send(Connection_Codes.ENVOI_MESSAGE_OK);
                                        } else {
                                            ArrayList<Object> error = new ArrayList<>();
                                            error.add("Erreur lors de l'envoi du message");
                                            client.send(Connection_Codes.ENVOI_MESSAGE_KO, error);
                                        }
                                    } else {
                                        ArrayList<Object> error = new ArrayList<>();
                                        error.add("Erreur de format");
                                        client.send(Connection_Codes.ENVOI_MESSAGE_KO, error);
                                    }
                                    break;
                                case SUPPRESSION_MESSAGE:
                                    Afficher("Suppression message");
                                    query.setQueryExecute("DELETE FROM message WHERE id_utilisateur=" + id + " AND " + Search(annex) + ";");
                                    query.setQueryAsk("SELECT * FROM message WHERE id_utilisateur=" + id + " AND " + Search(annex) + ";");
                                    if (query.getQueryResult().size() == 0) {
                                        client.send(Connection_Codes.SUPPRESSION_MESSAGE_OK);
                                    } else {
                                        ArrayList<Object> error = new ArrayList<>();
                                        error.add("Erreur lors de la suppression du message");
                                        client.send(Connection_Codes.SUPPRESSION_MESSAGE_KO, error);
                                    }
                                    break;
                                case MODIFICATION_MESSAGE:
                                    Afficher("Modification message");
                                    query.setQueryExecute("UPDATE message SET contenu=" + annex.get(0) + " WHERE id_message = " + annex.get(1) + ";");
                                    query.setQueryAsk("SELECT * FROM message WHERE id_message = " + annex.get(1) + ";");
                                    ArrayList<Object> result = query.getQueryResult();
                                    ArrayList<Object> header = query.getQueryHeader();
                                    if (result.size() > 0 && result.get(header.indexOf("contenu")).equals(annex.get(0))) {
                                        client.send(Connection_Codes.MODIFICATION_MESSAGE_OK);
                                    } else {
                                        ArrayList<Object> error = new ArrayList<>();
                                        error.add("Erreur lors de la modification du message");
                                        client.send(Connection_Codes.MODIFICATION_MESSAGE_KO, error);
                                    }
                                    break;
                                case SUPPRESSION_UTILISATEUR:
                                    Afficher("Suppression utilisateur");
                                    query.setQueryExecute("DELETE FROM utilisateur WHERE id_utilisateur= " + id + ";");
                                    query.setQueryAsk("SELECT * FROM utilisateur WHERE id_utilisateur= " + id + ";");
                                    if (query.getQueryResult().size() == 0) {
                                        ArrayList<Object> success = new ArrayList<>();
                                        success.add("Utilisateur supprimé");
                                        client.send(Connection_Codes.SUPPRESSION_UTILISATEUR_OK, success);
                                    } else {
                                        ArrayList<Object> error = new ArrayList<>();
                                        error.add("Erreur lors de la suppression de l'utilisateur");
                                        client.send(Connection_Codes.SUPPRESSION_UTILISATEUR_KO, error);
                                    }
                                    break;
                                case MODIFICATION_UTILISATEUR:
                                    Afficher("Modification utilisateur");
                                    //query.setQueryExecute("UPDATE utilisateur SET nom_de_colonne ="+ extract.get(0) +" WHERE id_utilisateur= "+ extract.get(1) +" ;");
                                    break;
                                case CREATION_ADMIN_DISCUSSION:
                                    Afficher("Creation admin groupe");
                                    //UPDATE groupe_discussion SET role=... WHERE id_utilisateur=... AND id_discussion=... ;
                                    break;
                                case SUPPRESSION_ADMIN_DISCUSSION:
                                    Afficher("Suppression admin groupe");
                                    //UPDATE groupe_discussion SET role= ... WHERE id_utilisateur= ... AND id_discussion= ... ;
                                    break;
                                case MODIFICATION_ADMIN_DISCUSSION:
                                    Afficher("Modification admin groupe");
                                    //UPDATE groupe_discussion SET role = ... WHERE id_utilisateur = ... AND id_discussion= ... ;
                                    break;
                                case RECUPERATION_MESSAGES:
                                    Afficher("Recuperation messages");
                                    // vérifier que l'utilisateur est bien dans la discussion
                                    query.setQueryAsk("SELECT * FROM groupe_discussion WHERE id_discussion=" + annex.get(0) + " AND id_utilisateur=" + id + ";");
                                    if (query.getQueryResult().size() > 0) {
                                        query.setQueryAsk("SELECT * FROM message WHERE id_discussion = " + annex.get(0) + ";");
                                        ArrayList<Object> messages = query.getQueryResult();
                                        if (messages.size() > 0) {
                                            client.send(Connection_Codes.RECUPERATION_MESSAGES_OK, messages);
                                        } else {
                                            ArrayList<Object> error = new ArrayList<>();
                                            error.add("Erreur lors de la récupération des messages");
                                            client.send(Connection_Codes.RECUPERATION_MESSAGES_KO, error);
                                        }
                                    } else {
                                        ArrayList<Object> error = new ArrayList<>();
                                        error.add("Vous n'êtes pas dans cette discussion");
                                        client.send(Connection_Codes.RECUPERATION_MESSAGES_KO, error);
                                    }
                                    break;
                                case RECUPERATION_DISCUSSIONS:
                                    Afficher("Recuperation discussions");
                                    query.setQueryAsk("SELECT * FROM discussion d, groupe_discussion g WHERE g.id_utilisateur = " + id + " AND g.id_discussion=d.id_discussion;");
                                    ArrayList<Object> discussions = query.getQueryResult();
                                    if (discussions.size() > 0) {
                                        client.send(Connection_Codes.RECUPERATION_DISCUSSIONS_OK, discussions);
                                    } else {
                                        ArrayList<Object> error = new ArrayList<>();
                                        error.add("Erreur lors de la récupération des discussions");
                                        client.send(Connection_Codes.RECUPERATION_DISCUSSIONS_KO, error);
                                    }
                                    break;
                                case RECUPERATION_UTILISATEURS:
                                    Afficher("Recuperation utilisateurs");
                                    // vérifier que l'utilisateur est bien dans la discussion
                                    query.setQueryAsk("SELECT * FROM groupe_discussion WHERE id_discussion=" + annex.get(0) + " AND id_utilisateur=" + id + ";");
                                    if (query.getQueryResult().size() > 0) {
                                        query.setQueryAsk("SELECT u.nom_utilisateur, u.description_utilisateur, u.photo_utilisateur FROM utilisateur u, groupe_discussion g WHERE g.id_discussion = " + annex.get(0) + " AND g.id_utilisateur=u.id_utilisateur;");
                                        ArrayList<Object> utilisateurs = query.getQueryResult();
                                        if (utilisateurs.size() > 0) {
                                            client.send(Connection_Codes.RECUPERATION_UTILISATEURS_OK, utilisateurs);
                                        } else {
                                            ArrayList<Object> error = new ArrayList<>();
                                            error.add("Erreur lors de la récupération des utilisateurs");
                                            client.send(Connection_Codes.RECUPERATION_UTILISATEURS_KO, error);
                                        }
                                    } else {
                                        ArrayList<Object> error = new ArrayList<>();
                                        error.add("Vous n'êtes pas dans cette discussion");
                                        client.send(Connection_Codes.RECUPERATION_UTILISATEURS_KO, error);
                                    }
                                    break;
                                default:
                                    Afficher("Code inconnu");
                                    break;
                            }
                        } else if (code == Connection_Codes.CREATION_UTILISATEUR) {
                            Afficher("Creation utilisateur");
                            if (extract != null) {
                                // On vérifie que l'utilisateur n'existe pas déjà
                                query.setQueryAsk("SELECT * FROM utilisateur WHERE nom_utilisateur = " + annex.get(1) + ";");
                                if (query.getQueryResult().size()==0){
                                    query.setQueryExecute("INSERT INTO utilisateur (" + extract.get(0) + ") VALUES (" + extract.get(1) + ");");
                                    query.setQueryAsk("SELECT * FROM utilisateur WHERE " + Search(annex) + ";");
                                    if (query.getQueryResult().size() != 0) {
                                        ArrayList<Object> success = new ArrayList<>();
                                        success.add("Le compte a bien été créé");
                                        client.send(Connection_Codes.CREATION_UTILISATEUR_OK, success);
                                    } else {
                                        ArrayList<Object> error = new ArrayList<>();
                                        error.add("Erreur lors de la création de l'utilisateur");
                                        client.send(Connection_Codes.CREATION_UTILISATEUR_KO, error);
                                    }
                                } else {
                                    ArrayList<Object> error = new ArrayList<>();
                                    error.add("L'utilisateur existe déjà");
                                    client.send(Connection_Codes.CREATION_UTILISATEUR_KO, error);
                                }

                            } else {
                                ArrayList<Object> error = new ArrayList<>();
                                error.add("Erreur de format");
                                client.send(Connection_Codes.CREATION_UTILISATEUR_KO, error);
                            }
                        } else {
                            Afficher("Non connecté tente de faire une action");
                            ArrayList<Object> sended = new ArrayList<>();
                            sended.add("Vous n'êtes pas connecté");
                            client.send(Connection_Codes.CONNEXION_KO, sended);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } while (!Objects.requireNonNull(message).getContenu().equals(Connection_Codes.DECONNEXION));
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

    public ArrayList<String> Extraction(ArrayList<Object> annex) {
        if (annex != null) {
            if(annex.stream().allMatch(String.class::isInstance)){
                if (annex.size() % 2 == 0) {
                    String txt = "";
                    String txt2 = "";
                    for (int i = 0; i < annex.size() / 2; i++) {
                        if (i == annex.size() / 2 - 1) {
                            txt += sanitize((String) annex.get(i * 2));
                            txt2 += "'" + sanitize((String) annex.get(i * 2 + 1)) + "'";
                        } else {
                            txt += sanitize((String) annex.get(i * 2)) + ", ";
                            txt2 += "'" + sanitize((String) annex.get(i * 2 + 1)) + "', ";
                        }
                    }
                    ArrayList<String> returned = new ArrayList<>();
                    returned.add(txt);
                    returned.add(txt2);
                    return returned;
                } else {
                    return null;
                }
            } else{
                return null;
            }
        } else {
            return null;
        }

    }
    public String Search(ArrayList<Object> annex){
        if (annex != null) {
            if(annex.stream().allMatch(String.class::isInstance)){
                if (annex.size() % 2 == 0) {
                    String txt = "";
                    for (int i = 0; i < annex.size() / 2; i++) {
                        if (i == annex.size() / 2 - 1) {
                            txt += sanitize((String) annex.get(i * 2)) + " = '" + sanitize((String) annex.get(i * 2 + 1)) + "'";
                        } else {
                            txt += sanitize((String) annex.get(i * 2)) + " = '" + sanitize((String) annex.get(i * 2 + 1)) + "' AND ";
                        }
                    }
                    return txt;
                } else {
                    return null;
                }
            } else{
                return null;
            }
        } else {
            return null;
        }
    }

    // methode sanitize sql
    public String sanitize(String str) {
        return str.replaceAll("[^a-zA-Z0-9_@.]", "");
    }

    public void Afficher(String text) {
        if (id != -1) {
            System.out.println("client " + count + " (ID" + id + ") : " + text);
        } else {
            System.out.println("client " + count + " (not connected) : " + text);
        }

    }
}
