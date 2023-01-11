package tools;

import java.io.Serializable;
import java.util.ArrayList;

public class Connection_format implements Serializable {
    protected int id_utilisateur; // Qui a envoyé le message
    protected String password; // password de l'utilisateur
    protected Connection_Codes contenu;
    protected ArrayList<String> annex; // annex du message

    public Connection_format(int id_utilisateur, String password) {
        this.id_utilisateur = id_utilisateur;
        this.password = password;
    }

    public void setMessage(Connection_Codes contenu) {
        this.contenu = contenu;
        this.annex = null;
    }

    public void setMessage(Connection_Codes contenu, ArrayList<String> annex) {
        this.contenu = contenu;
        this.annex = annex;
    }

    public int getId_utilisateur() {
        return id_utilisateur;
    }

    public Connection_Codes getContenu() {
        return contenu;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "tools.Message{" +
                ", id_utilisateur=" + id_utilisateur +
                ", contenu=" + contenu +
                '}';
    }

    public ArrayList<String> getAnnex() {
        return annex;
    }
}
