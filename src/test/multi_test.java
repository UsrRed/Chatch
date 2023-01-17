package test;

import java.io.IOException;

import static java.lang.Thread.sleep;

public class multi_test {
    public static void main(String[] args) throws InterruptedException {
        // execute Server.Serveur.main as Thread
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Serveur.Serveur.main(null);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        // execute Client.Client.main as Thread
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Client.Client.main(null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        sleep(7000);
        t2.start();
        t2.start();
    }
}
