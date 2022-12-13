import java.net.InetAddress;
import java.net.UnknownHostException;

public class Connection {
    public static void main(String[] args) {
        try {
            InetAddress adrLocale = InetAddress.getLocalHost();
            System.out.println("Adresse locale = " + adrLocale.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
