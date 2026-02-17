from fastapi import FastAPI

app = FastAPI(title="JGB Risk Engine RAG Service")

@app.get("/health")
def health_check():
    return {"status": "ok"}

# Placeholder for RAG endpoints
@app.get("/docs")
def docs_redirect():
    return {"message": "Use /redoc or /openapi.json"}
