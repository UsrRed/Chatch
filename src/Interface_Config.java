import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class Interface_Config extends JFrame { // la classe Cadre1 hérite de la classe des fenêtres Frame

    public static void main(String[] args){
        new Interface_Config(new ArrayList());
    }
    public Interface_Config(ArrayList listChan) {
        super();
        addWindowListener (new WindowAdapter(){ // fermeture de la fenêtre
            public void windowClosing (WindowEvent e){
                dispose();
            }
        });
        setTitle("Chatch"); // définit le titre de la fenêtre
        //setResizable(false); // permet de désactiver le carré qui agrandi la fenêtre
            setLayout(null); // pas de Layout, nous positionnons les composants nous-mêmes
        setBackground(Color.decode("#FF0000")); // couleur du background
        setForeground(Color.black); // couleur du texte
        pack();
        show();
        setSize(1000, 1000); // définit la taille de la fenêtre
    }

    public Interface_Config() {
        new Interface_Config(new ArrayList());

        JTextField jtf = new JTextField();
        String txt = jtf.getText();
        JTextField txte = new JTextField(txt);

    }
}
