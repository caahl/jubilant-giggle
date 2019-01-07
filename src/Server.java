import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {
    private List<Socket> clients = new ArrayList<>();
    private ServerSocket server;
    private static int portNumber;
    private Thread thread = new Thread(this);

    public Server(int portNumber) {
        try {
            server = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.out.println("Error: cannot connect to port: " + portNumber);
        }
        thread.start();
    }

    public void run() {
        Socket socket = null;
        while (true) {
            try {
                socket = server.accept();
            } catch (IOException e) {
                System.out.println("FAILED: ACCEPTING CLIENT");
            }
            addClient(socket);
        }
    }

    public static void main(String[] args) {
        if (args.length != 0) {
            portNumber = Integer.parseInt(args[0]);
        } else {
            portNumber = 2000;
        }
        new Server(portNumber);
    }


    private synchronized void addClient(Socket paramSocket) {
        broadcast(paramSocket, "CLIENT CONNECTED: " + getClientHost(paramSocket));
        clients.add(paramSocket);
        setTitle(clients.size());
        new ClientHandler(paramSocket, this);
    }

    public synchronized void removeClient(Socket paramSocket) {
        clients.remove(paramSocket);
        broadcast(paramSocket, "CLIENT DISSCONNECTED: " + getClientHost(paramSocket));
        setTitle(clients.size());
    }

    public synchronized void broadcast(Socket paramSocket, String paramString) {
        System.out.println("CLIENT: " + getClientHost(paramSocket) + " BROADCAST: " + paramString);
        for (Socket socket : clients) {
          //  Socket localSocket = clients.get(i);
            sendToOne(socket, paramString);
        }
    }

    public synchronized void sendToOne(Socket paramSocket, String paramString) {
        try {
            PrintWriter localPrintWriter = new PrintWriter(new OutputStreamWriter(paramSocket.getOutputStream(), "ISO-8859-1"), true);
            localPrintWriter.println(paramString);
        } catch (java.io.IOException localIOException) {
            System.out.println("FAILED: SEND TO ONE");
        }
    }

    private String getServerHost(ServerSocket paramServerSocket) {
        try {
            return paramServerSocket.getInetAddress().getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "Cannot retrieve Server Socket Address";
        }
    }

    private String getClientHost(Socket paramSocket) {
        return paramSocket.getInetAddress().getHostName();
    }

    private void setTitle(int paramInt) {
        System.out.println("SERVER HOST: " + getServerHost(server) + " - PORT: " + portNumber + " - CONNECTED: " + paramInt);
    }

    public void killThread(ClientHandler clientHandler) {
        clientHandler.interrupt();
    }
}
