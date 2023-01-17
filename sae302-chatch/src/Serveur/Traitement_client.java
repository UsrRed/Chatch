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
                    Connection_Codes code = message.getContenu();
                    ArrayList<Object> annex = message.getAnnex();
                    try {
                        ArrayList<String> extract = Extraction(annex);
                        if (code == Connection_Codes.CONNEXION) {
                            Afficher("Connexion");
                            query.setQueryAsk("SELECT * FROM utilisateur WHERE nom_utilisateur = " + annex.get(0) + " AND motdepasse = '" + annex.get(1) + "'");
                            ArrayList<Object> res = query.getQueryResult();
                            if (res.size() > 0) {
                                client.send(Connection_Codes.CONNEXION_OK);
                                ArrayList cl = (ArrayList) res.get(0);
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
                                    query.setQueryAsk("INSERT INTO discussion (" + extract.get(0) + ") VALUES(" + extract.get(1) + ")");
                                    break;
                                case SUPPRESSION_DISCUSSION:
                                    Afficher("Suppression discussion");
                                    query.setQueryAsk("DELETE FROM discussion WHERE id_discussion = " + annex.get(0) + " ;");
                                    break;
                                case MODIFICATION_DISCUSSION:
                                    Afficher("Modification discussion");
                                    query.setQueryAsk("UPDATE FROM discussion SET colone = " + extract.get(0) + " WHERE id_discussion = " + extract.get(1) + " ;");
                                    break;
                                case ENVOI_MESSAGE:
                                    Afficher("Envoi message");
                                    query.setQueryAsk("INSERT INTO message (" + extract.get(0) + ") VALUES(" + extract.get(1) + ");");
                                    break;
                                case SUPPRESSION_MESSAGE:
                                    Afficher("Suppression message");
                                    query.setQueryAsk("DELETE FROM message WHERE id_message=" + annex.get(0) + " ;");
                                    break;
                                case MODIFICATION_MESSAGE:
                                    Afficher("Modification message");
                                    query.setQueryAsk("UPDATE message SET contenu=" + extract.get(0) + " WHERE id_message = " + extract.get(1) + " ;");
                                    break;
                                case CREATION_GROUPE:
                                    Afficher("Creation groupe");
                                    query.setQueryAsk("INSERT INTO groupe_discussion (" + extract.get(0) + ") VALUES (" + extract.get(1) + ");");
                                    break;
                                case SUPPRESSION_GROUPE:
                                    Afficher("Suppression groupe");
                                    query.setQueryAsk("DELETE FROM groupe_discussion WHERE id_discussion=" + annex.get(0) + "... ;");
                                    break;
                                case MODIFICATION_GROUPE:
                                    Afficher("Modification groupe");
                                    //query.setQueryAsk("UPDATE FROM groupe_discussion SET colone = "+ extract.get(0) +" WHERE id_discussion="+ extract.get(1) +";");
                                    break;
                                case SUPPRESSION_UTILISATEUR:
                                    Afficher("Suppression utilisateur");
                                    query.setQueryAsk("DELETE FROM utilisateur WHERE id_utilisateur= " + extract.get(0) + ";");
                                    break;
                                case MODIFICATION_UTILISATEUR:
                                    Afficher("Modification utilisateur");
                                    //query.setQueryAsk("UPDATE utilisateur SET nom_de_colonne ="+ extract.get(0) +" WHERE id_utilisateur= "+ extract.get(1) +" ;");
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
                            query.setQueryAsk("INSERT INTO utilisateur (" + extract.get(0) + ") VALUES (" + extract.get(1) + ");");
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
        if (annex.size() % 2 == 0) {
            String txt = "";
            String txt2 = "";
            for (int i = 0; i < annex.size() / 2; i++) {
                if (i == annex.size() / 2 - 1) {
                    txt += annex.get(i * 2);
                    txt2 += annex.get(i * 2 + 1);
                } else {
                    txt += annex.get(i * 2) + ", ";
                    txt2 += annex.get(i * 2 + 1) + ", ";
                }
            }
            ArrayList<String> returned = new ArrayList<String>();
            returned.add(txt);
            returned.add(txt2);
            return returned;
        } else {
            return null;
        }
    }

    public void Afficher(String text){
        if(id != -1){
            System.out.println("client " + count + "(" + id + ") : " + text);
        } else {
            System.out.println("client " + count + "(not connected) : " + text);
        }

    }
}
