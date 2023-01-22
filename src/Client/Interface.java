package Client;

import tools.Connection_Codes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

public class Interface extends JFrame {
    private JPanel messages_frame = new JPanel();
    private JPanel chat = new JPanel();
    private JPanel menu = new JPanel();
    private JComboBox menuChannel = new JComboBox();
    JScrollPane scroll = new JScrollPane(messages_frame);

    public Interface() {
        super();
        // Place les menus dans la barre
        JButton config = new JButton(" Configuration ");
        menu.add(menuChannel);
        menu.add(config);

        messages_frame.setLayout(new BoxLayout(messages_frame, BoxLayout.Y_AXIS));
        messages_frame.setBackground(Color.WHITE);
        messages_frame.setMaximumSize(new Dimension(chat.getWidth(), 999999999));
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

        // Layouts
        messages_frame.setLayout(new BoxLayout(messages_frame, BoxLayout.Y_AXIS));
        // messages_frame avec les éléments au top
        messages_frame.setAlignmentY(Component.TOP_ALIGNMENT);
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
        add(menu, BorderLayout.NORTH);
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
        // bouton de configuration
        config.addActionListener(e -> {
            new Interface_Config();
        });
        // envoi de message
        valid_entry.addActionListener(e -> {
            // récupère le texte de data_entry
            String data = data_entry.getText();
            if (!data.equals("")) {
                // récupère le nom_utilisateur
                String nom_utilisateur = Thread_Client.connexion.getNom_utilisateur();
                // récupère le channel actuel
                int channel = getID_current_Channel();
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
            }
            // vide la zone de texte
            data_entry.setText("");
            // met la scrollbar en bas
            scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
            // remet le focus sur la zone de texte
            data_entry.requestFocus();

        });
        menuChannel.addActionListener(e -> {
            if (menuChannel.getSelectedItem().equals("+")) {
                // Ouvre une fenêtre pour créer un nouveau channel
                String channel_name = JOptionPane.showInputDialog("Nom du channel");
                if (channel_name != null) {
                    ArrayList<Object> channel = new ArrayList<>();
                    channel.add(channel_name);
                    try {
                        Thread_Client.connexion.send(Connection_Codes.CREATION_DISCUSSION, channel);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            } else {
                // Récupère les messages du channel
                ArrayList<Object> message = new ArrayList<>();
                Channel discussion = (Channel) menuChannel.getSelectedItem();
                message.add(discussion.getId());
                try {
                    Thread_Client.connexion.send(Connection_Codes.RECUPERATION_MESSAGES, message);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                // Récupère les utilisateurs du channel
                try {
                    Thread_Client.connexion.send(Connection_Codes.RECUPERATION_UTILISATEURS, message);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        // envoi de message avec la touche entrée
        data_entry.addActionListener(e -> {
            // TODO ne fonctionne pas
            if (e.getActionCommand().equals("Enter")) {
                valid_entry.doClick();
            }
        });
    }

    public void error(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    public void success(String message) {
        JOptionPane.showMessageDialog(this, message, "Succès", JOptionPane.INFORMATION_MESSAGE);
    }

    public int getID_current_Channel() {
        return ((Channel) menuChannel.getSelectedItem()).getId();
    }

    public void setMessages(ArrayList messages) {
        messages_frame.removeAll();
        for (Object msg : messages) {
            // crée l'objet message
            Message message = new Message((ArrayList) msg, messages_frame.getWidth());
            // ajoute le message à la liste
            messages_frame.add(message.getPane());
        }
        messages_frame.revalidate();
        messages_frame.repaint();
        scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
    }

    public void addMessage(ArrayList<Object> msg) {
        // crée l'objet message
        Message message = new Message(msg, messages_frame.getWidth());
        messages_frame.add(message.getPane());
        messages_frame.revalidate();
        messages_frame.repaint();
        scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
    }

    public void setDiscussions(ArrayList<Object> channels) {
        menuChannel.removeAllItems();
        int i = 0;
        // transforme les items de channels en Object Channel
        for (Object channel : channels) {
            menuChannel.addItem(new Channel((String) ((ArrayList) channel).get(0), (int) ((ArrayList) channel).get(1), (String) ((ArrayList) channel).get(2)));
            if (i == 0) {
                menuChannel.setSelectedIndex(0);
                int id = (int) ((ArrayList) channel).get(1);
                ArrayList<Object> data = new ArrayList<>();
                data.add(id);
                try {
                    Thread_Client.connexion.send(Connection_Codes.RECUPERATION_MESSAGES, data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            i++;
        }
        menuChannel.addItem("+");
        menuChannel.revalidate();
        menuChannel.repaint();
        // add listener
    }
}