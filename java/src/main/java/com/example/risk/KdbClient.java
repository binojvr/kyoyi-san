package com.example.risk;

import com.kx.c.K;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KdbClient implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(KdbClient.class);
    private K connection;

    /**
     * Host and port can be supplied via environment variables KDB_HOST/KDB_PORT;
     * otherwise defaults to localhost:5001 (Phase2 risk data engine).
     */
    public KdbClient(String host, int port) throws Exception {
        String actualHost = host != null ? host : System.getenv().getOrDefault("KDB_HOST", "localhost");
        int actualPort = port > 0 ? port : Integer.parseInt(System.getenv().getOrDefault("KDB_PORT", "5001"));
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
