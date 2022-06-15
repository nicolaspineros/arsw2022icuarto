package httpserver;

import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

import java.util.List;

public class HttpServer {
    private static HttpServer _instance = new HttpServer();

    public HttpServer() {
    }

    public static HttpServer getInstance(){
        return _instance;
    }

    public static void start(String[] args) throws  Exception {
        ServerSocket serverSocket = null;
        HttpServerWriter writer = new HttpServerWriter();
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

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine,path="";
            boolean firstLine = true;

            while ((inputLine = in.readLine()) != null) {
                if (firstLine){
                    path = inputLine.split(" ")[1];
                    System.out.println("Path: " + path);
                    firstLine = false;
                }
                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }
            writer.writer(path,clientSocket);
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }
}
