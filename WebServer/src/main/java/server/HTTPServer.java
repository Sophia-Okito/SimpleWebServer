package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTPServer {
    public static void main(String[] args) throws IOException {
        // Listen for client connection requests on this server socket
        final ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Server open, ready to accept connections...");

        while (true) {
            // Waits until client requests a connection, then returns a connection (socket)
            Socket clientSocket = serverSocket.accept();
            System.out.println("Connected..." + clientSocket.toString());

            // Runnable ConnectionHandler
            final HttpRequestHandler request = new HttpRequestHandler(clientSocket);
            Thread thread = new Thread(request);
            thread.start();

        }

    }
}
