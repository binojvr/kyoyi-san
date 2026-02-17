# Project: JGB Risk Engine using kdb+

Objective:
Design and implement a modular institutional-grade risk engine
for Japanese Government Bonds (JGBs).

## Target Environment:
Investment bank markets technology stack.

## Scope:
- Fixed-rate JGBs only
- Yield curve risk only
- No credit risk
- No derivatives (Phase 1)

## Core Features:
- Bond pricing
- Duration & Modified Duration
- Convexity
- DV01
- Scenario engine (parallel, steepener, flattener)
- Portfolio P&L aggregation

## Non-Goals:
- No GUI initially
- No production deployment
- No external market data integration

## Output should include:
- q table definitions
- Sample data
- Modular functions
- Example scenario run
- Sample portfolio output

## Build & Test Commands
- Java: `./mvnw clean install`
- Python: `pytest`
- kdb+ Check: `q check_math.q`