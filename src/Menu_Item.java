import java.util.ArrayList;

public class Menu_Item {
    protected int id_discussion;
    protected String nom;
    protected String photo = null;
    public Menu_Item(int id_discussion, String nom){
        this.id_discussion = id_discussion;
        this.nom = nom;
    }
    public Menu_Item(int id_discussion, String nom, String photo){
        this.id_discussion = id_discussion;
        this.nom = nom;
        this.photo = photo;
    }
    public Menu_Item(ArrayList array){
        this.id_discussion = (int) array.get(0);
        this.nom = (String) array.get(1);
        this.photo = (String) array.get(2);
    }
    public String getNom(){
        return this.nom;
    }
    public void setNom(String new_name){
        this.nom = new_name;
    }
    public int getId_discussion(){
        return this.id_discussion;
    }

    @Override
    public String toString() {
        return this.nom;
    }
}
