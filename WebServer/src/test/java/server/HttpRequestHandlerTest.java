package server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;

class HttpRequestHandlerTest {
    ServerSocket serverSocket;
    HttpRequestHandler handler;

    @BeforeEach
    void setUp() throws IOException {
        try {
           serverSocket = new ServerSocket(8080);
            Socket clientSocket = serverSocket.accept();
            HttpRequestHandler request = new HttpRequestHandler(clientSocket);
            request.run();

        }catch (Exception e){
            e.printStackTrace();
        }

//
    }

    @Test
    void indexTest(){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/"))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String expected  = Files.readString(Paths.get("./src/main/resources/2/index.html"))
                    .replaceAll("\\s", "");
            Assertions.assertAll(
                    () -> Assertions.assertEquals(expected, response.body().replaceAll("\\s", "")),
                    () -> Assertions.assertEquals(200, response.statusCode())
            );
        } catch (IOException | InterruptedException ioException) {
            ioException.printStackTrace();
        }
    }
    @Test
    void jsonTest(){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/json"))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String expected  = Files.readString(Paths.get("./src/main/resources/generated.json"))
                    .replaceAll("\\s", "");
            Assertions.assertAll(
                    () -> Assertions.assertEquals(expected, response.body().replaceAll("\\s", "")),
                    () -> Assertions.assertEquals(200, response.statusCode())
            );
        } catch (IOException | InterruptedException ioException) {
            ioException.printStackTrace();
        }
    }
    @Test
    void notFoundTest(){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/not"))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String expected  = Files.readString(Paths.get("./src/main/resources/404.html"))
                    .replaceAll("\\s", "");
            Assertions.assertAll(
                    () -> Assertions.assertEquals(expected, response.body().replaceAll("\\s", "")),
                    () -> Assertions.assertEquals(404, response.statusCode())
            );
        } catch (IOException | InterruptedException ioException) {
            ioException.printStackTrace();
        }
    }
}