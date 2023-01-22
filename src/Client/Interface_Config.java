package Client;

import tools.Connection_Codes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

public class Interface_Config extends JFrame {

    public Interface_Config() {
        // permet de changer les caractéristiques de l'utilisateur (nom, mot de passe, email, photo)
        // permet de supprimer le compte
        super();
        super.setVisible(false);
        setTitle("Configuration"); // définit le titre de la fenêtre
        setResizable(false); // permet de désactiver le carré qui agrandi la fenêtre
        setLayout(new BorderLayout(2, 2)); // pas de Layout, nous positionnons les composants nous-mêmes
        setBackground(Color.decode("#FF0000")); // couleur du background
        setForeground(Color.black); // couleur du texte
        setSize(400, 500);
        // placement des éléments
        // titre
        JLabel title = new JLabel("Configuration");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);
        // les boutons
        JPanel south = new JPanel();
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));

        JButton delete = new JButton("Supprimer le compte");
        delete.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton validate = new JButton("Valider les changements");
        validate.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton cancel = new JButton("Annuler les changements");
        cancel.setAlignmentX(Component.CENTER_ALIGNMENT);
        south.add(Box.createRigidArea(new Dimension(0, 10)));
        south.add(delete);
        south.add(Box.createRigidArea(new Dimension(0, 10)));
        south.add(validate);
        south.add(Box.createRigidArea(new Dimension(0, 10)));
        south.add(cancel);
        south.add(Box.createRigidArea(new Dimension(0, 10)));
        add(south, BorderLayout.SOUTH);
        // les champs
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setAlignmentY(Component.CENTER_ALIGNMENT);
        center.setAlignmentX(Component.CENTER_ALIGNMENT);

        center.add(Box.createRigidArea(new Dimension(0, 20)));
        JButton photo = new JButton("Changer la photo");
        JLabel photo_label = new JLabel("Photo");
        photo_label.setFont(new Font("Arial", Font.PLAIN, 14));
        photo_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        photo.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(photo_label);
        center.add(photo);

        center.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton name = new JButton("Changer le nom");
        JLabel name_label = new JLabel("Nom");
        name_label.setHorizontalAlignment(JLabel.CENTER);
        name_label.setFont(new Font("Arial", Font.PLAIN, 14));
        JLabel name_value = new JLabel(Thread_Client.connexion.getNom_utilisateur());
        name_value.setHorizontalAlignment(JLabel.CENTER);
        name_value.setFont(new Font("Arial", Font.PLAIN, 14));
        name.setAlignmentX(Component.CENTER_ALIGNMENT);
        name_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        name_value.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(name_label);
        center.add(name);
        center.add(name_value);

        center.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton password = new JButton("Changer le mot de passe");
        JLabel password_label = new JLabel("Mot de passe");
        password_label.setHorizontalAlignment(JLabel.CENTER);
        password_label.setFont(new Font("Arial", Font.PLAIN, 14));
        JLabel password_value = new JLabel("********");
        password_value.setHorizontalAlignment(JLabel.CENTER);
        password_value.setFont(new Font("Arial", Font.PLAIN, 14));
        password.setAlignmentX(Component.CENTER_ALIGNMENT);
        password_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        password_value.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(password_label);
        center.add(password);
        center.add(password_value);

        center.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton email = new JButton("Changer l'email");
        JLabel email_label = new JLabel("Email");
        email_label.setHorizontalAlignment(JLabel.CENTER);
        email_label.setFont(new Font("Arial", Font.PLAIN, 14));
        JLabel email_value = new JLabel("********");
        email_value.setHorizontalAlignment(JLabel.CENTER);
        email_value.setFont(new Font("Arial", Font.PLAIN, 14));
        email.setAlignmentX(Component.CENTER_ALIGNMENT);
        email_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        email_value.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(email_label);
        center.add(email);
        center.add(email_value);

        center.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton description = new JButton("Changer la description");
        JLabel description_label = new JLabel("Description");
        description_label.setHorizontalAlignment(JLabel.CENTER);
        description_label.setFont(new Font("Arial", Font.PLAIN, 14));
        JLabel description_value = new JLabel("********");
        description_value.setHorizontalAlignment(JLabel.CENTER);
        description_value.setFont(new Font("Arial", Font.PLAIN, 14));
        description.setAlignmentX(Component.CENTER_ALIGNMENT);
        description_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        description_value.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(description_label);
        center.add(description);
        center.add(description_value);

        center.add(Box.createRigidArea(new Dimension(0, 20)));

        add(center, BorderLayout.CENTER);
        // affichage de la fenêtre
        setVisible(true);
        setLocationRelativeTo(null);

        // les listeners
        // fermeture de la fenêtre
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Interface_Config.super.setVisible(true);
                dispose();
            }
        });
        // quand on clique sur le bouton "Supprimer le compte"
        delete.addActionListener(e -> {
            // on demande confirmation en comparant le mot de passe
            String password_confirm = JOptionPane.showInputDialog("Veuillez entrer votre mot de passe pour confirmer la suppression du compte");
            if (password_confirm != null) {
                if (password_confirm.equals(Thread_Client.connexion.getPassword())) {
                    // on envoie la requête de suppression
                    try {
                        Thread_Client.connexion.send(Connection_Codes.SUPPRESSION_UTILISATEUR);
                        dispose();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Mot de passe incorrect");
                }
            }
        });
        // quand on clique sur le bouton "Valider les changements"
        validate.addActionListener(e -> {
            // on demande confirmation en comparant le mot de passe
            String password_confirm = JOptionPane.showInputDialog("Veuillez entrer votre mot de passe pour confirmer les changements");
            if (password_confirm != null) {
                if (password_confirm.equals(Thread_Client.connexion.getPassword())) {
                    ArrayList<Object> changements = new ArrayList<>();
                    String change = "";
                    if (name_value.getText() != Thread_Client.connexion.getNom_utilisateur()) {
                        change += "\nNouveau nom : " + name_value.getText();
                        changements.add("nom_utilisateur");
                        changements.add(name_value.getText());
                    }
                    if (password_value.getText() != "********") {
                        change += "\nNouveau mot de passe : " + password_value.getText();
                        changements.add("motdepasse");
                        changements.add(password_value.getText());
                    }
                    if (email_value.getText() != "********") {
                        change += "\nNouveau email : " + email_value.getText();
                        changements.add("adresse_email");
                        changements.add(email_value.getText());
                    }
                    if (description_value.getText() != "********") {
                        change += "\nNouvelle description : " + description_value.getText();
                        changements.add("description_utilisateur");
                        changements.add(description_value.getText());
                    }
                    int reply = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment effectuer les changements suivants ?" + change, "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                        // si oui, on valide les changements
                        try {
                            Thread_Client.connexion.send(Connection_Codes.MODIFICATION_UTILISATEUR, changements);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Mot de passe incorrect");
                }
            }
        });
        // quand on clique sur le bouton "Annuler les changements"
        cancel.addActionListener(e -> {
            name_value.setText(Thread_Client.connexion.getNom_utilisateur());
            password_value.setText("********");
            email_value.setText("********");
            description_value.setText("********");
        });
        // quand on clique sur le bouton "Changer la photo"
        photo.addActionListener(e -> {
            /* TODO implémenter les photos dans la base de données
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choisissez une photo");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                // on envoie la photo
                try {
                    Thread_Client.connexion.send(Connection_Codes.MODIFICATION_PHOTO, selectedFile);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            */
            // affichage d'un message d'erreur
            JOptionPane.showMessageDialog(null, "Les photos ne sont pas encore implémentées");
        });
        // quand on clique sur le bouton "Changer le nom"
        name.addActionListener(e -> {
            String new_name = JOptionPane.showInputDialog("Veuillez entrer votre nouveau nom");
            if (new_name != null && new_name.length() > 3) {
                name_value.setText(new_name);
            } else {
                JOptionPane.showMessageDialog(null, "Le nom doit contenir au moins 4 caractères");
            }
        });
        // quand on clique sur le bouton "Changer le mot de passe"
        password.addActionListener(e -> {
            String new_password = JOptionPane.showInputDialog("Veuillez entrer votre nouveau mot de passe");
            // on vérifie que le mot de passe est assez long
            if (new_password != null && new_password.length() > 4) {
                // on redemande le mot de passe pour confirmer
                String new_password_confirm = JOptionPane.showInputDialog("Veuillez confirmer votre nouveau mot de passe");
                if (new_password_confirm.equals(new_password)) {
                    password_value.setText(new_password);
                } else {
                    JOptionPane.showMessageDialog(null, "Les mots de passe ne correspondent pas");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Le mot de passe doit contenir au moins 5 caractères");
            }
        });
        // quand on clique sur le bouton "Changer l'email"
        email.addActionListener(e -> {
            String new_email = JOptionPane.showInputDialog("Veuillez entrer votre nouveau email");
            // on vérifie que l'email est valide
            if (new_email != null && new_email.contains("@") && new_email.contains(".")) {
                email_value.setText(new_email);
            } else {
                JOptionPane.showMessageDialog(null, "L'email n'est pas valide");
            }
        });
        // quand on clique sur le bouton "Changer la description"
        description.addActionListener(e -> {
            String new_description = JOptionPane.showInputDialog("Veuillez entrer votre nouvelle description");
            description_value.setText(new_description);
        });
    }
}
