package Client;

import tools.Connection_Codes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

public class Interface extends JFrame {
    // chat comportant message_frame et l'envoie de messages
    private JPanel chat = new JPanel();
    // frame avec les messages
    private JPanel messages_frame = new JPanel();
    // scrollbar pour les messages (contient messages_frame)
    JScrollPane scroll = new JScrollPane(messages_frame);
    // menu de selection en haut
    private JPanel menu = new JPanel();
    // les channels disponibles
    private JComboBox menuChannel = new JComboBox();
    // propriétés de la discussion
    private JPanel propriete = new JPanel();
    private JPanel sub_propriete = new JPanel();


    public Interface() {
        super();
        // Place les menus dans la barre
        JButton config = new JButton("Configuration");
        menu.add(menuChannel);
        menu.add(config);

        // le chat
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

        messages_frame.setLayout(new BoxLayout(messages_frame, BoxLayout.Y_AXIS));
        messages_frame.setAlignmentY(Component.TOP_ALIGNMENT);
        entry.setLayout(new BoxLayout(entry, BoxLayout.X_AXIS));
        chat.setLayout(new BoxLayout(chat, BoxLayout.Y_AXIS));

        // propriétés de la discussion
        propriete.setLayout(new BoxLayout(propriete, BoxLayout.Y_AXIS));
        propriete.setBorder(BorderFactory.createTitledBorder("Propriétés de la discussion"));
        propriete.setAlignmentX(Component.CENTER_ALIGNMENT);
        propriete.add(sub_propriete);
        sub_propriete.setLayout(new BoxLayout(sub_propriete, BoxLayout.Y_AXIS));
        JButton add_user = new JButton("Ajouter un utilisateur");
        propriete.add(add_user);
        propriete.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton remove_user = new JButton("Supprimer un utilisateur");
        propriete.add(remove_user);
        propriete.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton remove_discussion = new JButton("Supprimer la discussion");
        propriete.add(remove_discussion);

        setTitle("Chatch"); // définit le titre de la fenêtre
        //setResizable(false); // permet de désactiver le carré qui agrandi la fenêtre
        setLayout(new BorderLayout(2, 2)); // pas de Layout, nous positionnons les composants nous-mêmes
        setBackground(Color.decode("#FF0000")); // couleur du background
        setForeground(Color.black); // couleur du texte
        // placement des éléments
        add(chat, BorderLayout.CENTER);
        add(menu, BorderLayout.NORTH);
        add(propriete, BorderLayout.EAST);
        show();
        setSize(1280, 720); // définit la taille de la fenêtre
        setLocationRelativeTo(null); // centre la fenêtre

        addWindowListener(new WindowAdapter() { // fermeture de la fenêtre
            public void windowClosing(WindowEvent e) {
                // TODO : fermeture indirecte avec demande au serveur (ne fonctionne pas)
                try {
                    Thread_Client.connexion.send(Connection_Codes.DECONNEXION);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println("Fermeture de la fenêtre");
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
                // Récupère les propriétés du channel
                try {
                    Thread_Client.connexion.send(Connection_Codes.RECUPERATION_DISCUSSION, message);
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
        // ajoute un utilisateur
        add_user.addActionListener(e -> {
            // récupère le channel actuel
            int channel = getID_current_Channel();
            // récupère le nom de l'utilisateur
            String user_name = JOptionPane.showInputDialog("Nom de l'utilisateur");
            // dialog avec le choix du role de l'utilisateur
            Object[] options = {"Administrateur", "Modérateur", "Membre"};
            int role = JOptionPane.showOptionDialog(null,
                    "Quel est le rôle de l'invité ?", "Rôle de l'utilisateur",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]) + 1;
            if (user_name != null) {
                // envoie le message
                ArrayList<Object> message = new ArrayList<>();
                message.add(channel);
                message.add(user_name);
                message.add(role);
                try {
                    Thread_Client.connexion.send(Connection_Codes.AJOUT_UTILISATEUR_DISCUSSION, message);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        // supprime un utilisateur
        remove_user.addActionListener(e -> {
            // récupère le channel actuel
            int channel = getID_current_Channel();
            // récupère le nom de l'utilisateur
            String user_name = JOptionPane.showInputDialog("Nom de l'utilisateur");
            if (user_name != null) {
                // envoie le message
                ArrayList<Object> message = new ArrayList<>();
                message.add("id_discussion");
                message.add(channel);
                message.add("nom_utilisateur");
                message.add(user_name);
                try {
                    Thread_Client.connexion.send(Connection_Codes.SUPPRESSION_UTILISATEUR_DISCUSSION, message);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        // supprime un channel
        remove_discussion.addActionListener(e -> {
            // récupère le channel actuel
            int channel = getID_current_Channel();
            // envoie le message
            ArrayList<Object> message = new ArrayList<>();
            message.add(channel);
            try {
                Thread_Client.connexion.send(Connection_Codes.SUPPRESSION_DISCUSSION, message);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
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
        // TODO : optimiser le rafrachissement, ne pas avoir a redémarer.
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
    }

    public void set_discussion(ArrayList annex) {
        // récupère le nom du channel
        String channel_name = (String) annex.get(0);
        // récupère le nombre de messages
        long nb_messages = (long) annex.get(1);
        // récupère le nombre d'utilisateurs
        ArrayList<Object> membres = (ArrayList) annex.get(2);
        int nb_membres = membres.size();
        sub_propriete.removeAll();
        sub_propriete.add(new JLabel("Nom du channel : " + channel_name));
        sub_propriete.add(new JLabel("Nombre de messages : " + nb_messages));
        sub_propriete.add(new JLabel("Membres (" + nb_membres + ") : "));
        for (int i = 0; i < membres.size(); i++) {
            ArrayList<Object> membre = (ArrayList<Object>) membres.get(i);
            String nom = (String) membre.get(0);
            String description = (String) membre.get(1);
            int role_id = (int) membre.get(2);
            JLabel Jnom = new JLabel(nom);
            if (role_id == 1) {
                Jnom.setText(Jnom.getText() + " (Admin)");
                Jnom.setForeground(Color.RED);
            } else if (role_id == 2) {
                Jnom.setText(Jnom.getText() + " (Modérateur)");
                Jnom.setForeground(Color.BLUE);
            } else if (role_id == 3) {
                Jnom.setText(Jnom.getText() + " (Membre)");
                Jnom.setForeground(Color.BLACK);
            } else {
                Jnom.setText(Jnom.getText() + " (Membre)");
                Jnom.setForeground(Color.BLACK);
            }
            sub_propriete.add(Jnom);
            if (description != null) {
                JLabel Jdescription = new JLabel("\t\t" + description);
                Jdescription.setForeground(Color.GRAY);
                Jdescription.setFont(new Font("Arial", Font.ITALIC, 12));
                sub_propriete.add(Jdescription);
            }
        }
        sub_propriete.revalidate();
        sub_propriete.repaint();
        propriete.revalidate();
        propriete.repaint();
    }
}