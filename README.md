# JGB Risk Engine (Kyoyi-san)

A modular, institutional-grade risk engine for Japanese Government Bonds (JGBs) built around kdb+, Java, and Python.

## Architecture

Three-tier design with an AI orchestration layer:

```
User (natural language)
        │
        ▼
[Java Orchestrator]  ←  LangChain4j + OpenAI GPT-4
        │                 parses shift magnitude from text
        ▼
[kdb+ Analytics Engine]  ←  in-memory columnar store
        │                    bond math, scenario P&L
        ▼
[Python REST/RAG Service]  ←  FastAPI + ChromaDB
                               exposes results, macro narratives
```

See [architecture.md](architecture.md) for the full layered design.

## Business Logic

All analytics use **Act/365** day count (Japanese market standard).

| Metric | Method |
|--------|--------|
| Bond price | PV of annual coupons + face value redemption, discounted at YTM |
| Macaulay duration | Weighted average time to cashflows |
| Convexity | Second-order price/yield curvature |
| DV01 | Finite-difference approximation over 1bp shift |
| Parallel shift scenario | Uniform Δy applied to all tenors; portfolio repriced |
| Portfolio P&L | `(newPrice − oldPrice) × quantity` aggregated across positions |

## Components

### kdb+ (`q/`)

| File | Purpose |
|------|---------|
| `schema.q` | Table definitions: `bonds`, `yields`, `portfolios`, `scenarios` |
| `sample_data.q` | Sample 2Y/5Y/10Y JGBs, yield curve, portfolio positions |
| `analytics.q` | Core functions: `price`, `duration`, `convexity`, `dv01`, `parallelShift`, `runScenario` |
| `run_example.q` | Loads all scripts and runs a +1bp scenario end-to-end |
| `check_math.q` | Unit tests validating pricing correctness |

### Java (`java/`)

| File | Purpose |
|------|---------|
| `JavaOrchestrator.java` | CLI REPL — reads commands, drives the orchestration loop |
| `ScenarioAgent.java` | LangChain4j + GPT-4 — parses `"run a 5bp shift"` → `0.0005` |
| `ScenarioScheduler.java` | Invokes `runScenario` in kdb+, logs result to `scenarios` table |
| `KdbClient.java` | TCP IPC wrapper to kdb+ (default `localhost:5001`) |

### Python (`python/`)

| File | Purpose |
|------|---------|
| `app/main.py` | FastAPI root; mounts routers; `/health` endpoint |
| `app/endpoints.py` | `/explain-risk`, `/interpret-macro`, `/narrative-scenario` |
| `app/kdb_client.py` | qpython wrapper to query kdb+ from Python |

## Build & Run

### kdb+

```sh
# Load and run interactively
q -q q/schema.q q/sample_data.q q/analytics.q

# Run end-to-end example (+1bp parallel shift)
q q/run_example.q

# Validate pricing math
q check_math.q
```

### Java

Requires: Java 21, Maven, kdb+ running on port 5001.

```sh
cd java
./mvnw clean install
export OPENAI_API_KEY=<your-key>
java -cp target/jgb-risk-engine-0.1.0-SNAPSHOT.jar com.example.risk.JavaOrchestrator
```

Type commands such as `run a 1bp shift` or `apply five basis points`. Type `quit` to exit.

### Python

Requires: Python 3.10+, kdb+ running on port 5001.

```sh
cd python
python -m venv .venv
source .venv/bin/activate          # Windows: .\.venv\Scripts\activate
pip install -r requirements.txt
uvicorn app.main:app --reload --port 8001
```

Endpoints:

| Endpoint | Description |
|----------|-------------|
| `GET /health` | Liveness check |
| `GET /explain-risk?amount=0.0001` | Runs scenario in kdb+, returns position P&L |
| `GET /interpret-macro` | Macro RAG lookup (ChromaDB + LangChain) |
| `GET /narrative-scenario` | BoJ scenario narrative text generation |

## Scope (Phase 1)

- Fixed-rate JGBs only
- Yield curve risk only (no credit, FX, or derivatives)
- No external market data feeds
- No production deployment

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Analytics | kdb+/q |
| Orchestration | Java 21, LangChain4j |
| LLM | OpenAI GPT-4 |
| kdb+ IPC (Java) | kx-kdb 3.6.0 |
| REST service | FastAPI + uvicorn |
| Vector DB | ChromaDB |
| kdb+ IPC (Python) | qpython |
| Testing | JUnit 5, pytest |
