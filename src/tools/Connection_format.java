package tools;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author : Eliott LEBOSSE et Yohann DENOYELLE
 * Cette classe permet de créer un objet qui va être envoyé au ou au client
 * Ainsi on peut moduler les messages envoyés. Selon le code, on sait ce que l'on doit faire.
 */
public class Connection_format implements Serializable {
    protected Connection_Codes contenu;
    protected ArrayList<Object> annex; // annex du message

    /**
     * Constructeur de la classe Connection_format qui permet de créer un objet qui va être envoyé au ou au client
     *
     * @param contenu le code du message
     */
    public Connection_format(Connection_Codes contenu) {
        this.contenu = contenu;
        this.annex = null;
    }

    /**
     * Constructeur de la classe Connection_format qui permet de créer un objet qui va être envoyé au ou au client
     *
     * @param contenu le code du message
     * @param annex   les annexes du message (ArrayList)
     */
    public Connection_format(Connection_Codes contenu, ArrayList<Object> annex) {
        this.contenu = contenu;
        this.annex = annex;
    }

    /**
     * permet de récupérer le code du message
     *
     * @return le code du message
     */
    public Connection_Codes getContenu() {
        return contenu;
    }

    /**
     * permet de récupérer les annexes du message
     *
     * @return les annexes du message
     */
    public ArrayList<Object> getAnnex() {
        return annex;
    }

    /**
     * @return l'affichage du message
     */
    @Override
    public String toString() {
        return "Message{" +
                ", contenu=" + contenu +
                '}';
    }
}
