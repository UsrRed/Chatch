package Serveur;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
/**
 * @author : Eliott LEBOSSE et Yohann DENOYELLE
 * Cette classe permet d'initialiser le serveur.
 * Elle permet aussi de récupérer les informations de connexion à la base de données qui sont stockées dans un fichier properties.
 */
public class Serveur {

    private static final int PORT = 2009; // socket port
    private ServerSocket server = null;
    private Socket socket = null;

    public Serveur() throws IOException {
        // Connection a la Database avec le fichier properties
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src/Serveur/properties/configuration.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // get the settings
        String host = properties.getProperty("db_host");
        String user = properties.getProperty("db_user");
        String pwd = properties.getProperty("db_pwd");
        String dbname = properties.getProperty("db_name");
        String port = properties.getProperty("db_port");

        Database data = new Database(host, port, user, pwd, dbname);
        data.connect();

        server = new ServerSocket(PORT);
        Boolean state = true;
        while (state) {
            System.out.println("Attente Client");
            socket = server.accept();
            Traitement_client t1 = new Traitement_client(socket, data, PORT);
            t1.start();
        }
        // déconnexion de la BDD
        data.disconnect();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new Serveur();

    }

}