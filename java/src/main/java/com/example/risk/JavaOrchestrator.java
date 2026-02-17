package com.example.risk;

import com.kx.c.K;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaOrchestrator {
    private static final Logger log = LoggerFactory.getLogger(JavaOrchestrator.class);

    public static void main(String[] args) {
        log.info("Starting JGB Risk Engine orchestrator...");

        try (KdbClient kdb = new KdbClient("localhost", 5000)) {
            ScenarioScheduler scheduler = new ScenarioScheduler(kdb);
            ScenarioAgent agent = ScenarioAgent.defaultAgent();

            // simple console-based loop to accept user instructions
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
            String line;
            log.info("Enter scenario commands (type 'quit' to exit)");
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) break;
                double shift = agent.parseShift(line);
                log.info("Executing parallel shift of {} (from '{}')", shift, line);
                scheduler.runParallelShift(shift);
                // optionally write a record into the scenarios table via kdb
                String logCmd = String.format("`scenarios insert (uuid[];`parallel;%f;.z.p)", shift);
                kdb.query(logCmd);
            }
        } catch (Exception e) {
            log.error("Failed to initialize kdb client", e);
        }

        log.info("Orchestrator exiting");
    }
}
