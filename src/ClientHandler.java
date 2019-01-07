import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientHandler extends Thread {
    private Socket clientSocket;
    private Server server;
    private BufferedReader in;
    private PrintWriter out;


    ClientHandler(Socket clientSocket, Server server) {
        this.server = server;
        this.clientSocket = clientSocket;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        start();
    }

    public void run() {
        String msg;
        try {
            while ((msg = in.readLine()) != null && !msg.equalsIgnoreCase("exit")) {

                server.broadcast(clientSocket, msg);
            }
            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException ex) {
            System.err.println(ex);
        }
        server.removeClient(clientSocket);

    }
}
