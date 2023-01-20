package Client;

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
            if (!this.nom_utilisateur.equals(Thread_Client.connexion.getNom_utilisateur())) {
                panel.add(new JLabel(" : " + this.nom_utilisateur), BorderLayout.EAST);
                panel.add(new JLabel(auto_saut((String) this.contenu), JLabel.RIGHT), BorderLayout.CENTER);
                JLabel Jdate = new JLabel(this.date.toString(), JLabel.RIGHT);
                Jdate.setFont(new Font("Arial", Font.ITALIC, 10));
                Jdate.setForeground(Color.GRAY);
                panel.add(Jdate, BorderLayout.SOUTH);
                panel.setAlignmentX(Component.RIGHT_ALIGNMENT);
            } else {
                panel.add(new JLabel(this.nom_utilisateur + " : ", JLabel.LEFT), BorderLayout.WEST);
                panel.add(new JLabel(auto_saut((String) this.contenu), JLabel.LEFT), BorderLayout.CENTER);
                JLabel Jdate = new JLabel(this.date.toString(), JLabel.LEFT);
                Jdate.setFont(new Font("Arial", Font.ITALIC, 10));
                Jdate.setForeground(Color.GRAY);
                panel.add(Jdate, BorderLayout.SOUTH);
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

    public String auto_saut(String text) {
        // ajoute des \n pour que le texte s'affiche correctement
        String res = "";
        int i = 0;
        while (i < text.length()) {
            if (i % 30 == 0 && i != 0) {
                res += "\n";
            }
            res += text.charAt(i);
            i++;
        }
        return res;
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
