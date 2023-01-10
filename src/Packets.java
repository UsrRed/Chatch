
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Packets extends Thread {

    private Socket socket = null;

    public Packets(Socket socket) {

        this.socket = socket;
    }

    public void run() {
        Message message = null;
        InputStream inputStream = null;
        try {
            inputStream = socket.getInputStream();
            ObjectInputStream reader = null;
            reader = new ObjectInputStream(inputStream);
        do {
            System.out.println("Attente message");
            try {
                message = (Message) reader.readObject();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
            System.out.println(message);
            if (!message.getContenu().equals("kill")) {
                System.exit(0);
            } else {
            }
        } while (!message.getContenu().equals("exit"));
        socket.close();
        System.out.println("deconnexion");
    } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
