package Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class main {
    private static final int PORT = 2009; // socket port
    private static final InetAddress ADDRESS; // socket host
    static {
        try {
            ADDRESS = InetAddress.getByAddress(new byte[]{0, 0, 0, 0});
        } catch ( UnknownHostException e ) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        /*
          Demander à l'utilisateur sont id, mdp etc ou de créer un compte
          = authentification
          - Faire une interface avec des entrées
         */

        // sql select des id_discussion que l'utilisateur possède
        //------request.setQueryAsk("SELECT d.id_discussion d.nom_discussion d.photo_discussion FROM groupe_discussion as gd, discussion as d WHERE gd.id_utilisateur=" + id_utilisateur + " AND gd.id_discussion=d.id_discussion;");
        //------ArrayList channels = request.getQueryResult();
        // messages
        //------request.setQueryAsk("SELECT * FROM messages WHERE id_utilisateur=" + id_utilisateur);
        //------ArrayList messages = request.getQueryResult();
        // test connection TheBaye usr : 1 ; pass : 1234
        Client client = new Client(PORT, ADDRESS, 1, "1234");
        client.run();
        // création de l'interface
        Interface appli = new Interface();
        //------appli.reload(channels, messages);

        // changer la boucle while pour détecter quand un message est envoyé avec un listener de exit

    }
}

