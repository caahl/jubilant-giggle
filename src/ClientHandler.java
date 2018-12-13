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
    private String user;


    ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;

        try {
            user = clientSocket.getInetAddress().getHostName();
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    @Override
    public void run() {
        String msg;
        try {
            while ((msg = in.readLine()) != null) {
                // msg = in.readLine();
                System.out.println(user + ": " + msg);
            }
            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException ex) {
            System.err.println(ex);
        }
//        server.killThread(this);
    }


}
