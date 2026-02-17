# JGB Risk Engine

A modular, institutional‑grade risk engine for Japanese Government Bonds (JGBs) built around kdb+, Java, and Python.

The design follows the layered architecture described in `architecture.md`, taking into account the project objectives from `claude.md` (fixed‑rate JGBs, yield curve risk, DV01, scenario engine, etc.) and assumes a local kdb+ instance running on `localhost:5000` (see `q/q.md`).

Key features:

- Bond pricing, duration, convexity, DV01 in vectorized q functions
- Scenario engine supporting parallel shifts (with room for steepener/flattener)
- Portfolio P&L aggregation based on positions
- Java orchestrator using LangChain4j to accept natural‑language commands
- Python RAG service (FastAPI + ChromaDB) for research lookups

Components

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
