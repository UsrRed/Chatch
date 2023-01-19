package tools;

import Client.Thread_Client;

import javax.swing.*;
import java.awt.*;
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
    protected JPanel panel; // panel du message

    public Message(ArrayList message) {
        this.id = (int) message.get(0);
        this.id_discussion = (int) message.get(1);
        this.id_utilisateur = (int) message.get(2);
        this.contenu = message.get(3);
        this.date = (Date) message.get(4);
        this.type = (int) message.get(5);
        this.nom_utilisateur = (String) message.get(6);

        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        if (this.type == 1) {
            if (this.nom_utilisateur.equals(Thread_Client.connexion.getNom_utilisateur())) {
                panel.add(new JLabel(" : " + this.nom_utilisateur), BorderLayout.EAST);
                panel.add(new JLabel((String) this.contenu, JLabel.RIGHT), BorderLayout.CENTER);
                panel.add(new JLabel(this.date.toString(), JLabel.RIGHT), BorderLayout.SOUTH);
                panel.setAlignmentX(Component.RIGHT_ALIGNMENT);
            } else {
                panel.add(new JLabel(this.nom_utilisateur + " : ", JLabel.LEFT), BorderLayout.WEST);
                panel.add(new JLabel((String) this.contenu, JLabel.LEFT), BorderLayout.CENTER);
                panel.add(new JLabel(this.date.toString(), JLabel.LEFT), BorderLayout.SOUTH);
                panel.setAlignmentX(Component.LEFT_ALIGNMENT);
            }
        } else {
            // TODO: message image et autres
        }
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

    public JPanel getPane() {
        return panel;
    }
}
