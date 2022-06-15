package httpserver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

    static List<String> fileLines = new ArrayList<>();
    public static List<String> readFile(String file) {
        try {
            BufferedReader lector = new BufferedReader(new java.io.FileReader(file));
            String linea = lector.readLine();
            while (linea != null){
                fileLines.add(linea);
                linea = lector.readLine();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileLines;
    }
}
