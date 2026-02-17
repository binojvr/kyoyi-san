# JGB Risk Engine

This repository implements the architecture defined in `architecture.md`.

## Components

- **kdb+ scripts (`q/`)**: table definitions, sample data, analytics functions, scenario runner.
- **Java orchestrator (`java/`)**: Maven project with a stubbed main class.
- **Python RAG service (`python/`)**: FastAPI app skeleton with requirements.

## Build & Run

### kdb+
Load the scripts from the `q` directory inside a q session:

```sh
cd q
q -q schema.q sample_data.q analytics.q
```

Use `q check_math.q` to validate functions.

### Java

The Java orchestrator now includes a simple console interface powered by LangChain4j. It reads natural-language scenario commands, uses an LLM to extract a parallel shift amount, and invokes the kdb+ scheduler. A record of each request is inserted into the `scenarios` table.

```
cd java
./mvnw clean install
# set OPENAI_API_KEY or another provider if you want real LLM parsing
java -cp target/jgb-risk-engine-0.1.0-SNAPSHOT.jar com.example.risk.JavaOrchestrator
```

Once running, type commands like `run a 1bp shift` or `apply five basis points` and press Enter. Type `quit` to exit.

### Python

```sh
cd python
python -m venv .venv
.\.venv\Scripts\activate
pip install -r requirements.txt
uvicorn app.main:app --reload
```

## Next Steps
- Flesh out Java IPC logic and scenario scheduler
- Implement Python RAG endpoints
- Expand q analytics with full pricing, convexity, DV01 validation
