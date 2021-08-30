package nextstep.jwp;

import nextstep.jwp.framework.manager.RequestManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class WebServer {

    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);

    private static final int DEFAULT_PORT = 8080;

    private final int port;
    private final RequestManager requestManager;

    public WebServer(int port, RequestManager requestManager) {
        this.port = checkPort(port);
        this.requestManager = requestManager;
    }

    public static int defaultPortIfNull(String[] args) {
        return Stream.of(args)
                .findFirst()
                .map(Integer::parseInt)
                .orElse(WebServer.DEFAULT_PORT);
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Web Server started {} port.", serverSocket.getLocalPort());
            handle(serverSocket);
        } catch (IOException exception) {
            logger.error("Exception accepting connection", exception);
        } catch (RuntimeException exception) {
            logger.error("Unexpected error", exception);
        }
    }

    private void handle(ServerSocket serverSocket) throws IOException {
        ExecutorService service = Executors.newFixedThreadPool(200);

        Socket connection;
        while ((connection = serverSocket.accept()) != null) {
            service.submit(new RequestHandler(connection, requestManager));
        }
    }

    private int checkPort(int port) {
        if (port < 1 || 65535 < port) {
            return DEFAULT_PORT;
        }
        return port;
    }
}