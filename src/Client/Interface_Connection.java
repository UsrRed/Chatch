package Client;

import tools.Connection_Codes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Interface_Connection extends JFrame {
    private static final int PORT = 2009; // socket port
    private static final InetAddress ADDRESS; // socket host

    static {
        try {
            ADDRESS = InetAddress.getByAddress(new byte[]{0, 0, 0, 0});
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public JPanel cards = new JPanel(new CardLayout());
    public CardLayout cardLayout;
    private Thread_Client client = null;

    public Interface_Connection() {
        super();
        JPanel Connexion = new JPanel();
        Connexion.setLayout(new BoxLayout(Connexion, BoxLayout.Y_AXIS));
        JLabel Co_title = new JLabel("Connexion");
        Co_title.setFont(new Font("Arial", Font.BOLD, 20));
        Co_title.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel Co_login = new JLabel("Pseudonyme");
        Co_login.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField Co_login_text = new JTextField();
        Co_login_text.setMaximumSize(new Dimension(200, 20));
        JLabel Co_password = new JLabel("mot de passe");
        Co_password.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPasswordField Co_password_text = new JPasswordField();
        Co_password_text.setMaximumSize(new Dimension(200, 20));
        Co_password_text.setEchoChar('*');


        JPanel Co_button = new JPanel();
        JButton Co_button_connexion = new JButton("Se connecter");
        JButton Co_button_inscription = new JButton("S'inscrire -->");

        Connexion.add(Co_title);
        Connexion.add(Co_login);
        Connexion.add(Co_login_text);
        Connexion.add(Co_password);
        Connexion.add(Co_password_text);
        Connexion.add(Co_button);

        Co_button.add(Co_button_connexion);
        Co_button.add(Co_button_inscription);


        JPanel Inscription = new JPanel();
        Inscription.setLayout(new BoxLayout(Inscription, BoxLayout.Y_AXIS));
        JLabel Cr_title = new JLabel("Création de compte");
        Cr_title.setFont(new Font("Arial", Font.BOLD, 20));
        Cr_title.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel Cr_login = new JLabel("Pseudonyme");
        Cr_login.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField Cr_login_text = new JTextField();
        Cr_login_text.setMaximumSize(new Dimension(200, 20));
        JLabel Cr_password = new JLabel("Mot de passe");
        Cr_password.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPasswordField Cr_password_text = new JPasswordField();
        Cr_password_text.setEchoChar('*');
        Cr_password_text.setMaximumSize(new Dimension(200, 20));
        JLabel Cr_password_confirm = new JLabel("Confirmer le mot de passe");
        Cr_password_confirm.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPasswordField Cr_password_confirm_text = new JPasswordField();
        Cr_password_confirm_text.setMaximumSize(new Dimension(200, 20));
        Cr_password_confirm_text.setEchoChar('*');
        JLabel Cr_mail = new JLabel("Adresse mail");
        Cr_mail.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField Cr_mail_text = new JTextField();
        Cr_mail_text.setMaximumSize(new Dimension(200, 20));
        //  Mettre une description au profile
        JLabel Cr_description = new JLabel("Description");
        Cr_description.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField Cr_description_text = new JTextField();
        Cr_description_text.setMaximumSize(new Dimension(200, 20));


        JPanel Cr_button = new JPanel();
        JButton Cr_button_connexion = new JButton("Se connecter -->");
        JButton Cr_button_inscription = new JButton("S'inscrire");

        Inscription.add(Cr_title);
        Inscription.add(Cr_login);
        Inscription.add(Cr_login_text);
        Inscription.add(Cr_password);
        Inscription.add(Cr_password_text);
        Inscription.add(Cr_password_confirm);
        Inscription.add(Cr_password_confirm_text);
        Inscription.add(Cr_mail);
        Inscription.add(Cr_mail_text);
        Inscription.add(Cr_description);
        Inscription.add(Cr_description_text);
        Inscription.add(Cr_button);

        Cr_button.add(Cr_button_inscription);
        Cr_button.add(Cr_button_connexion);

        setTitle("Chatch - Connection"); // définit le titre de la fenêtre
        cardLayout = (CardLayout) cards.getLayout();
        setLayout(cardLayout);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.decode("#FF0000")); // couleur du background
        setForeground(Color.black); // couleur du texte
        // placement des éléments

        cards.add(Connexion, "Connexion");
        cards.add(Inscription, "Inscription");
        add(cards);
        cardLayout.show(cards, "Connexion");
        show();
        setSize(500, 350); // définit la taille de la fenêtre
        setLocationRelativeTo(null); // centre la fenêtre

        // les listeners
        addWindowListener(new WindowAdapter() { // fermeture de la fenêtre
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        // menu Connexion
        Co_button_connexion.addActionListener(e -> {
            System.out.println("Connexion");
            try {
                if (client != null) {
                    client.close();
                }
                client = new Thread_Client(PORT, ADDRESS, Co_login_text.getText(), Co_password_text.getText(), this, true, null);
                client.start();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        });
        // Cache le panel Connexion et affiche le panel Inscription
        Co_button_inscription.addActionListener(e -> {
            System.out.println("Inscription");
            cardLayout.show(cards, "Inscription");
        });
        // menu Inscription
        // Cache le panel Inscription et affiche le panel Connexion
        Cr_button_connexion.addActionListener(e -> {
            System.out.println("Connexion");
            cardLayout.show(cards, "Connexion");
        });
        Cr_button_inscription.addActionListener(e -> {
            System.out.println("Inscription");
            if (Cr_password_text.getText().equals(Cr_password_confirm_text.getText())) {
                ArrayList<String> data = new ArrayList<>();
                data.add("adresse_email");
                data.add(Cr_mail_text.getText());
                data.add("description_utilisateur");
                data.add(Cr_description_text.getText());
                try {
                    if (client != null) {
                        client.close();
                    }
                    client = new Thread_Client(PORT, ADDRESS, Cr_login_text.getText(), Cr_password_text.getText(), this, false, data);
                    client.start();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                System.out.println("Les mots de passe ne correspondent pas");
            }
        });
    }

    public void error(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    public void success(String message) {
        JOptionPane.showMessageDialog(this, message, "Succès", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        Interface_Connection fenetre = new Interface_Connection();
    }
}
