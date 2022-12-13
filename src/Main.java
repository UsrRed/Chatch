import javax.swing.*;
import java.util.ArrayList;

class Main {
    public static void main(String[] args) {
        Database data = new Database("10.195.25.15", "3306", "22104409t", "sae302");
        data.connect();

        /*
          Demander à l'utilisateur sont id, mdp etc ou de créer un compte
          = authentification
          - Faire une interface avec des entrées
         */
        int id_utilisateur = 1;


        BDD_Query request = new BDD_Query(data);
        // sql select des id_discussion que l'utilisateur possède
        request.setQueryAsk("SELECT d.id_discussion d.nom_discussion d.photo_discussion FROM groupe_discussion as gd, discussion as d WHERE gd.id_utilisateur=" + id_utilisateur + " AND gd.id_discussion=d.id_discussion;");
        ArrayList channels = request.getQueryResult();
        // messages
        request.setQueryAsk("SELECT * FROM messages WHERE id_utilisateur=" + id_utilisateur);
        ArrayList messages = request.getQueryResult();

        // création de l'interface
        Interface appli = new Interface();
        appli.reload(channels, messages);

        // déconnexion de la BDD
        data.disconnect();
    }
}


