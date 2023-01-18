package Client;

import tools.Connection_Codes;

import javax.swing.*;
import java.awt.*; // utilisation des classes du package awt
import java.io.IOException;
import java.util.ArrayList;

public class Menu_class extends JPanel {
    protected JComboBox menuChannel = new JComboBox();
    protected JButton config = new JButton(" Configuration ");

    public Menu_class() {
        // Place les menus dans la barre
        this.add(menuChannel);
        this.add(config);
    }

    public void reloadChannels(ArrayList channels) {
        menuChannel.removeAllItems();
        for (Object channel : channels) {
            menuChannel.addItem(channel);
        }
        menuChannel.addItem("+");
        // menuChannel.setSelectedIndex(0);
        // add listener
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
                message.add(menuChannel.getSelectedItem());
                try {
                    Thread_Client.connexion.send(Connection_Codes.RECUPERATION_MESSAGES, message);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                // Récupère les propriétés du channel
                try {
                    Thread_Client.connexion.send(Connection_Codes.RECUPERATION_DISCUSSIONS, message);
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
    }

    public Menu_Item getselected() {
        return (Menu_Item) menuChannel.getSelectedItem();
    }
}
