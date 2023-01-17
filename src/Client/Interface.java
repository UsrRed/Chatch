package Client;

import tools.Connection_Codes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

public class Interface extends JFrame { // la classe Cadre1 hérite de la classe des fenêtres Frame
    protected Chat frame_chat = new Chat();
    protected Menu_class frame_menu = new Menu_class();

    public Interface() {
        super();
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
        setTitle("Chatch"); // définit le titre de la fenêtre
        //setResizable(false); // permet de désactiver le carré qui agrandi la fenêtre
        setLayout(new BorderLayout(2, 2)); // pas de Layout, nous positionnons les composants nous-mêmes
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.decode("#FF0000")); // couleur du background
        setForeground(Color.black); // couleur du texte
        // placement des éléments
        this.add(frame_chat, BorderLayout.CENTER);
        this.add(frame_menu, BorderLayout.NORTH);
        show();
        setSize(1000, 1000); // définit la taille de la fenêtre
        setLocationRelativeTo(null); // centre la fenêtre

    }

    public void reload(ArrayList listChan, ArrayList messages) {
        frame_menu.setChannels(listChan);
        frame_chat.setMessages(messages, frame_menu.getselected());
    }

    public void reload_msg(ArrayList messages) {
        frame_chat.setMessages(messages);
    }
}