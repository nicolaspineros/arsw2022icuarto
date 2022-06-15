package httpserver;

import java.io.IOException;
import java.net.URISyntaxException;

public class HttpServerController {

    public static void main(String[] args) {
        HttpServer svr = HttpServer.getInstance();
        try {
            svr.start(args);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
