package Client;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public class Client {

    public static void main(String[] args) throws IOException {
        // test connection TheBaye usr : TheBaye ; pass : 1234
        // fichier properties
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src/Client/properties/configuration.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // get the settings
        try {
            // socket host
            InetAddress host = InetAddress.getByName(properties.getProperty("server_host"));
            // socket port
            int port = Integer.parseInt(properties.getProperty("server_port"));
            new Interface_Connection(host, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}

