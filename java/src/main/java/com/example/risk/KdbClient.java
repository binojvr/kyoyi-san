package com.example.risk;

import com.kx.c.K;
import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KdbClient implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(KdbClient.class);
    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    private K connection;

    /** Resolves KDB_HOST / KDB_PORT from .env (project root) or system environment. */
    public KdbClient(String host, int port) throws Exception {
        String actualHost = host != null ? host : dotenv.get("KDB_HOST", "localhost");
        int actualPort = port > 0 ? port : Integer.parseInt(dotenv.get("KDB_PORT", "5000"));
        log.info("Connecting to kdb+ at {}:{}", actualHost, actualPort);
        connection = new K(actualHost, actualPort);
    }

    public Object query(String q) throws Exception {
        return connection.k(q);
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}
