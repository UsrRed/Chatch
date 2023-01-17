package tools;

import java.io.Serializable;
import java.util.ArrayList;

public class Connection_format implements Serializable {
    protected Connection_Codes contenu;
    protected ArrayList<Object> annex; // annex du message

    public Connection_format(Connection_Codes contenu) {
        this.contenu = contenu;
        this.annex = null;
    }

    public Connection_format(Connection_Codes contenu, ArrayList<Object> annex) {
        this.contenu = contenu;
        this.annex = annex;
    }

    public Connection_Codes getContenu() {
        return contenu;
    }

    public ArrayList<Object> getAnnex() {
        return annex;
    }

    @Override
    public String toString() {
        return "Message{" +
                ", contenu=" + contenu +
                '}';
    }
}
