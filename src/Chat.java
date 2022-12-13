import javax.swing.*;
import java.util.ArrayList;

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

        // Layouts
        messages_frame.setLayout(new BoxLayout(messages_frame, BoxLayout.Y_AXIS));
        entry.setLayout(new BoxLayout(entry, BoxLayout.X_AXIS));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    }

    public void setMessages(ArrayList messages_request, String channel) {
        for (Object msg : messages_request) {
            JPanel box = new JPanel();
            // crée l'objet message
            Message message = new Message((ArrayList) msg);
            if (message.type == 1) { // Message string classique
                box.add(new JLabel((String) message.getContenu(), JLabel.LEFT));
            }
            messages_frame.add(box);
        }
    }

    public static void main(String[] args) {
        new Chat();
    }
}