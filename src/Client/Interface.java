package Client;

import tools.Connection_Codes;
import tools.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

public class Interface extends JFrame { // la classe Cadre1 hérite de la classe des fenêtres Frame
    private JPanel messages_frame = new JPanel();
    private JPanel chat = new JPanel();
    protected Menu_class frame_menu = new Menu_class(this);

    public Interface() {
        super();


        messages_frame.setLayout(new BoxLayout(messages_frame, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(messages_frame);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setPreferredSize(new Dimension(400, 400));
        scroll.setMaximumSize(new Dimension(400, 400));
        scroll.setMinimumSize(new Dimension(400, 400));
        chat.add(scroll);

        JPanel entry = new JPanel();
        JTextField data_entry = new JTextField();
        data_entry.setMaximumSize(new Dimension(500, 25));
        JButton valid_entry = new JButton("Envoyer >>>");

        entry.add(data_entry);
        entry.add(valid_entry);
        chat.add(entry);

        // envoi de message
        valid_entry.addActionListener(e -> {
            // récupère le texte de data_entry
            String data = data_entry.getText();
            // récupère le nom_utilisateur
            String nom_utilisateur = Thread_Client.connexion.getNom_utilisateur();
            // récupère le channel actuel
            int channel = getID_current_chat();
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
        chat.setLayout(new BoxLayout(chat, BoxLayout.Y_AXIS));

        setTitle("Chatch"); // définit le titre de la fenêtre
        //setResizable(false); // permet de désactiver le carré qui agrandi la fenêtre
        setLayout(new BorderLayout(2, 2)); // pas de Layout, nous positionnons les composants nous-mêmes
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.decode("#FF0000")); // couleur du background
        setForeground(Color.black); // couleur du texte
        // placement des éléments
        add(chat, BorderLayout.CENTER);
        add(frame_menu, BorderLayout.NORTH);
        show();
        setSize(1280, 720); // définit la taille de la fenêtre
        setLocationRelativeTo(null); // centre la fenêtre

        addWindowListener(new WindowAdapter() { // fermeture de la fenêtre
            public void windowClosing(WindowEvent e) {
                dispose();
                // envoie du message "deconnexion"
                try {
                    Thread_Client.connexion.send(Connection_Codes.DECONNEXION);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public int getID_current_chat() {
        return frame_menu.getID();
    }

    public void setDiscussions(ArrayList<Object> listChan) {
        frame_menu.reloadChannels(listChan);
    }

    public void error(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    public void success(String message) {
        JOptionPane.showMessageDialog(this, message, "Succès", JOptionPane.INFORMATION_MESSAGE);
    }

    public void setMessages(ArrayList messages) {
        messages_frame.removeAll();
        for (Object msg : messages) {
            // crée l'objet message
            Message message = new Message((ArrayList) msg);
            // ajoute le message à la liste
            messages_frame.add(message.getPane());
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