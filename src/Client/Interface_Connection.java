package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * @author : Eliott LEBOSSE et Yohann DENOYELLE
 * Cette classe permet de créer une fenêtre de connexion.
 * Elle permet de récupérer les informations de connexion de l'utilisateur et de les envoyer au serveur.
 * On peut se connecter, créer son compte et changer de serveur.
 */
public class Interface_Connection extends JFrame {
    private final InetAddress host; // socket host
    private final int port; // socket port
    public JPanel cards = new JPanel(new CardLayout());
    public CardLayout cardLayout;
    private Thread_Client client = null;

    public Interface_Connection(InetAddress host, int port) {
        super();
        this.host = host;
        this.port = port;

        JPanel Connexion = new JPanel();
        Connexion.setLayout(new BoxLayout(Connexion, BoxLayout.Y_AXIS));
        // Création des composants de la fenêtre de connexion
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

        // Création des boutons de la fenêtre de connexion
        JPanel Co_button = new JPanel();
        Co_button.setLayout(new BoxLayout(Co_button, BoxLayout.X_AXIS));
        JButton Co_button_connexion = new JButton("Se connecter");
        JButton Co_button_inscription = new JButton("S'inscrire -->");
        JButton Co_button_Serveur = new JButton("Serveur -->");

        // Placement des composants de la fenêtre de connexion
        Connexion.add(Co_title);
        Connexion.add(Co_login);
        Connexion.add(Co_login_text);
        Connexion.add(Co_password);
        Connexion.add(Co_password_text);
        Connexion.add(Co_button);

        Co_button.add(Co_button_connexion);
        Co_button.add(Co_button_inscription);
        Co_button.add(Co_button_Serveur);

        // Création des composants de la fenêtre d'inscription
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
        JLabel Cr_description = new JLabel("Description");
        Cr_description.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField Cr_description_text = new JTextField();
        Cr_description_text.setMaximumSize(new Dimension(200, 20));

        // Création des boutons de la fenêtre Inscription
        JPanel Cr_button = new JPanel();
        JButton Cr_button_connexion = new JButton("Se connecter -->");
        JButton Cr_button_inscription = new JButton("S'inscrire");

        // Placement des composants de la fenêtre Inscription
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

        // page pour le changement de connection au serveur
        JPanel Server = new JPanel();
        Server.setLayout(new BoxLayout(Server, BoxLayout.Y_AXIS));
        JLabel Se_title = new JLabel("Serveur");
        Se_title.setFont(new Font("Arial", Font.BOLD, 20));
        Se_title.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel Se_host = new JLabel("Adresse IP");
        Se_host.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField Se_host_text = new JTextField(host.getHostAddress());
        Se_host_text.setOpaque(false);
        Se_host_text.setMaximumSize(new Dimension(200, 20));
        JLabel Se_port = new JLabel("Port (default : 2009)");
        Se_port.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField Se_port_text = new JTextField(port + "");
        Se_port_text.setOpaque(false);
        Se_port_text.setMaximumSize(new Dimension(200, 20));
        JPanel Se_button = new JPanel();
        JButton Se_button_modifier = new JButton("Modifier");
        JButton Se_button_retour = new JButton("Retour -->");

        // Placement des composants de la fenêtre de changement de connexion avec le serveur
        Server.add(Se_title);
        Server.add(Se_host);
        Server.add(Se_host_text);
        Server.add(Se_port);
        Server.add(Se_port_text);
        Server.add(Se_button);

        Se_button.add(Se_button_modifier);
        Se_button.add(Se_button_retour);

        // Ajout des propriétés de la fenêtre
        setTitle("Chatch - Connection"); // définit le titre de la fenêtre
        cardLayout = (CardLayout) cards.getLayout();
        setLayout(cardLayout);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.decode("#FF0000")); // couleur du background
        setForeground(Color.black); // couleur du texte
        // placement des éléments

