package TCP;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private String nombreCliente;

    public Client(Socket socket, String nombreCliente) {
        try {
            this.socket = socket;
            this.nombreCliente = nombreCliente;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            //Cerrar
            cerrarComunicacion(socket, in, out);
        }

    }

    public void cerrarComunicacion(Socket socket, BufferedReader in, BufferedWriter out) {
        try {
            if(socket != null) {
                socket.close();
            }
            if(in != null) {
                in.close();
            }
            if(out != null) {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviarMensaje() {
        try {
            out.write(nombreCliente);
            out.newLine();
            out.flush();
            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()){
                String mensaje = scanner.next();
                out.write(nombreCliente + ": " + mensaje);
                out.newLine();
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            cerrarComunicacion(socket, in, out);
        }
    }

    public void escucharMensaje() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String mensajeDesdeChat;
                while (socket.isConnected()){
                    try {
                        mensajeDesdeChat = in.readLine();
                        System.out.println(mensajeDesdeChat);
                    } catch (Exception e) {
                        e.printStackTrace();
                        cerrarComunicacion(socket, in, out);
                    }
                }
            }
        }).start();
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce tu nombre: ");
        String nombreCliente = scanner.next();

        Socket socket = new Socket("localhost", 5000);
        Client cliente = new Client(socket, nombreCliente);
        cliente.escucharMensaje();
        cliente.enviarMensaje();
    }
}
