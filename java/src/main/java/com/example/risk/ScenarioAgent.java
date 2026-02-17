package com.example.risk;

import io.github.langchain4j.Llm;
import io.github.langchain4j.chain.Chain;
import io.github.langchain4j.chain.PromptTemplate;
import io.github.langchain4j.model.openai.OpenAiApi;
import io.github.langchain4j.model.openai.OpenAiModel;
import io.github.langchain4j.model.openai.OpenAiModelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScenarioAgent {

    private static final Logger log = LoggerFactory.getLogger(ScenarioAgent.class);
    private final Chain chain;

    public ScenarioAgent(Llm llm) {
        // create a simple chain that maps natural language to a numeric shift value
        PromptTemplate template = PromptTemplate.fromTemplate(
                "You are an assistant for a JGB risk engine.\n" +
                "Extract the parallel shift in the yield curve (in decimal form) from the user's instruction.\n" +
                "Examples:\n" +
                "  'run a 1bp parallel shift' -> 0.0001\n" +
                "  'apply a five basis point bump' -> 0.0005\n" +
                "  'no shift' -> 0.0\n" +
                "Input: {input}\n" +
                "Output:" );
        chain = Chain.fromPromptTemplate(llm, template);
    }

    /**
     * Returns the shift amount (e.g. 0.0001 for 1bp) or 0.0 if parsing fails.
     */
    public double parseShift(String userInstruction) {
        try {
            String response = chain.run(userInstruction).getOutput().trim();
            log.info("Agent parsed '{}' -> {}", userInstruction, response);
            return Double.parseDouble(response);
        } catch (Exception e) {
            log.warn("Failed to parse shift from '{}': {}", userInstruction, e.getMessage());
            return 0.0;
        }
    }

    public static ScenarioAgent defaultAgent() {
        // environment variable OPENAI_API_KEY must be set for real usage
        Llm llm = new OpenAiApi(OpenAiModelType.GPT_4);
        return new ScenarioAgent(llm);
    }
}