        cards.add(Connexion, "Connexion");
        cards.add(Inscription, "Inscription");
        cards.add(Server, "Server");
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
            try {
                if (client != null) {
                    client.close();
                }
                client = new Thread_Client(port, host, Co_login_text.getText(), Co_password_text.getText(), this, true, null);
                client.start();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        });
        // Cache le panel Connexion et affiche le panel Inscription
        Co_button_inscription.addActionListener(e -> {
            cardLayout.show(cards, "Inscription");
        });
        Co_button_Serveur.addActionListener(e -> {
            cardLayout.show(cards, "Server");
        });
        // menu Inscription
        // Cache le panel Inscription et affiche le panel Connexion
        Cr_button_connexion.addActionListener(e -> {
            cardLayout.show(cards, "Connexion");
        });
        // Inscription
        Cr_button_inscription.addActionListener(e -> {
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
                    client = new Thread_Client(port, host, Cr_login_text.getText(), Cr_password_text.getText(), this, false, data);
                    client.start();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                System.out.println("Les mots de passe ne correspondent pas");
            }
        });
        // menu Server
        Se_button_retour.addActionListener(e -> {
            cardLayout.show(cards, "Connexion");
        });
        // Modification de la connexion avec le serveur
        Se_button_modifier.addActionListener(e -> {
            String temp_addr = Se_host_text.getText();
            try {
                int temp_port = Integer.parseInt(Se_port_text.getText());
                // si l'adresse ip et le port sont valides
                if (temp_addr.matches("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$") && temp_port > 0 && temp_port < 65536) {
                    // on écrit les nouvelles valeurs dans le fichier de configuration
                    try (FileWriter fw = new FileWriter("src/Client/properties/configuration.properties")) {
                        fw.write("server_host=" + Se_host_text.getText() + "\n");
                        fw.write("server_port=" + Se_port_text.getText() + "\n");
                        JOptionPane.showMessageDialog(null, "Les modifications ont été enregistrées\nVous devez redémarrer pour prendre en compte les changements", "Modification", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "L'adresse IP ou le port n'est pas valide", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Le port doit être un nombre", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        // listener pour les touches
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dispose();
                }
            }
        });
        // listener pour Se_host_text prend le focus
        Se_host_text.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                Se_host_text.selectAll();
                Se_host_text.setOpaque(true);
                Se_host_text.setBackground(Color.decode("#FFFFFF"));
            }
        });
        // listener pour Se_host_text perd le focus
        Se_host_text.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                // si l'adresse ip est valide
                if (!Se_host_text.getText().matches("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$")) {
                    // met en rouge le champ
                    Se_host_text.setBackground(Color.decode("#FF0000"));
                }
            }
        });
        // listener pour Se_port_text prend le focus
        Se_port_text.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                Se_port_text.selectAll();
                Se_port_text.setOpaque(true);
                Se_port_text.setBackground(Color.decode("#FFFFFF"));
            }
        });
        // listener pour Se_port_text perd le focus
        Se_port_text.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                // si le port est valide
                try {
                    int temp_port = Integer.parseInt(Se_port_text.getText());
                    if (temp_port < 0 || temp_port > 65536) {
                        // met en rouge le champ
                        Se_port_text.setBackground(Color.decode("#FF0000"));
                    }
                } catch (NumberFormatException ex) {
                    // met en rouge le champ
                    Se_port_text.setBackground(Color.decode("#FF0000"));
                }
            }
        });
        // détection de l'appui des touches sur la page Connexion
        Connexion.addKeyListener(new KeyAdapter() {
            // TODO : ne fonctionne pas
            @Override
            public void keyPressed(KeyEvent e) {
                // TODO : ne fonctionne pas
                // si la touche entrée est pressée
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    Co_button_connexion.doClick();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dispose();
                } else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    // effacer les champs
                    Co_login_text.setText("");
                    Co_password_text.setText("");
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    Co_button_inscription.doClick();
                }
            }
        });
        // détection de l'appui des touches sur la page Inscription
        Inscription.addKeyListener(new KeyAdapter() {
            // TODO : ne fonctionne pas
            @Override
            public void keyPressed(KeyEvent e) {
                // TODO : ne fonctionne pas
                // si la touche entrée est pressée
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    Cr_button_inscription.doClick();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dispose();
                } else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    // effacer les champs
                    Cr_login_text.setText("");
                    Cr_password_text.setText("");
                    Cr_password_confirm_text.setText("");
                    Cr_mail_text.setText("");
                    Cr_description_text.setText("");
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    Cr_button_connexion.doClick();
                }
            }
        });
        // si une un JTextField perd le focus
        Co_login_text.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                Co_login_text.setText(Co_login_text.getText().trim());
            }
        });
        Co_password_text.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                Co_password_text.setText(Co_password_text.getText().trim());
            }
        });
        Cr_login_text.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                Cr_login_text.setText(Cr_login_text.getText().trim());
            }
        });
        // test le mot de passe une fois saisi
        Cr_password_text.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                Cr_password_text.setText(Cr_password_text.getText().trim());
                // si le mot de passe est trop court
                if (Cr_password_text.getText().length() < 8) {
                    Cr_password_text.setBackground(Color.decode("#FF0000"));
                    Cr_password_text.setToolTipText("Le mot de passe doit contenir au moins 8 caractères");
                } else {
                    Cr_password_text.setBackground(Color.decode("#FFFFFF"));
                }
            }
        });
        // test le mot de passe comfirm une fois saisi
        Cr_password_confirm_text.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                Cr_password_confirm_text.setText(Cr_password_confirm_text.getText().trim());
                // si les mots de passe ne correspondent pas
                if (!Cr_password_text.getText().equals(Cr_password_confirm_text.getText())) {
                    Cr_password_confirm_text.setBackground(Color.decode("#FF0000"));
                    Cr_password_confirm_text.setToolTipText("Les mots de passe ne correspondent pas");
                } else {
                    Cr_password_confirm_text.setBackground(Color.decode("#FFFFFF"));
                }
            }
        });
        // test l'adresse mail une fois saisie
        Cr_mail_text.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                Cr_mail_text.setText(Cr_mail_text.getText().trim());
                // si l'adresse mail n'est pas valide
                if (!Cr_mail_text.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    Cr_mail_text.setBackground(Color.decode("#FF0000"));
                    Cr_mail_text.setToolTipText("L'adresse mail n'est pas valide");
                } else {
                    Cr_mail_text.setBackground(Color.decode("#FFFFFF"));
                }
            }
        });
        Cr_description_text.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                Cr_description_text.setText(Cr_description_text.getText().trim());
            }
        });
        // si une un JTextField a le focus : remet la couleur blanche
        Co_login_text.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                Co_login_text.setBackground(Color.decode("#FFFFFF"));
            }
        });
        Co_password_text.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                Co_password_text.setBackground(Color.decode("#FFFFFF"));
            }
        });
        Cr_login_text.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                Cr_login_text.setBackground(Color.decode("#FFFFFF"));
            }
        });
        Cr_password_text.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                Cr_password_text.setBackground(Color.decode("#FFFFFF"));
            }
        });
        Cr_password_confirm_text.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                Cr_password_confirm_text.setBackground(Color.decode("#FFFFFF"));
            }
        });
        Cr_mail_text.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                Cr_mail_text.setBackground(Color.decode("#FFFFFF"));
            }
        });
        Cr_description_text.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                Cr_description_text.setBackground(Color.decode("#FFFFFF"));
            }
        });


    }

    /**
     * Affiche un message d'erreur
     *
     * @param message String
     */
    public void error(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Affiche un message d'information
     *
     * @param message String
     */
    public void success(String message) {
        JOptionPane.showMessageDialog(this, message, "Succès", JOptionPane.INFORMATION_MESSAGE);
    }
}
