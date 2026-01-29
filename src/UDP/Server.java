package UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args) throws IOException {
        DatagramSocket s = new DatagramSocket(5000);
        ArrayList<String> c = new ArrayList<>();
        byte[] b = new byte[1024];

        System.out.println("servidor iniciado");

        while (true) {
            DatagramPacket p = new DatagramPacket(b, b.length);
            s.receive(p);

            String m = new String(p.getData(), 0, p.getLength());
            InetAddress ip = p.getAddress();
            int pt = p.getPort();
            String id = ip.getHostAddress() + ":" + pt;

            System.out.println(m);

            if (m.trim().endsWith(" ha salido")) {
                c.remove(id);
                System.out.println("cliente eliminado: " + id);
            } else {
                if (!c.contains(id)) {
                    c.add(id);
                    System.out.println("nuevo cliente registrado: " + id);
                }
            }

            for (String u : c) {
                String[] d = u.split(":");
                InetAddress ip_d = InetAddress.getByName(d[0]);
                int pt_d = Integer.parseInt(d[1]);
                byte[] bf = m.getBytes();
                DatagramPacket env = new DatagramPacket(bf, bf.length, ip_d, pt_d);
                s.send(env);
            }
        }
    }
}