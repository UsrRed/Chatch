package tools;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;


public class Connect {
    private Socket socket = null;
    private OutputStream out = null;
    private ObjectOutputStream writer = null;
    private Connection_format msg = null;

    public Connect(int PORT, InetAddress address, int id_utilisateur, String password) throws IOException {
        msg = new Connection_format(id_utilisateur, password);
        try {
            socket =new Socket(address, PORT);
            System.out.println("connecté "+ socket);
            out = socket.getOutputStream();
            writer = new ObjectOutputStream(out);
        } catch ( IOException e ) {
            throw new RuntimeException(e);
        }
        send(Connection_Codes.CONNEXION);
    }
    public void send(Connection_Codes code, ArrayList<String> message) throws IOException {
        try {
            msg.setMessage(code, message);
            writer.writeObject(msg);
            writer.flush();
        } catch ( IOException e ) {
            throw new RuntimeException(e);
        }
    }
    public void send(Connection_Codes code) throws IOException {
        try {
            msg.setMessage(code);
            writer.writeObject(msg);
            writer.flush();
        } catch ( IOException e ) {
            throw new RuntimeException(e);
        }
    }

}
