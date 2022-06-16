package httpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class RequestProcesor implements Runnable{

    private final Socket clientSocket;

    public RequestProcesor(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            process();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void process() throws Exception {
        System.out.println("Hilo: " + Thread.currentThread());
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        HttpServerWriter writer = new HttpServerWriter();
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

}
