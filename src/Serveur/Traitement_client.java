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
    private BDD_Query query = null;
    private int PORT;
    Connect client = null;
    private String regex = "[^a-zA-Z0-9_@. !'$£%&*()\\-+=?;:,<>#{}\\[\\]~`^|]";

    public Traitement_client(Socket socket, Database data, int PORT) {
        count = ID_FACTORY.getAndIncrement();
        System.out.println("Client " + count + " : se connecte !");
        this.PORT = PORT;
        this.socket = socket;
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
                    Afficher(e.getMessage());
                    break;
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
                    ArrayList<String> extract = Extraction(annex);
                    ArrayList<Object> result = null;
                    ArrayList<Object> header = null;
                    ArrayList<Object> roles = null;
                    if (annex != null) {
                        for (int i = 0; i < annex.size(); i++) {
                            if (annex.get(i) instanceof String) {
                                // sanitize les réponses utilisateur
                                annex.set(i, Objects.requireNonNull(annex.get(i)).toString().replaceAll(regex, ""));
                            }
                        }
                    }
                    try {
                        Afficher("Message reçu : code{" + code + "} annex{" + annex + "} extract{" + extract + "} Search{" + Search(annex) + "}");
                        // trie des différents codes
                        if (code == Connection_Codes.CONNEXION) {
                            query.setQueryAsk("SELECT * FROM utilisateur WHERE nom_utilisateur=\"" + annex.get(0) + "\" AND motdepasse=\"" + annex.get(1) + "\";");
                            result = query.getQueryResult();
                            if (result.size() > 0) {
                                // récupération de l'id de l'utilisateur
                                ArrayList cl = (ArrayList) result.get(0);
                                id = (int) cl.get(0);
                                // insertion de la date de connection
                                ArrayList<Object> bvn = new ArrayList<>();
                                query.setQueryAsk("SELECT * FROM connexions WHERE id_utilisateur=" + id + ";");
                                result = query.getQueryResult();
                                if (result.size() == 0) {
                                    // l'utilisateur n'a jamais été connecté
                                    query.setQueryAsk("SELECT * FROM connexions WHERE adresse_ip=\"" + socket.getInetAddress() + "\";");
                                    result = query.getQueryResult();
                                    if (result.size() == 0) {
                                        // l'adresse ip n'a jamais été utilisée : nouvel utilisateur
                                        query.setQueryExecute("INSERT INTO connexions (id_utilisateur, adresse_ip) VALUES (" + id + ", \"" + socket.getInetAddress() + "\");");
                                        bvn.add("Bienvenue " + annex.get(0) + " !\n" +
                                                "Vous pouvez découvrir toutes les fonctionnalités de l'application sur le README.\n" +
                                                "Pour débuter vous pouvez appuyer sur le \"+\" afin de crée une discussion.\n" +
                                                "Vous pourrez ensuite avec le menu à droite ajouter des personnes à votre discussion.\n" +
                                                "Sachez que vous avez été ajouté à la base de donnée.\n" +
                                                "Votre adresse ip est : " + socket.getInetAddress() + "\n" +
                                                "Vous êtes l'utilisateur n°" + id);
                                    } else {
                                        // l'adresse ip a déjà été utilisée : multicompte
                                        query.setQueryExecute("INSERT INTO connexions (id_utilisateur, adresse_ip) VALUES (" + id + ", \"" + socket.getInetAddress() + "\");");
                                        bvn.add("Bienvenue " + annex.get(0) + " !\n" +
                                                "Vous avez créer un nouveau compte.\n" +
                                                "Vous avez été ajouté à la base de donnée.\n" +
                                                "Votre adresse ip est : " + socket.getInetAddress() + "\n" +
                                                "Vous êtes l'utilisateur n°" + id);
                                    }
                                } else {
                                    // l'utilisateur a déjà été connecté
                                    query.setQueryAsk("SELECT * FROM connexions WHERE adresse_ip=\"" + socket.getInetAddress() + "\";");
                                    result = query.getQueryResult();
                                    if (result.size() > 0) {
                                        // l'adresse ip est déjà utilisée : connection classique
                                        query.setQueryExecute("UPDATE connexions SET id_utilisateur=" + id + " WHERE adresse_ip=\"" + socket.getInetAddress() + "\";");
                                    } else {
                                        // l'adresse ip n'est pas utilisée : connection depuis un nouvel appareil
                                        query.setQueryExecute("INSERT INTO connexions (id_utilisateur, adresse_ip) VALUES (" + id + ", \"" + socket.getInetAddress() + "\");");
                                        bvn.add("Bienvenue " + annex.get(0) + " !\n" +
                                                "Vous vous êtes connecté depuis un nouvel appareil.\n" +
                                                "Votre adresse ip est : " + socket.getInetAddress() + "\n" +
                                                "Vous êtes l'utilisateur n°" + id);
                                    }
                                }
                                query.setQueryExecute("INSERT INTO connexions (id_utilisateur, adresse_ip) VALUES (" + id + ", \"" + socket.getInetAddress() + "\");");
                                connected = true;
                                client.send(Connection_Codes.CONNEXION_OK, bvn);
                            } else {
                                client.send(Connection_Codes.CONNEXION_KO);
                            }
                        } else if (connected) {
                            switch (code) {
                                case CONNEXION:
                                    break;
                                case CREATION_DISCUSSION:
                                    if (annex != null) {
                                        query.setQueryAsk("SELECT id_discussion FROM discussion WHERE nom_discussion=\"" + annex.get(0) + "\";");
                                        if (query.getQueryResult().size() == 0) {
                                            query.setQueryExecute("INSERT INTO discussion (nom_discussion) VALUES(\"" + annex.get(0) + "\");");
                                            query.setQueryAsk("SELECT id_discussion FROM discussion WHERE nom_discussion=\"" + annex.get(0) + "\";");
                                            result = (ArrayList<Object>) query.getQueryResult().get(0);
                                            if (result.size() > 0) {
                                                int tentatives = 0;
                                                do {
                                                    // créer le groupe de discussion et ajoute l'utilisateur
                                                    query.setQueryExecute("INSERT INTO groupe_discussion (id_utilisateur, id_discussion, role) VALUES(" + id + ", " + result.get(0) + ", 3);");
                                                    query.setQueryAsk("SELECT id_groupe FROM groupe_discussion WHERE id_utilisateur=" + id + " AND id_discussion=" + result.get(0) + ";");
                                                    tentatives++;
                                                } while (query.getQueryResult().size() == 0 && tentatives < 10);
                                                System.out.println(query.getQueryResult());
                                                if (query.getQueryResult().size() > 0) {
                                                    ArrayList<Object> res = new ArrayList<>();
                                                    res.add(annex.get(0));
                                                    client.send(Connection_Codes.CREATION_DISCUSSION_OK, res);
                                                } else {
                                                    client.send(Connection_Codes.CREATION_DISCUSSION_KO);
                                                    query.setQueryExecute("DELETE FROM discussion WHERE id_discussion=" + result.get(0) + ";");
                                                }
                                            } else {
                                                ArrayList<Object> error = new ArrayList<>();
                                                error.add("Erreur lors de la création de la discussion");
                                                client.send(Connection_Codes.CREATION_DISCUSSION_KO, error);
                                            }
                                        } else {
                                            ArrayList<Object> error = new ArrayList<>();
                                            error.add("La discussion existe déjà");
                                            client.send(Connection_Codes.CREATION_DISCUSSION_KO, error);
                                        }
                                    }

                                    break;
                                case SUPPRESSION_DISCUSSION:
                                    query.setQueryExecute("DELETE FROM groupe_discussion WHERE id_discussion=" + annex.get(0) + ";");
                                    query.setQueryExecute("DELETE FROM message WHERE id_discussion=" + annex.get(0) + ";");
                                    query.setQueryExecute("DELETE FROM discussion WHERE id_discussion=" + annex.get(0) + ";");
                                    query.setQueryAsk("SELECT * FROM discussion WHERE id_discussion=" + annex.get(0) + ";");
                                    if (query.getQueryResult().size() == 0) {
                                        client.send(Connection_Codes.SUPPRESSION_DISCUSSION_OK);
                                    } else {
                                        ArrayList<Object> error = new ArrayList<>();
                                        error.add("Erreur lors de la suppression de la discussion");
                                        client.send(Connection_Codes.SUPPRESSION_DISCUSSION_KO, error);
                                    }
                                    break;
                                case MODIFICATION_DISCUSSION:
                                    if (annex != null) {
                                        query.setQueryExecute("UPDATE FROM discussion SET colone=" + annex.get(0) + " WHERE id_discussion=" + annex.get(1) + ";");
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
                                    if (extract != null) {
                                        // get nom_utilisateur
                                        query.setQueryAsk("SELECT nom_utilisateur FROM utilisateur WHERE id_utilisateur=" + id + ";");
                                        ArrayList temp_result = (ArrayList) query.getQueryResult().get(0);
                                        String nom_utilisateur = (String) temp_result.get(0);
                                        // voir pour insérer l'objet message et l'id de l'utilisateur et son nom (auto)
                                        query.setQueryExecute("INSERT INTO message (id_utilisateur, nom_utilisateur, " + extract.get(0) + ") VALUES(" + id + ", \"" + nom_utilisateur + "\", " + extract.get(1) + ");");
                                        query.setQueryAsk("SELECT * FROM message WHERE id_utilisateur=" + id + " AND nom_utilisateur=\"" + nom_utilisateur + "\" AND " + Search(annex) + ";");
                                        if (query.getQueryResult().size() > 0) {
                                            System.out.println(query.getQueryResult());
                                            result = query.getQueryResult();
                                            client.send(Connection_Codes.ENVOI_MESSAGE_OK, result);
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
                                    query.setQueryExecute("UPDATE message SET contenu=\"" + annex.get(0) + "\" WHERE id_message=" + annex.get(1) + ";");
                                    query.setQueryAsk("SELECT * FROM message WHERE id_message=" + annex.get(1) + ";");
                                    result = query.getQueryResult();
                                    header = query.getQueryHeader();
                                    if (result.size() > 0 && result.get(header.indexOf("contenu")).equals(annex.get(0))) {
                                        client.send(Connection_Codes.MODIFICATION_MESSAGE_OK);
                                    } else {
                                        ArrayList<Object> error = new ArrayList<>();
                                        error.add("Erreur lors de la modification du message");
                                        client.send(Connection_Codes.MODIFICATION_MESSAGE_KO, error);
                                    }
                                    break;
                                case SUPPRESSION_UTILISATEUR:
                                    query.setQueryExecute("DELETE FROM groupe_discussion WHERE id_utilisateur=" + id + ";");
                                    query.setQueryExecute("DELETE FROM connexions WHERE id_utilisateur=" + id + ";");
                                    query.setQueryAsk("SELECT nom_utilisateur FROM utilisateur WHERE id_utilisateur=" + id + ";");
                                    result = (ArrayList<Object>) query.getQueryResult().get(0);
                                    query.setQueryExecute("UPDATE message SET id_utilisateur=1, nom_utilisateur=\"(deleted account) " + result.get(0) + "\" WHERE id_utilisateur=" + id + ";");
                                    query.setQueryExecute("DELETE FROM utilisateur WHERE id_utilisateur=" + id + ";");
                                    query.setQueryAsk("SELECT * FROM utilisateur WHERE id_utilisateur=" + id + ";");
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
                                    if (extract != null) {
                                        Boolean isOK = true;
                                        // on vérifie la validité des arguments
                                        for (int i = 0; i < annex.size() / 2; i++) {
                                            switch ((String) annex.get(i * 2)) {
                                                case "nom_utilisateur":
                                                    query.setQueryAsk("SELECT * FROM utilisateur WHERE nom_utilisateur=\"" + annex.get(i * 2 + 1) + "\";");
                                                    if (!(query.getQueryResult().size() == 0)) {
                                                        ArrayList<Object> error = new ArrayList<>();
                                                        error.add("Nom d'utilisateur déjà utilisé");
                                                        client.send(Connection_Codes.MODIFICATION_UTILISATEUR_KO, error);
                                                        isOK = false;
                                                    }
                                                    break;
                                                case "motdepasse":
                                                    if (annex.get(i * 2 + 1).toString().length() < 4) {
                                                        ArrayList<Object> error = new ArrayList<>();
                                                        error.add("Mot de passe trop court");
                                                        client.send(Connection_Codes.MODIFICATION_UTILISATEUR_KO, error);
                                                        isOK = false;
                                                    }
                                                    break;
                                                case "adresse_email":
                                                    query.setQueryAsk("SELECT * FROM utilisateur WHERE adresse_email=\"" + annex.get(i * 2 + 1) + "\";");
                                                    if (!(query.getQueryResult().size() == 0)) {
                                                        ArrayList<Object> error = new ArrayList<>();
                                                        error.add("Email déjà utilisé");
                                                        client.send(Connection_Codes.MODIFICATION_UTILISATEUR_KO, error);
                                                        isOK = false;
                                                    }
                                                    if (!(annex.get(i * 2 + 1).toString().contains("@") && annex.get(i * 2 + 1).toString().contains("."))) {
                                                        ArrayList<Object> error = new ArrayList<>();
                                                        error.add("Email invalide");
                                                        client.send(Connection_Codes.MODIFICATION_UTILISATEUR_KO, error);
                                                        isOK = false;
                                                    }
                                                    break;
                                                case "description_utilisateur":
                                                    // pas de critères particuliers
                                                    break;
                                                case "photo_utilisateur":
                                                    // TODO
                                                    break;
                                                default:
                                                    ArrayList<Object> error = new ArrayList<>();
                                                    error.add("Erreur de format");
                                                    client.send(Connection_Codes.MODIFICATION_UTILISATEUR_KO, error);
                                                    isOK = false;
                                                    break;
                                            }
                                        }
                                        if (isOK) {
                                            // on modifie les données
                                            query.setQueryExecute("UPDATE utilisateur SET " + SET(annex) + " WHERE id_utilisateur=" + id + ";");
                                            // on vérifie que les données ont bien été modifiées
                                            query.setQueryAsk("SELECT * FROM utilisateur WHERE id_utilisateur=" + id + " AND " + Search(annex) + ";");
                                            if (query.getQueryResult().size() > 0) {
                                                ArrayList<Object> success = new ArrayList<>();
                                                success.add("Utilisateur modifié");
                                                client.send(Connection_Codes.MODIFICATION_UTILISATEUR_OK, success);
                                            } else {
                                                ArrayList<Object> error = new ArrayList<>();
                                                error.add("Erreur lors de la modification de l'utilisateur");
                                                client.send(Connection_Codes.MODIFICATION_UTILISATEUR_KO, error);
                                            }
                                        } else {
                                            ArrayList<Object> error = new ArrayList<>();
                                            error.add("Erreur lors de la modification de l'utilisateur");
                                            client.send(Connection_Codes.MODIFICATION_UTILISATEUR_KO, error);
                                        }
                                    } else {
                                        ArrayList<Object> error = new ArrayList<>();
                                        error.add("Erreur de format");
                                        client.send(Connection_Codes.MODIFICATION_UTILISATEUR_KO, error);
                                    }
                                    break;
                                case CREATION_ADMIN_DISCUSSION:
                                    //UPDATE groupe_discussion SET role=... WHERE id_utilisateur=... AND id_discussion=... ;
                                    break;
                                case SUPPRESSION_ADMIN_DISCUSSION:
                                    //UPDATE groupe_discussion SET role= ... WHERE id_utilisateur= ... AND id_discussion= ... ;
                                    break;
                                case MODIFICATION_ADMIN_DISCUSSION:
                                    //UPDATE groupe_discussion SET role = ... WHERE id_utilisateur = ... AND id_discussion= ... ;
                                    break;
                                case RECUPERATION_MESSAGES:
                                    // vérifier que l'utilisateur est bien dans la discussion
                                    query.setQueryAsk("SELECT * FROM groupe_discussion WHERE id_discussion=" + annex.get(0) + " AND id_utilisateur=" + id + ";");
                                    if (query.getQueryResult().size() > 0) {
                                        query.setQueryAsk("SELECT * FROM message WHERE id_discussion=" + annex.get(0) + ";");
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
                                    query.setQueryAsk("SELECT d.nom_discussion, d.id_discussion, d.photo_discussion FROM discussion d, groupe_discussion g WHERE g.id_utilisateur=" + id + " AND g.id_discussion=d.id_discussion;");
                                    ArrayList<Object> discussions = query.getQueryResult();
                                    client.send(Connection_Codes.RECUPERATION_DISCUSSIONS_OK, discussions);
                                    break;
                                case RECUPERATION_DISCUSSION:
                                    // récupère les données de la discussion
                                    // fournie un id de discussion
                                    // Nom du channel
                                    // Nombre de messages
                                    // Liste des membres
                                    if (annex.size() == 1) {
                                        // vérifier que l'utilisateur est bien dans la discussion et récupérer l'id de la discussion
                                        query.setQueryAsk("SELECT * FROM groupe_discussion WHERE id_discussion=" + annex.get(0) + " AND id_utilisateur=" + id + ";");
                                        if (query.getQueryResult().size() > 0) {
                                            int id_discussion = -1;
                                            try {
                                                id_discussion = (int) annex.get(0);
                                            } catch (Exception e) {
                                                ArrayList<Object> error = new ArrayList<>();
                                                error.add("Erreur de format");
                                                client.send(Connection_Codes.RECUPERATION_DISCUSSION_KO, error);
                                                break;
                                            }
                                            // récupérer le nom de la discussion
                                            query.setQueryAsk("SELECT nom_discussion FROM discussion WHERE id_discussion=" + id_discussion + ";");
                                            ArrayList<Object> nom_discussions = (ArrayList<Object>) query.getQueryResult().get(0);
                                            String nom_discussion = (String) nom_discussions.get(0);
                                            // récupérer le nombre de messages
                                            query.setQueryAsk("SELECT COUNT(*) FROM message WHERE id_discussion=" + id_discussion + ";");
                                            result = (ArrayList<Object>) query.getQueryResult().get(0);
                                            long nb_messages = (long) result.get(0);
                                            // récupérer la liste des membres
                                            // TODO : donner la photo de la discussion
                                            // query.setQueryAsk("SELECT u.nom_utilisateur, u.photo_utilisateur FROM utilisateur u, groupe_discussion g WHERE g.id_discussion=" + id_discussion + " AND g.id_utilisateur=u.id_utilisateur;");
                                            query.setQueryAsk("SELECT u.nom_utilisateur, u.description_utilisateur, g.role FROM utilisateur u, groupe_discussion g WHERE g.id_discussion=" + id_discussion + " AND g.id_utilisateur=u.id_utilisateur;");
                                            ArrayList<Object> data = query.getQueryResult();

                                            ArrayList<Object> discussion = new ArrayList<>();
                                            discussion.add(nom_discussion);
                                            discussion.add(nb_messages);
                                            discussion.add(data);
                                            client.send(Connection_Codes.RECUPERATION_DISCUSSION_OK, discussion);
                                        } else {
                                            ArrayList<Object> error = new ArrayList<>();
                                            error.add("Vous n'êtes pas dans cette discussion");
                                            client.send(Connection_Codes.RECUPERATION_DISCUSSION_KO, error);
                                        }
                                    } else {
                                        ArrayList<Object> error = new ArrayList<>();
                                        error.add("Erreur de format");
                                        client.send(Connection_Codes.RECUPERATION_DISCUSSION_KO, error);
                                    }
                                    break;
                                case RECUPERATION_UTILISATEURS:
                                    // vérifier que l'utilisateur est bien dans la discussion
                                    query.setQueryAsk("SELECT * FROM groupe_discussion WHERE id_discussion=" + annex.get(0) + " AND id_utilisateur=" + id + ";");
                                    if (query.getQueryResult().size() > 0) {
                                        query.setQueryAsk("SELECT u.nom_utilisateur, u.description_utilisateur, u.photo_utilisateur FROM utilisateur u, groupe_discussion g WHERE g.id_discussion=" + annex.get(0) + " AND g.id_utilisateur=u.id_utilisateur;");
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
                                case AJOUT_UTILISATEUR_DISCUSSION:
                                    // vérifier que l'utilisateur est bien dans la discussion
                                    query.setQueryAsk("SELECT role FROM groupe_discussion WHERE id_discussion=" + annex.get(0) + " AND id_utilisateur=" + id + ";");
                                    roles = (ArrayList) query.getQueryResult().get(0);
                                    int role = (int) roles.get(0);
                                    if (query.getQueryResult().size() > 0) {
                                        // vérifier que l'utilisateur n'est pas déjà dans la discussion
                                        query.setQueryAsk("SELECT * FROM groupe_discussion g, utilisateur u WHERE g.id_discussion=" + annex.get(0) + " AND u.nom_utilisateur='" + annex.get(1) + "' AND g.id_utilisateur=u.id_utilisateur;");
                                        if (query.getQueryResult().size() == 0) {
                                            int ext_role = (int) annex.get(2);
                                            String txt_role = "";
                                            switch (ext_role) {
                                                case 0:
                                                    txt_role = "utilisateur";
                                                    break;
                                                case 1:
                                                    txt_role = "modérateur";
                                                    break;
                                                case 2:
                                                    txt_role = "administrateur";
                                                    break;
                                                default:
                                                    txt_role = "utilisateur";
                                                    break;
                                            }
                                            if (ext_role <= role) {
                                                query.setQueryAsk("SELECT id_utilisateur FROM utilisateur WHERE nom_utilisateur=\"" + annex.get(1) + "\";");
                                                ArrayList<Object> id_utilisateur =(ArrayList<Object>) query.getQueryResult().get(0);
                                                // ajouter l'utilisateur à la discussion
                                                query.setQueryExecute("INSERT INTO groupe_discussion (id_discussion, id_utilisateur, role) VALUES (" + annex.get(0) + ", " + id_utilisateur.get(0) + ", " + annex.get(2) + ");");
                                                query.setQueryAsk("SELECT * FROM groupe_discussion g, utilisateur u WHERE g.id_discussion=" + annex.get(0) + " AND u.nom_utilisateur='" + annex.get(1) + "' AND g.id_utilisateur=u.id_utilisateur;");
                                                if (query.getQueryResult().size() > 0) {
                                                    ArrayList<Object> success = new ArrayList<>();
                                                    success.add("Le'utilisateur " + txt_role + " a bien été ajouté à la discussion");
                                                    client.send(Connection_Codes.AJOUT_UTILISATEUR_DISCUSSION_OK, success);
                                                } else {
                                                    ArrayList<Object> error = new ArrayList<>();
                                                    error.add("Erreur lors de l'ajout de l'utilisateur");
                                                    client.send(Connection_Codes.AJOUT_UTILISATEUR_DISCUSSION_KO, error);
                                                }
                                            } else {
                                                ArrayList<Object> error = new ArrayList<>();
                                                error.add("Vous n'avez pas assez de droits pour ajouter un " + txt_role + " à cette discussion");
                                                client.send(Connection_Codes.AJOUT_UTILISATEUR_DISCUSSION_KO, error);
                                            }
                                        } else {
                                            ArrayList<Object> error = new ArrayList<>();
                                            error.add("L'utilisateur est déjà dans la discussion");
                                            client.send(Connection_Codes.AJOUT_UTILISATEUR_DISCUSSION_KO, error);
                                        }
                                    } else {
                                        ArrayList<Object> error = new ArrayList<>();
                                        error.add("Vous n'êtes pas dans cette discussion");
                                        client.send(Connection_Codes.AJOUT_UTILISATEUR_DISCUSSION_KO, error);
                                    }
                                    break;
                                case SUPPRESSION_UTILISATEUR_DISCUSSION:
                                    // vérifier que l'utilisateur est bien dans la discussion et qu'il est admin
                                    query.setQueryAsk("SELECT role FROM groupe_discussion WHERE id_discussion=" + annex.get(0) + " AND id_utilisateur=" + id + ";");
                                    if (query.getQueryResult().size() > 0) {
                                        roles = (ArrayList) query.getQueryResult().get(0);
                                        int self_role = (int) roles.get(0);
                                        // vérifier que l'utilisateur est bien dans la discussion et on compare le role de l'utilisateur à supprimer
                                        query.setQueryAsk("SELECT g.role FROM groupe_discussion g, utilisateur u WHERE g.id_discussion=" + annex.get(0) + " AND u.nom_utilisateur='" + annex.get(1) + "' AND g.id_utilisateur=u.id_utilisateur;");
                                        if (query.getQueryResult().size() > 0) {
                                            roles = (ArrayList) query.getQueryResult().get(0);
                                            int user_role = (int) roles.get(0);
                                            System.out.println("self_role : " + self_role + " user_role : " + user_role);
                                            if (self_role > user_role) {
                                                // supprimer l'utilisateur de la discussion
                                                query.setQueryAsk("DELETE FROM groupe_discussion WHERE id_discussion=" + annex.get(0) + " AND id_utilisateur=" + annex.get(1) + ";");
                                                if (query.getQueryResult().size() == 0) {
                                                    ArrayList<Object> success = new ArrayList<>();
                                                    success.add("L'utilisateur a bien été supprimé de la discussion");
                                                    client.send(Connection_Codes.SUPPRESSION_UTILISATEUR_DISCUSSION_OK, success);
                                                } else {
                                                    ArrayList<Object> error = new ArrayList<>();
                                                    error.add("Erreur lors de la suppression de l'utilisateur");
                                                    client.send(Connection_Codes.SUPPRESSION_UTILISATEUR_DISCUSSION_KO, error);
                                                }
                                            } else {
                                                ArrayList<Object> error = new ArrayList<>();
                                                error.add("Vous ne pouvez pas supprimer un utilisateur avec un rôle supérieur ou égal au votre");
                                                client.send(Connection_Codes.SUPPRESSION_UTILISATEUR_DISCUSSION_KO, error);
                                            }
                                        } else {
                                            ArrayList<Object> error = new ArrayList<>();
                                            error.add("L'utilisateur n'est pas dans la discussion");
                                            client.send(Connection_Codes.SUPPRESSION_UTILISATEUR_DISCUSSION_KO, error);
                                        }
                                    } else {
                                        ArrayList<Object> error = new ArrayList<>();
                                        error.add("Vous n'êtes pas dans cette discussion");
                                        client.send(Connection_Codes.SUPPRESSION_UTILISATEUR_DISCUSSION_KO, error);
                                    }
                                    break;
                                default:
                                    Afficher("Code inconnu");
                                    break;
                            }
                        } else if (code == Connection_Codes.CREATION_UTILISATEUR) {
                            if (extract != null) {
                                // On vérifie que l'utilisateur n'existe pas déjà
                                query.setQueryAsk("SELECT * FROM utilisateur WHERE nom_utilisateur = \"" + annex.get(1) + "\";");
                                if (query.getQueryResult().size() == 0) {
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
                        if (e.getMessage().contains("Connection reset")) {
                            Afficher("Déconnecté (Connection reset)");
                            break;
                        } else if (e.getMessage().contains("Socket closed")) {
                            Afficher("Déconnecté (socket closed)");
                            break;
                        } else if (e.getMessage().contains("Connection timed out")) {
                            Afficher("Déconnecté (timed out)");
                            break;
                        } else if (e.getMessage().contains("Connection refused")) {
                            Afficher("Déconnecté (refused)");
                            break;
                        } else if (e.getMessage().contains("Connection aborted")) {
                            Afficher("Déconnecté (aborted)");
                            break;
                        } else {
                            Afficher("Erreur inconnue : " + e.getMessage());
                            e.printStackTrace();
                        }
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
        Afficher("Déconnexion du serveur");
    }

    public ArrayList<String> Extraction(ArrayList<Object> annex) {
        if (annex != null) {
            if (annex.size() % 2 == 0) {
                String txt = "";
                String txt2 = "";
                for (int i = 0; i < annex.size() / 2; i++) {
                    if (i == annex.size() / 2 - 1) {
                        txt += sanitize(annex.get(i * 2).toString());
                        if (annex.get(i * 2 + 1) instanceof String) {
                            txt2 += "\"" + sanitize(annex.get(i * 2 + 1).toString()) + "\"";
                        } else {
                            txt2 += sanitize(annex.get(i * 2 + 1).toString());
                        }
                    } else {
                        txt += sanitize(annex.get(i * 2).toString()) + ", ";
                        if (annex.get(i * 2 + 1) instanceof String) {
                            txt2 += "\"" + sanitize(annex.get(i * 2 + 1).toString()) + "\", ";
                        } else {
                            txt2 += sanitize(annex.get(i * 2 + 1).toString()) + ", ";
                        }
                    }
                }
                ArrayList<String> returned = new ArrayList<>();
                returned.add(txt);
                returned.add(txt2);
                return returned;
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    public String Search(ArrayList<Object> annex) {
        if (annex != null) {
            if (annex.size() % 2 == 0) {
                String txt = "";
                for (int i = 0; i < annex.size() / 2; i++) {
                    if (i == annex.size() / 2 - 1) {
                        if (annex.get(i * 2 + 1) instanceof String) {
                            txt += sanitize(annex.get(i * 2).toString()) + " = \"" + sanitize(annex.get(i * 2 + 1).toString()) + "\"";
                        } else {
                            txt += sanitize(annex.get(i * 2).toString()) + " = " + sanitize(annex.get(i * 2 + 1).toString());
                        }
                    } else {
                        if (annex.get(i * 2 + 1) instanceof String) {
                            txt += sanitize(annex.get(i * 2).toString()) + " = \"" + sanitize(annex.get(i * 2 + 1).toString()) + "\" AND ";
                        } else {
                            txt += sanitize(annex.get(i * 2).toString()) + " = " + sanitize(annex.get(i * 2 + 1).toString()) + " AND ";
                        }
                    }
                }
                return txt;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String SET(ArrayList<Object> annex) {
        if (annex != null) {
            if (annex.size() % 2 == 0) {
                String txt = "";
                for (int i = 0; i < annex.size() / 2; i++) {
                    if (i == annex.size() / 2 - 1) {
                        if (annex.get(i * 2 + 1) instanceof String) {
                            txt += sanitize(annex.get(i * 2).toString()) + " = \"" + sanitize(annex.get(i * 2 + 1).toString()) + "\"";
                        } else {
                            txt += sanitize(annex.get(i * 2).toString()) + " = " + sanitize(annex.get(i * 2 + 1).toString());
                        }
                    } else {
                        if (annex.get(i * 2 + 1) instanceof String) {
                            txt += sanitize(annex.get(i * 2).toString()) + " = \"" + sanitize(annex.get(i * 2 + 1).toString()) + "\", ";
                        } else {
                            txt += sanitize(annex.get(i * 2).toString()) + " = " + sanitize(annex.get(i * 2 + 1).toString()) + ", ";
                        }
                    }
                }
                return txt;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    // methode sanitize sql
    public String sanitize(String str) {
        return str.replaceAll(regex, "");
    }

    public void Afficher(String text) {
        if (id != -1) {
            System.out.println("client " + count + " (ID" + id + ") : " + text);
        } else {
            System.out.println("client " + count + " (not connected) : " + text);
        }

    }
}
