package TCP;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClienteHandler implements Runnable{

    private Socket socket;
    private ArrayList<ClienteHandler> clientHandlers = new ArrayList<>();
    private BufferedReader in;
    private BufferedWriter out;
    private String nombreCliente;


    public ClienteHandler(Socket socket){
        try{
            this.socket = socket;
            this.in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            this.out = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
            this.nombreCliente = in.readLine();
            clientHandlers.add(this);
            mandarMensaje("SERVER: SE HA UNIDO EL CLIENTE: " + nombreCliente);
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void quitarClienteHandler() {
        clientHandlers.remove(this);
        mandarMensaje("SERVER: SE HA IDO EL CLIENTE: " + nombreCliente);
    }

    @Override
    public void run() {
        String mensajeDesdeCliente;
        while (socket.isConnected()) {
            try {
                mensajeDesdeCliente = in.readLine();
                //mandamos mensaje
                mandarMensaje(mensajeDesdeCliente);
            } catch (IOException e) {
                e.printStackTrace();
                //cerrar comunicación
                cerrarComunicacion(socket,in,out);
            }
        }
    }

    public void mandarMensaje(String mensaje) {
        for (ClienteHandler clienteHandler : clientHandlers) {
            try {
                clienteHandler.out.write(mensaje);
                clienteHandler.out.newLine();
                clienteHandler.out.flush();
            } catch (IOException e) {
                e.printStackTrace();
                cerrarComunicacion(socket,in,out);
            }
        }
    }

    public void cerrarComunicacion(Socket socket, BufferedReader in, BufferedWriter out) {
        quitarClienteHandler();
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
}
