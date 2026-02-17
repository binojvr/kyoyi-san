package com.example.risk;

import com.kx.c.K;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KdbClient implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(KdbClient.class);
    private K connection;

    public KdbClient(String host, int port) throws Exception {
        log.info("Connecting to kdb+ at {}:{}", host, port);
        connection = new K(host, port);
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
