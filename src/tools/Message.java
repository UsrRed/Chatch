package tools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Message implements Serializable {
    protected int id; // id du message
    protected int id_discussion; // id de la discussion
    protected int id_utilisateur; // Qui a envoyé le message
    protected Object contenu; // varchar 1000 avec soit text, soit image etc
    protected Date date; // Date du message
    protected int type; // type de message (img/text/autre) 1 : text, 2 : image, 3 : autre
    protected String nom_utilisateur; // nom de l'utilisateur qui a envoyé le message

    public Message(ArrayList messages_request) {
        this.id = (int) messages_request.get(0);
        this.id_discussion = (int) messages_request.get(1);
        this.id_utilisateur = (int) messages_request.get(2);
        this.contenu = messages_request.get(3);
        this.date = (Date) messages_request.get(4);
        this.type = (int) messages_request.get(5);
        this.nom_utilisateur = (String) messages_request.get(6);
    }

    public Object getContenu() {
        return contenu;
    }

    public int getType() {
        return type;
    }

    public String getNom() {
        return nom_utilisateur;
    }
    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", id_discussion=" + id_discussion +
                ", id_utilisateur=" + id_utilisateur +
                ", contenu=" + contenu +
                ", date=" + date +
                ", type=" + type +
                ", nom_utilisateur='" + nom_utilisateur + '\'' +
                '}';
    }

    public void modify(String contenu, int type) {
        this.contenu = contenu;
        this.type = type;
    }
}
