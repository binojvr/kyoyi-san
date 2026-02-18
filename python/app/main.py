from fastapi import FastAPI

from .endpoints import router as rag_router

app = FastAPI(title="JGB Risk Engine RAG Service")

app.include_router(rag_router)

@app.get("/health")
def health_check():
    return {"status": "ok"}
