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
