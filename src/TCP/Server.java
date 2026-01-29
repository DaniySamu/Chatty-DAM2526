package TCP;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void iniciarServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("Se ha conectado un nuevo cliente");
                ClienteHandler clienteHandler = new ClienteHandler(socket);
                Thread hilo = new Thread(clienteHandler);
                hilo.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            cerrarServer();
        }
    }

    public void cerrarServer() {
        try {
            if (!serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
         ServerSocket serverSocket1 = new ServerSocket(5000);
         Server server = new Server(serverSocket1);
         server.iniciarServer();
    }
}
