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
                    Boolean res = false;
                    try {
                        ArrayList<String> extract = Extraction(annex);
                        System.out.println("Extraction : \n" + extract.get(0) + "\n" + extract.get(1));

                        // trie des différents codes
                        if (code == Connection_Codes.CONNEXION) {
                            Afficher("Connexion");
                            query.setQueryAsk("SELECT * FROM utilisateur WHERE nom_utilisateur=" + annex.get(0) + " AND motdepasse=" + annex.get(1) + ";");
                            ArrayList<Object> result = query.getQueryResult();
                            System.out.println(res);
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
                                        res = query.setQueryExecute("INSERT INTO discussion (" + extract.get(0) + ") VALUES(" + extract.get(1) + ");");
                                        if (res) {
                                            client.send(Connection_Codes.CREATION_DISCUSSION_OK);
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
                                    res = query.setQueryExecute("DELETE FROM discussion WHERE id_discussion = " + annex.get(0) + ";");
                                    if (res) {
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
                                        res = query.setQueryExecute("UPDATE FROM discussion SET colone = " + extract.get(0) + " WHERE id_discussion = " + extract.get(1) + ";");
                                        if (res) {
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
                                        res = query.setQueryExecute("INSERT INTO message (" + extract.get(0) + ") VALUES(" + extract.get(1) + ");");
                                        if (res) {
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
                                    res = query.setQueryExecute("DELETE FROM message WHERE id_message=" + annex.get(0) + ";");
                                    if (res) {
                                        client.send(Connection_Codes.SUPPRESSION_MESSAGE_OK);
                                    } else {
                                        ArrayList<Object> error = new ArrayList<>();
                                        error.add("Erreur lors de la suppression du message");
                                        client.send(Connection_Codes.SUPPRESSION_MESSAGE_KO, error);
                                    }
                                    break;
                                case MODIFICATION_MESSAGE:
                                    Afficher("Modification message");
                                    if (extract != null) {
                                        res = query.setQueryExecute("UPDATE message SET contenu=" + extract.get(0) + " WHERE id_message = " + extract.get(1) + ";");
                                        if (res) {
                                            client.send(Connection_Codes.MODIFICATION_MESSAGE_OK);
                                        } else {
                                            ArrayList<Object> error = new ArrayList<>();
                                            error.add("Erreur lors de la modification du message");
                                            client.send(Connection_Codes.MODIFICATION_MESSAGE_KO, error);
                                        }
                                    } else {
                                        ArrayList<Object> error = new ArrayList<>();
                                        error.add("Erreur de format");
                                        client.send(Connection_Codes.MODIFICATION_MESSAGE_KO, error);
                                    }
                                    break;
                                case CREATION_GROUPE:
                                    Afficher("Creation groupe");
                                    if (extract != null) {
                                        res = query.setQueryExecute("INSERT INTO groupe_discussion (" + extract.get(0) + ") VALUES (" + extract.get(1) + ");");
                                        if (res) {
                                            client.send(Connection_Codes.CREATION_GROUPE_OK);
                                        } else {
                                            ArrayList<Object> error = new ArrayList<>();
                                            error.add("Erreur lors de la création du groupe");
                                            client.send(Connection_Codes.CREATION_GROUPE_KO, error);
                                        }
                                    } else {
                                        ArrayList<Object> error = new ArrayList<>();
                                        error.add("Erreur de format");
                                        client.send(Connection_Codes.CREATION_GROUPE_KO, error);
                                    }
                                    break;
                                case SUPPRESSION_GROUPE:
                                    Afficher("Suppression groupe");
                                    query.setQueryExecute("DELETE FROM groupe_discussion WHERE id_discussion=" + annex.get(0) + "... ;");
                                    break;
                                case MODIFICATION_GROUPE:
                                    Afficher("Modification groupe");
                                    //query.setQueryAsk("UPDATE FROM groupe_discussion SET colone = "+ extract.get(0) +" WHERE id_discussion="+ extract.get(1) +";");
                                    break;
                                case SUPPRESSION_UTILISATEUR:
                                    Afficher("Suppression utilisateur");
                                    res = query.setQueryExecute("DELETE FROM utilisateur WHERE id_utilisateur= " + annex.get(0) + ";");
                                    if (res) {
                                        client.send(Connection_Codes.SUPPRESSION_UTILISATEUR_OK);
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
                                case CREATION_ADMIN_GROUPE:
                                    Afficher("Creation admin groupe");
                                    //UPDATE groupe_discussion SET role=... WHERE id_utilisateur=... AND id_discussion=... ;
                                    break;
                                case SUPPRESSION_ADMIN_GROUPE:
                                    Afficher("Suppression admin groupe");
                                    //UPDATE groupe_discussion SET role= ... WHERE id_utilisateur= ... AND id_discussion= ... ;
                                    break;
                                case MODIFICATION_ADMIN_GROUPE:
                                    Afficher("Modification admin groupe");
                                    //UPDATE groupe_discussion SET role = ... WHERE id_utilisateur = ... AND id_discussion= ... ;
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
                                    Afficher("Extraction2 : \n" + extract.get(0) + "\n" + extract.get(1));
                                    query.setQueryExecute("INSERT INTO utilisateur (" + extract.get(0) + ") VALUES (" + extract.get(1) + ");");
                                    query.setQueryAsk("SELECT * FROM utilisateur WHERE " + Search(annex) + ";");
                                    if (query.getQueryResult().size() != 0) {
                                        client.send(Connection_Codes.CREATION_UTILISATEUR_OK);
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
