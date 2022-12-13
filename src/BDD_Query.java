import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class BDD_Query {
    // la BDD utilisé par la requête
    protected Database BDD;
    // le résultat donné par la requête est stocké ici
    protected ArrayList<Object> result = new ArrayList<>();
    // les types de colonnes sont stockés dans cette variable
    protected ArrayList<Object> types = new ArrayList<>();

    /**
     * Constructeur de la BDD_Query
     * @param BDD Base de donnée utilisée de type Database
     */
    public BDD_Query(Database BDD){
        this.BDD = BDD;
    }

    /**
     * Constructeur de la BDD_Query
     * @param BDD Base de donnée utilisée de type Database
     * @param query la commande SQL a executer
     */
    public BDD_Query(Database BDD, String query){
        this.BDD = BDD;
        setQueryAsk(query);
    }

    /**
     * /!\ Ne retourne rien, l'enregistre dans l'objet
     * @param query la commande SQL a executer
     */
    public void setQueryAsk(String query){
        try {
            types = new ArrayList<>();
            result = new ArrayList<>();
            ResultSet res = BDD.Query(query);
            ResultSetMetaData meta = res.getMetaData();
            int NbColumn = meta.getColumnCount();
            for (int i=1; i<=NbColumn; i++){
                try {
                    types.add(meta.getColumnLabel(i));
                } catch (SQLException e){
                    types.add(null);
                }
            }
            // construction d'une ArrayList constitué de sub ArrayList (réponse SQL de plusieurs lignes)
            while (res.next()){
                ArrayList<Object> sub = new ArrayList<>();
                for (int i = 1; i <= NbColumn; i++) {
                    try {
                        sub.add(res.getObject(i));
                    } catch (SQLException e) {
                        sub.add(null);
                    }

                }
                result.add(sub);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retourne un boolean pour valider ou non l'opération
     * @param query la commande SQL a executer
     */
    public boolean setQueryExecute(String query) {
        return BDD.Do(query);
    }
    /**
     * Peux être appelé plusieurs fois sans refaire de requête
     * @return Une ArrayList contenant les résultats de la requête
     */
    public ArrayList<Object> getQueryResult() {
        return result;
    }
    /**
     * Peux être appelé plusieurs fois sans refaire de requête
     * @return Une ArrayList contenant l'en-tête de la requête
     */
    public ArrayList<Object> getQueryHeader() {
        return types;
    }

    @Override
    public String toString() {
        StringBuilder prt = new StringBuilder();
        if (result != null) {
            for (Object type : types) {
                prt.append("\t|\t").append(type);
            }
            prt.append("\n");
            for (Object value : result) {
                ArrayList sub = (ArrayList) value;
                for (Object o : sub) {
                    prt.append("\t|\t").append(o);
                }
                prt.append("\n");
            }
        } else {
            prt.append("Pas de requête saisie !");
        }
        return prt.toString();
    }
}
