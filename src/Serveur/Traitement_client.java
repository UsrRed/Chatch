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
                } catch ( ClassNotFoundException | IOException e ) {
                    e.printStackTrace();
                }
                while ( client==null ) {
                    try {
                        client = new Connect(PORT+1, socket.getInetAddress(), 1, "password");
                        Thread.sleep(1000);
                    } catch ( IOException e ) {
                        e.printStackTrace();
                    } catch ( InterruptedException e ) {
                        throw new RuntimeException(e);
                    }
                }
                // Traitement
                if ( message != null ) {
                    Connection_Codes code = message.getContenu();
                    ArrayList<Object> annex = message.getAnnex();
                    switch ( code ) {
                        case CONNEXION:
                            System.out.println("Connexion");
                            query.setQueryAsk("SELECT * FROM utilisateur WHERE id_utilisateur = " + annex.get(0) + " AND motdepasse = '" + annex.get(1) + "'");
                            ArrayList res = query.getQueryResult();
                            if ( res.size() > 0 ) {
                                client.send(Connection_Codes.CONNEXION_OK);
                                System.out.println("Connexion OK");
                            } else {
                                client.send(Connection_Codes.CONNEXION_KO);
                                System.out.println("Connexion KO");
                            }
                            break;
                        case CREATION_DISCUSSION:
                            System.out.println("Creation discussion");
                            break;
                        case SUPPRESSION_DISCUSSION:
                            System.out.println("Suppression discussion");
                            break;
                        case MODIFICATION_DISCUSSION:
                            System.out.println("Modification discussion");
                            break;
                        case ENVOI_MESSAGE:
                            System.out.println("Envoi message");
                            break;
                        case SUPPRESSION_MESSAGE:
                            System.out.println("Suppression message");
                            break;
                        case MODIFICATION_MESSAGE:
                            System.out.println("Modification message");
                            break;
                        case CREATION_GROUPE:
                            System.out.println("Creation groupe");
                            break;
                        case SUPPRESSION_GROUPE:
                            System.out.println("Suppression groupe");
                            break;
                        case MODIFICATION_GROUPE:
                            System.out.println("Modification groupe");
                            break;
                        case CREATION_UTILISATEUR:
                            System.out.println("Creation utilisateur");
                            break;
                        case SUPPRESSION_UTILISATEUR:
                            System.out.println("Suppression utilisateur");
                            break;
                        case MODIFICATION_UTILISATEUR:
                            System.out.println("Modification utilisateur");
                            break;
                        case CREATION_ADMIN_GROUPE:
                            System.out.println("Creation admin groupe");
                            break;
                        case SUPPRESSION_ADMIN_GROUPE:
                            System.out.println("Suppression admin groupe");
                            break;
                        case MODIFICATION_ADMIN_GROUPE:
                            System.out.println("Modification admin groupe");
                            break;

                    }
                }
            } while ( !message.getContenu().equals(Connection_Codes.DECONNEXION) );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch ( IOException e ) {
            throw new RuntimeException(e);
        }
        System.out.println("deconnexion");
    }
}
