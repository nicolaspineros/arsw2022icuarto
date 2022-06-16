package httpserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class HttpServerWriter {

    static String file,outputLine,fileType;

    static List<String> typeText = new ArrayList<String>(){
        {
            add("html");
            add("js");
            add("css");
        }
    };

    static List<String> typeImage = new ArrayList<String>(){
        {
            add("png");
            add("jpg");
            //add("ico");
        }
    };
    static Socket clientSocket;

    public static void writer(String path, Socket clientS) throws Exception {
        file = path;
        fileType = path.split("\\.")[1];
        System.out.println(fileType);
        clientSocket = clientS;
        if (typeText.contains(fileType)){
            writeText();
        } else if (typeImage.contains(fileType)) {
            writeImage();
        } else { System.err.println("Typo de archivo no encontrado");
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(error);
            //throw new Exception("Typo de archivo no encontrado");
        }
    }

    public static void writeText(){
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            outputLine = "HTTP/1.1 200 OK\r\n";
            outputLine += "Content-Type: text/"+ fileType + "\r\n";
            outputLine +="\r\n";
            outputLine += new String(Files.readAllBytes(Paths.get("resources" + file)), StandardCharsets.UTF_8);
            out.println(outputLine);
            out.close();
        } catch (IOException e) {
            System.err.println("No se pudo leer la ruta especificada");
        }
    }

    public static void writeImage(){
        try {
            File image = new File("resources" + file);
            FileInputStream filein = new FileInputStream(image);
            byte[] data = new byte[(int) image.length()];
            filein.read(data);
            filein.close();
            DataOutputStream binaryOut = new DataOutputStream(clientSocket.getOutputStream());
            binaryOut.writeBytes("HTTP/1.0 200 OK\r\n");
            binaryOut.writeBytes("Content-Type: image/" + fileType + "\r\n");
            binaryOut.writeBytes("Content-Length: " + data.length);
            binaryOut.writeBytes("\r\n\r\n");
            binaryOut.write(data);
            binaryOut.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static String error = "HTTP/1.0 404 Not Found"
            + "Content-Type: text/html\r\n"
            + "\r\n"
            + "<!DOCTYPE html>"
            + "<html>"
            + "<head>"
            + "<meta charset=\"UTF-8\">"
            + "<title>ERROR</title>\n" + "</head>"
            + "<body>"
            + "<h1> Error 404 </h1>"
            + "My Web Site"
            + "</body>"
            + "</html>";
}
