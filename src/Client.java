
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private Socket socket = null;



    public Client() throws IOException {

        InetAddress adr = InetAddress.getLocalHost();
        socket = new Socket(adr, 10000);
        System.out.println("connecté " + socket);
        OutputStream out = socket.getOutputStream();
        ObjectOutputStream writer = new ObjectOutputStream(out);
        Message msg = null;
        // changer la boucle while pour détecter quand un message est envoyé avec un listener de exit
        Boolean state = true;
        while (state) {
            // get le messsage
            //msg = new Message();
            writer.writeObject(msg);
            writer.flush();
        }
        socket.close();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new Client();
    }

}
