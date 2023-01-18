package Client;

import javax.swing.*;

public class Channel extends JComboBox {
    protected int id;
    protected String nom;
    protected String photo = null;

    public Channel(String name, int id, String photo) {
        super();
        this.id = id;
        this.nom = name;
        this.photo = photo;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return this.nom;
    }
}
