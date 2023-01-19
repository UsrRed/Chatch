package Client;

public class Channel {
    protected int id;
    protected String nom;
    protected String photo = null;

    public Channel(String name, int id, String photo) {
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
