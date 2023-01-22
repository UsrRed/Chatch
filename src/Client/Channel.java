package Client;

/**
 * @author : Eliott LEBOSSE et Yohann DENOYELLE
 * Cette classe permet de créer des Channel, aussi appelé "discussion" dans l'application.
 * Il s'agit simplement d'un objet stocké dans le menu déroulant de la fenêtre principale.
 * Il permet d'obtenir facilement les informations sur le channel (nom, id, etc...)
 */
public class Channel {
    protected int id;
    protected String nom;
    protected String photo = null;

    /**
     * Constructeur de la classe Channel
     *
     * @param name
     * @param id
     * @param photo
     */
    public Channel(String name, int id, String photo) {
        this.id = id;
        this.nom = name;
        this.photo = photo;
    }

    /**
     * @return l'id du channel (int)
     */
    public int getId() {
        return this.id;
    }

    /**
     * Transforme le Channel en tostring pour afficher son nom
     *
     * @return String
     */
    @Override
    public String toString() {
        return this.nom;
    }
}
