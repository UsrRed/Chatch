package Client;

import tools.Message;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import tools.Connection_Codes;

public class Chat extends JPanel {
    private JPanel messages_frame = new JPanel();
    private Interface parent;

    public Chat(Interface parent) {
        super();
        this.parent = parent;
        JButton reload = new JButton("🔄");

        messages_frame.setLayout(new BoxLayout(messages_frame, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(messages_frame);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setPreferredSize(new Dimension(400, 400));
        scroll.setMaximumSize(new Dimension(400, 400));
        scroll.setMinimumSize(new Dimension(400, 400));
        add(scroll);

        JPanel entry = new JPanel();
        JTextField data_entry = new JTextField();
        data_entry.setMaximumSize(new Dimension(500, 25));
        JButton valid_entry = new JButton("Envoyer >>>");

        entry.add(data_entry);
        entry.add(valid_entry);
        add(entry);

        // envoi de message
        valid_entry.addActionListener(e -> {
            // récupère le texte de data_entry
            String data = data_entry.getText();
            // récupère le nom_utilisateur
            String nom_utilisateur = Thread_Client.connexion.getNom_utilisateur();
            // récupère le channel actuel
            int channel = parent.getID_current_chat();
            // envoie le message
            ArrayList<Object> message = new ArrayList<>();
            message.add("id_discussion");
            message.add(channel);
            message.add("contenu");
            message.add(data);
            message.add("type_message");
            message.add(1);
            try {
                Thread_Client.connexion.send(Connection_Codes.ENVOI_MESSAGE, message);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            // vide la zone de texte
            data_entry.setText("");
        });

        // Layouts
        messages_frame.setLayout(new BoxLayout(messages_frame, BoxLayout.Y_AXIS));
        entry.setLayout(new BoxLayout(entry, BoxLayout.X_AXIS));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    }

    public void setMessages(ArrayList messages) {
        messages_frame.removeAll();
        for (Object msg : messages) {
            // crée l'objet message
            Message message = new Message((ArrayList) msg);
            if (message.getType() == 1) { // Message string classique
                if (message.getNom().equals(Thread_Client.connexion.getNom_utilisateur())) {
                    messages_frame.add(new JLabel("Moi : " + message.getContenu(), JLabel.RIGHT));
                } else {
                    messages_frame.add(new JLabel(message.getNom() + " : " + message.getContenu(), JLabel.LEFT));
                }
            }
        }
    }
    public void addMessage(ArrayList<Object> message) {
        // crée l'objet message
        Message msg = new Message((ArrayList) message.get(0));
        if (msg.getType() == 1) { // Message string classique
            messages_frame.add(new JLabel((String) msg.getContenu(), JLabel.RIGHT));
        }
    }
}