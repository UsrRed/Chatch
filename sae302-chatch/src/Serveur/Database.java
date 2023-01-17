package Serveur;

import java.sql.*;
import java.sql.Connection;
import java.util.ArrayList;

public class Database {
    protected String ip;
    protected String port;
    protected String identifiant;
    protected String password;
    protected Connection conn = null;

    /**
     * Crée l'objet BDD
     *
     * @param ip          adresse ip destination en String
     * @param port        port utilisé en String
     * @param identifiant identifiant en String
     * @param password    password de connection BDD en String
     */
    public Database(String ip, String port, String identifiant, String password) {
        // TODO Connexion with properties file
        this.ip = ip;
        this.port = port;
        this.identifiant = identifiant;
        this.password = password;
    }

    /**
     * Permet d'effectuer des requêtes sans arguments
     *
     * @param query requête a faire
     * @return ArrayList avec tous les résultats (dur a manipuler)
     */
    public ResultSet Query(String query) {
        try {
            Statement statement = conn.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Permet d'effectuer des requêtes sans arguments
     *
     * @param query requête a faire
     * @return ArrayList avec tous les résultats (dur a manipuler)
     */
    public boolean Do(String query) {
        try {
            Statement statement = conn.createStatement();
            return statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Connection a la base de donnée
     */
    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + identifiant + "", identifiant, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Déconnection de la BDD
     */
    public void disconnect() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean state() {
        return conn == null;
    }

    @Override
    public String toString() {
        if (conn != null) {
            return conn.toString();
        } else {
            return "Connection non établie...";
        }
    }
}
