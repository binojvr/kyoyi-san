package com.example.risk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScenarioScheduler {
    private static final Logger log = LoggerFactory.getLogger(ScenarioScheduler.class);

    private final KdbClient kdb;

    public ScenarioScheduler(KdbClient kdb) {
        this.kdb = kdb;
    }

    public void runParallelShift(double bpShift) {
        try {
            String cmd = String.format("runScenario %f", bpShift);
            Object result = kdb.query(cmd);
            log.info("Scenario run result: {}", result);
        } catch (Exception e) {
            log.error("Error running scenario", e);
        }
    }
}
