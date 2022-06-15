package httpserver;

import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class HttpServer {
    private static HttpServer _instance = new HttpServer();

    public HttpServer() {
    }

    public static HttpServer getInstance(){
        return _instance;
    }

    public static void start(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        boolean running = true;
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine;

            String path = "";
            boolean firstLine = true;

            while ((inputLine = in.readLine()) != null) {
                if (firstLine){
                    path = inputLine.split(" ")[1].substring(1);
                    System.out.println("Path: " + path);
                    URI resource = new URI(path);
                    System.out.println("Path: " + resource.getPath());
                    firstLine = false;
                }
                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }

            outputLine = "HTTP/1.1 200 OK\r\n";
            outputLine += "Content-Type: text/html\r\n";
            outputLine +="\r\n";
            outputLine += new String(Files.readAllBytes(Paths.get("resources/" + path)), StandardCharsets.UTF_8);

            out.println(outputLine);

            out.close();

            in.close();

            clientSocket.close();
        }
        serverSocket.close();
    }
}
