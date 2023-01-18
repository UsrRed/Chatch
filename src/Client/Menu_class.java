package Client;

import tools.Connection_Codes;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public class Menu_class extends JPanel {
    protected JComboBox menuChannel = new JComboBox();
    protected JButton config = new JButton(" Configuration ");
    private Interface parent;

    public Menu_class(Interface parent) {
        // Place les menus dans la barre
        this.add(menuChannel);
        this.add(config);
        this.parent = parent;
    }

    public void reloadChannels(ArrayList<Object> channels) {
        menuChannel.removeAllItems();
        int i = 0;
        // transforme les items de channels en Object Channel
        for (Object channel : channels) {
            menuChannel.addItem(new Channel((String) ((ArrayList) channel).get(0), (int) ((ArrayList) channel).get(1), (String) ((ArrayList) channel).get(2)));
            if(i == 0) {
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
    }

    public int getID() {
        return ((Channel) menuChannel.getSelectedItem()).getId();
    }
}
