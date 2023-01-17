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

    public Message(ArrayList messages_request) {
        this.id = (int) messages_request.get(0);
        this.id_discussion = (int) messages_request.get(1);
        this.id_utilisateur = (int) messages_request.get(2);
        this.contenu = messages_request.get(3);
        this.date = (Date) messages_request.get(4);
        this.type = (int) messages_request.get(5);
    }

    public int getId() {
        return id;
    }

    public int getId_discussion() {
        return id_discussion;
    }

    public int getId_utilisateur() {
        return id_utilisateur;
    }

    public Object getContenu() {
        return contenu;
    }

    public Date getDate() {
        return date;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return "tools.Message{" +
                "id=" + id +
                ", id_discussion=" + id_discussion +
                ", id_utilisateur=" + id_utilisateur +
                ", contenu=" + contenu +
                ", date=" + date +
                ", type=" + type +
                '}';
    }

    public void modify(String contenu, int type) {
        this.contenu = contenu;
        this.type = type;
    }
}
