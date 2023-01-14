package Serveur;

import tools.Connect;
import tools.Connection_Codes;
import tools.Connection_format;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Traitement_client extends Thread {

    private Socket socket = null;
    private Database data = null;
    private BDD_Query query = null;
    private int PORT;
    Connect client = null;

    public Traitement_client(Socket socket, Database data, int PORT) {
        this.PORT = PORT;
        this.socket = socket;
        this.data = data;
        query = new BDD_Query(data);
    }

    public void run() {
        Connection_format message = null;
        InputStream inputStream = null;
        try {
            do {
                System.out.println("Attente message");
                inputStream = socket.getInputStream();
                ObjectInputStream reader = null;
                reader = new ObjectInputStream(inputStream);
                try {
                    message = (Connection_format) reader.readObject();
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
                while (client == null) {
                    try {
                        client = new Connect(PORT + 1, socket.getInetAddress(), 1, "password");
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
                        switch (code) {
                            case CONNEXION:
                                System.out.println("Connexion");
                                query.setQueryAsk("SELECT * FROM utilisateur WHERE id_utilisateur = " + annex.get(0) + " AND motdepasse = '" + annex.get(1) + "'");
                                ArrayList res = query.getQueryResult();
                                if (res.size() > 0) {
                                    client.send(Connection_Codes.CONNEXION_OK);
                                    System.out.println("Connexion OK");
                                } else {
                                    client.send(Connection_Codes.CONNEXION_KO);
                                    System.out.println("Connexion KO");
                                }
                                break;
                            case CREATION_DISCUSSION:
                                System.out.println("Creation discussion");
                                query.setQueryAsk("INSERT INTO discussion (" + extract.get(0) + ") VALUES(" + extract.get(1) + ")");
                                break;
                            case SUPPRESSION_DISCUSSION:
                                System.out.println("Suppression discussion");
                                query.setQueryAsk("DELETE FROM discussion WHERE id_discussion = " + annex.get(0) + " ;");
                                break;
                            case MODIFICATION_DISCUSSION:
                                System.out.println("Modification discussion");
                                query.setQueryAsk("UPDATE FROM discussion SET colone = " + extract.get(0) + " WHERE id_discussion = " + extract.get(1) + " ;");
                                break;
                            case ENVOI_MESSAGE:
                                System.out.println("Envoi message");
                                query.setQueryAsk("INSERT INTO message ("+ extract.get(0) +") VALUES("+ extract.get(1) +");");
                                break;
                            case SUPPRESSION_MESSAGE:
                                System.out.println("Suppression message");
                                query.setQueryAsk("DELETE FROM message WHERE id_message=" + annex.get(0) + " ;");
                                break;
                            case MODIFICATION_MESSAGE:
                                System.out.println("Modification message");
                                query.setQueryAsk("UPDATE message SET contenu="+ extract.get(0) +" WHERE id_message = "+ extract.get(1) + " ;");
                                break;
                            case CREATION_GROUPE:
                                System.out.println("Creation groupe");
                                query.setQueryAsk("INSERT INTO groupe_discussion ("+ extract.get(0) +") VALUES ("+ extract.get(1) +");");
                                break;
                            case SUPPRESSION_GROUPE:
                                System.out.println("Suppression groupe");
                                query.setQueryAsk("DELETE FROM groupe_discussion WHERE id_discussion=" + annex.get(0) + "... ;");
                                break;
                            case MODIFICATION_GROUPE:
                                System.out.println("Modification groupe");
                                //query.setQueryAsk("UPDATE FROM groupe_discussion SET colone = "+ extract.get(0) +" WHERE id_discussion="+ extract.get(1) +";");
                                break;
                            case CREATION_UTILISATEUR:
                                System.out.println("Creation utilisateur");
                                query.setQueryAsk("INSERT INTO utilisateur ("+ extract.get(0) +") VALUES	("+ extract.get(1) +");");
                                break;
                            case SUPPRESSION_UTILISATEUR:
                                System.out.println("Suppression utilisateur");
                                query.setQueryAsk("DELETE FROM utilisateur WHERE id_utilisateur= " + extract.get(0) + ";");
                                break;
                            case MODIFICATION_UTILISATEUR:
                                System.out.println("Modification utilisateur");
                                //query.setQueryAsk("UPDATE utilisateur SET nom_de_colonne ="+ extract.get(0) +" WHERE id_utilisateur= "+ extract.get(1) +" ;");
                                break;
                            case CREATION_ADMIN_GROUPE:
                                System.out.println("Creation admin groupe");
                                //UPDATE groupe_discussion SET role=... WHERE id_utilisateur=... AND id_discussion=... ;
                                break;
                            case SUPPRESSION_ADMIN_GROUPE:
                                System.out.println("Suppression admin groupe");
                                //UPDATE groupe_discussion SET role= ... WHERE id_utilisateur= ... AND id_discussion= ... ;
                                break;
                            case MODIFICATION_ADMIN_GROUPE:
                                System.out.println("Modification admin groupe");
                                //UPDATE groupe_discussion SET role = ... WHERE id_utilisateur = ... AND id_discussion= ... ;
                                break;

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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
}
