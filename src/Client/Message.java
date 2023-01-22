package Client;

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

    public Message(ArrayList message, int width) {
        this.id = (int) message.get(0);
        this.id_discussion = (int) message.get(1);
        this.id_utilisateur = (int) message.get(2);
        this.contenu = message.get(3);
        this.date = (Date) message.get(4);
        this.type = (int) message.get(5);
        this.nom_utilisateur = (String) message.get(6);

        panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension((int) (width * 0.95), 1000));
        panel.add(new JLabel(" "));
        if (this.type == 1) {
            JLabel user = new JLabel(this.nom_utilisateur);
            if (!this.nom_utilisateur.equals(Thread_Client.connexion.getNom_utilisateur())) {
                user.setForeground(Color.decode("#0000FF"));
            } else {
                user.setForeground(Color.decode("#00FF00"));
            }
            user.setFont(new Font("Arial", Font.BOLD, 10));
            panel.add(user);
            for (String line : auto_saut((String) this.contenu, 50)) {
                JLabel label = new JLabel(line);
                label.setFont(new Font("Arial", Font.PLAIN, 12));
                panel.add(label);
            }
            JLabel Jdate = new JLabel(this.date.toString());
            Jdate.setFont(new Font("Arial", Font.ITALIC, 10));
            Jdate.setForeground(Color.GRAY);
            panel.add(Jdate);
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

    public ArrayList<String> auto_saut(String text, int nb_carac) {
        ArrayList<String> texts = new ArrayList<>();
        int i = 0;
        while (i < text.length()) {
            if (i + nb_carac < text.length()) {
                texts.add(text.substring(i, i + nb_carac) + "-");
                i += nb_carac;
            } else {
                texts.add(text.substring(i));
                i = text.length();
            }
        }
        return texts;
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
