package UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        DatagramSocket s = new DatagramSocket();
        InetAddress ip = InetAddress.getByName("localhost");
        int pt = 5000;
        Scanner sc = new Scanner(System.in);

        System.out.print("nombre: ");
        String n = sc.nextLine();

        new Thread(() -> {
            try {
                byte[] b = new byte[1024];
                while (!s.isClosed()) {
                    DatagramPacket p = new DatagramPacket(b, b.length);
                    s.receive(p);
                    String m = new String(p.getData(), 0, p.getLength());
                    System.out.println(m);
                }
            } catch (IOException e) {}
        }).start();

        String ini = n + " entro";
        byte[] bi = ini.getBytes();
        DatagramPacket pi = new DatagramPacket(bi, bi.length, ip, pt);
        s.send(pi);

        while (true) {
            String t = sc.nextLine();

            if (t.equals("salir")) {
                String m = n + " ha salido";
                byte[] bo = m.getBytes();
                DatagramPacket po = new DatagramPacket(bo, bo.length, ip, pt);
                s.send(po);
                s.close();
                System.exit(0);
            }

            String m = n + ": " + t;
            byte[] bo = m.getBytes();
            DatagramPacket po = new DatagramPacket(bo, bo.length, ip, pt);
            s.send(po);
        }
    }
}