package test;

import Serveur.BDD_Query;
import Serveur.Database;
import java.sql.ResultSet;
import java.sql.SQLException;

class Tests {
    public static void main(String[] args) {
        Database data = new Database("10.195.25.15", "3306", "22104409t", "sae302");
        data.connect();
        System.out.println(data);

        // Requête BDD "classique"
        try {
            ResultSet ask = data.Query("SELECT * FROM utilisateur;");
            while (ask.next()){
                Integer id_utilisateur = ask.getInt("id_utilisateur");
                String nom_utilisateur = ask.getString("nom_utilisateur");
                String adresse_email = ask.getString("adresse_email");
                String motdepasse = ask.getString("motdepasse");
                String description_utilisateur = ask.getString("description_utilisateur");
                String photo_utilisateur = ask.getString("photo_utilisateur");
                System.out.println(id_utilisateur.toString() + "\t" + nom_utilisateur.toString()
                        + "\t" + adresse_email.toString() + "\t" + motdepasse.toString()
                        + "\t" + description_utilisateur.toString() + "\t" + photo_utilisateur.toString());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        // Requête BDD "simplifiée"
        BDD_Query TheQuery = new BDD_Query(data, "SELECT * FROM utilisateur;");
        System.out.println();
        // réutilisable à volonté
        TheQuery.setQueryAsk("SELECT * FROM message;");
        System.out.println(TheQuery);
        System.out.println("TEST");


        // Compte le nombre de discussion de l'utilisateur u (ensuite affichera les discussions)
        int u = 1;
        try {
            ResultSet ask = data.Query("SELECT COUNT(*)" +
                    "\tFROM discussion discu, groupe_discussion grp\n" +
                    "\tWHERE grp.id_discussion=discu.id_discussion AND grp.id_utilisateur='"+u+"';");
            while (ask.next()){
                Integer nb = ask.getInt("COUNT(*)");
                System.out.println(nb.toString());
                for (int i=0;i<nb;i++){
                    System.out.println(i);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        // déconnection de la BDD
        data.disconnect();
    }
}


