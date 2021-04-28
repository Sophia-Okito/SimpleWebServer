package server;

import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@AllArgsConstructor
public class HttpRequestHandler implements Runnable {
    private final Socket clientSocket;

    @Override
    public void run() {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream outputStream = clientSocket.getOutputStream()) {

            // Get input data from client over socket
            String requestLine = bufferedReader.readLine();
            System.out.println("Receiving Message Header : " + requestLine + " | port: " + clientSocket.getPort());

            // Interpret request and respond accordingly
            if (requestLine == null)
                requestLine = "GET, /404, HTTP/1.1";
            String[] splitRequest = requestLine.split(" ");

            // Getting the requested route
            String fileName = splitRequest[1];

            // Getting the filePath
            String filePath = findRouteResources(fileName);

            // Setting the status code
            String statusCode =  filePath.equals("./src/main/resources/404.html") ? "404 Not Found": "200 OK";

            Path fileLocation = Paths.get(filePath);
            String contentType = Files.probeContentType(fileLocation);
            byte[] fileContent = Files.readAllBytes(fileLocation);

            // Build header
            outputStream.write(("HTTP/1.1 " + statusCode + "\r\n").getBytes());
            outputStream.write(("Content-Type: " + contentType + "\r\n").getBytes());
            outputStream.write(("\r\n").getBytes());
            outputStream.write(fileContent);
            outputStream.write(("\r\n" + "\r\n").getBytes());
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private  String findRouteResources( String route){
        return switch (route) {
            case "/", "/html" -> "./src/main/resources/2/index.html";
            case "/json" -> "./src/main/resources/generated.json";
            default -> "./src/main/resources/404.html";
        };
    }
}
