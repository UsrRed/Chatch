package Client;

import tools.Message;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import tools.Connection_Codes;

public class Chat extends JPanel {
    private JPanel messages_frame = new JPanel();

    public Chat() {
        JPanel entry = new JPanel();
        JTextField data_entry = new JTextField();
        data_entry.setSize(60, 20);
        JButton valid_entry = new JButton("envoyer >>>");

        entry.add(data_entry);
        entry.add(valid_entry);
        add(entry);

        // better view
        /*
        entry.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        messages.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        */
        valid_entry.addActionListener(e -> {
            // récupère le texte de data_entry
            String data = data_entry.getText();
            // envoie le message
            ArrayList<Object> message = new ArrayList<>();
            message.add(data);
            try {
                Client.connexion.send(Connection_Codes.ENVOI_MESSAGE, message);
            } catch ( IOException ex ) {
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

    public void setMessages(ArrayList messages_request, Menu_Item channel) {
        for (Object msg : messages_request) {
            JPanel box = new JPanel();
            // crée l'objet message
            Message message = new Message((ArrayList) msg);
            if ( message.getType() == 1 ) { // Message string classique
                if ( message.getId_discussion() != channel.getId_discussion() ) {
                    box.add(new JLabel((String) message.getContenu(), JLabel.LEFT));
                }
            }
            messages_frame.add(box);
        }
    }

    public void setMessages(ArrayList messages_request_sorted) {
        for (Object msg : messages_request_sorted) {
            JPanel box = new JPanel();
            // crée l'objet message
            Message message = new Message((ArrayList) msg);
            if ( message.getType() == 1 ) { // Message string classique
                box.add(new JLabel((String) message.getContenu(), JLabel.LEFT));
            }
            messages_frame.add(box);
        }
    }
}