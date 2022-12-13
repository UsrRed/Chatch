import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*; // utilisation des classes du package awt
import java.util.ArrayList;

public class Menu_class extends JPanel {
    protected JComboBox menuChannel= new JComboBox();
    protected JButton config = new JButton(" Configuration ");
    public Menu_class() {
        // Place les menus dans la barre
        this.add(menuChannel);
        this.add(config);
    }
    public void setChannels(ArrayList listChan){
        // Ajout des Channels
        for(Object o : listChan){
            Menu_Item item = new Menu_Item((ArrayList) o);
            menuChannel.addItem(item);
        }
        menuChannel.addItem("+");
    }
    public Menu_Item getselected(){
        return (Menu_Item) menuChannel.getSelectedItem();
    }
}
