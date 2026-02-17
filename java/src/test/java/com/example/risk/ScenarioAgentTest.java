package com.example.risk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScenarioAgentTest {

    @Test
    void parseShift_returnsZero_whenInputUnclear() {
        ScenarioAgent agent = new ScenarioAgent(new MockLlm());
        double val = agent.parseShift("something irrelevant");
        assertEquals(0.0, val, 1e-12);
    }

    @Test
    void parseShift_parsesNumericBp() {
        ScenarioAgent agent = new ScenarioAgent(new MockLlm());
        double val = agent.parseShift("run a 2bp shift");
        assertEquals(0.0002, val, 1e-12);
    }

    // simple mock that just returns a fixed response based on prompt
    static class MockLlm implements io.github.langchain4j.Llm {
        @Override
        public io.github.langchain4j.Response apply(String input) {
            String out;
            if (input.contains("2bp")) {
                out = "0.0002";
            } else {
                out = "0.0";
            }
            return io.github.langchain4j.Response.builder().output(out).build();
        }
    }
}
