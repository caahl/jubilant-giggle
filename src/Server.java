import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {
    private static int portNumber;
    private List<Socket> clients = new ArrayList<>();

    public static void main(String[] args) {
        getPortNumber(args);
        Server server = new Server();
        server.runServer();
    }

    public void runServer() {
        try (ServerSocket server = new ServerSocket(portNumber)) {
            while (true) {
                try {
                    Socket clientSocket = server.accept();
                    String host = clientSocket.getInetAddress().getHostName();
                    DataOutputStream remoteOut = new DataOutputStream(clientSocket.getOutputStream());
                    DataInputStream remoteIn = new DataInputStream(clientSocket.getInputStream());

                    synchronized (clients) {
                        clients.add(clientSocket);
                   /*     if (clients.size() == 1) {
                            System.out.println("Welcome! you're the first one here");
                        } else {
                            System.out.println("Welcome! you're the latest of " +
                                    clients.size() + " users.");
                        }*/
                    }
                    Thread t = new ClientHandler(clientSocket);
                    t.start();
                } catch (IOException e) {
                e.printStackTrace();
                }
            }
        } catch (IOException ex) {
            System.err.println("Couldn't start server");
        }
    }

    public static void getPortNumber(String[] args) {
        if (args.length == 0) {
            portNumber = 2000;
        } else {
            portNumber = Integer.parseInt(args[0]);
        }
    }

    synchronized List getClients() {
        return clients;
    }

    synchronized void removeFromClients(DataOutputStream remoteOut) {
        clients.remove(remoteOut);
    }

    public void killThread(ClientHandler clientHandler) {
        clientHandler.interrupt();
    }
}

/*
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server implements Runnable
{
  private Thread thread = new Thread(this);
  private ServerSocket ss;
  private Vector clientVector = new Vector();
  private static int port = 2000;
  private static boolean visible = true;

  public static void main(String[] paramArrayOfString) {
    if (paramArrayOfString.length != 0) {
      port = Integer.parseInt(paramArrayOfString[0]);
      if ((paramArrayOfString.length != 1) &&
        (paramArrayOfString[1].equals("nv"))) {
        visible = false;
      }
    }


    new Server();
  }

  public Server() {
    try {
      ss = new ServerSocket(port);
      setTitle(0);
    } catch (java.io.IOException localIOException) {
      System.out.println("FAILED: COULD NOT LISTEN ON PORT: " + port);
      System.exit(1);
    }

    thread.start();
  }

  public void run() {
    for (;;) {
      Socket localSocket = null;
      try {
        localSocket = ss.accept();
      } catch (java.io.IOException localIOException) {
        System.out.println("FAILED: ACCEPTING CLIENT");
      }
      addClient(localSocket);
    }
  }

  private synchronized void addClient(Socket paramSocket) {
    broadcast(paramSocket, "CLIENT CONNECTED: " + getHost(paramSocket));
    clientVector.addElement(paramSocket);
    setTitle(clientVector.size());
    new ClientHandler(paramSocket, this);
  }

  public synchronized void removeClient(Socket paramSocket) {
    clientVector.removeElement(paramSocket);
    broadcast(paramSocket, "CLIENT DISSCONNECTED: " + getHost(paramSocket));
    setTitle(clientVector.size());
  }

  public synchronized void broadcast(Socket paramSocket, String paramString) {
    System.out.println("CLIENT: " + getHost(paramSocket) + " BROADCAST: " + paramString);

    for (int i = 0; i < clientVector.size(); i++) {
      Socket localSocket = (Socket)clientVector.elementAt(i);
      sendToOne(localSocket, paramString);
    }
  }

  private synchronized void sendToOne(Socket paramSocket, String paramString) {
    try {
      java.io.PrintWriter localPrintWriter = new java.io.PrintWriter(paramSocket.getOutputStream(), true);
      localPrintWriter.println(paramString);
    } catch (java.io.IOException localIOException) {
      System.out.println("FAILED: SEND TO ONE");
    }
  }

  public synchronized void who(Socket paramSocket) {
    System.out.println("CLIENT: " + getHost(paramSocket) + " ASK: wwhhoo");

    for (int i = 0; i < clientVector.size(); i++) {
      Socket localSocket = (Socket)clientVector.elementAt(i);
      String str = getHost(localSocket);
      sendToOne(paramSocket, "WWHHOO: " + str);
    }
  }

  private String getHost(Socket paramSocket) {
    return paramSocket.getInetAddress().getHostName();
  }

  private String getHost(ServerSocket paramServerSocket) {
    try {
      paramServerSocket.getInetAddress();
      return java.net.InetAddress.getLocalHost().getHostName();
    } catch (java.net.UnknownHostException localUnknownHostException) {}
    return "FAILED: GET SERVER HOST";
  }

  private void setTitle(int paramInt)
  {
    System.out.println("SERVER ON: " + getHost(ss) + " - PORT: " + port + " - N CLIENTS: " + paramInt);
  }
}






import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static void main(String args[]) throws IOException {
        int portNumber = 2000;
        if(args.length > 0) {
            portNumber = Integer.parseInt(args[1]);
        }



        int number, temp;
        ServerSocket ss = new ServerSocket(1234);
        Socket s = ss.accept();
        Scanner sc = new Scanner(s.getInputStream());

        number = sc.nextInt();

        temp = number * 2;

        PrintStream p = new PrintStream(s.getOutputStream());
        p.println(temp);
    }

    public void run() {
        String msg;
        try {
            while ((msg = reader.readLine()) != null) {
                //... gör något med msg ...
            }
            writer.close();
            reader.close();
            socket.close();
        } catch (IOException e) {
        }
        server.killThread(this);
    }
}*/