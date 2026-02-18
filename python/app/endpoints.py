from fastapi import APIRouter, HTTPException

from .kdb_client import KdbClient

router = APIRouter()

@router.get("/explain-risk")
def explain_risk(amount: float = 0.0):
    """Return a human-readable explanation of risk metrics for given parallel-shift amount.

    The service will call the kdb+ engine and run the built-in `runScenario` function. If
    the kdb+ instance is unreachable the exception is caught and an error returned.
    """
    try:
        with KdbClient() as kdb:
            # run the scenario in q; the result is a table of P&L per position
            qcmd = f"runScenario {amount}"
            result = kdb.query(qcmd)
            return {"shift": amount, "result": result}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@router.get("/interpret-macro")
def interpret_macro(context: str):
    """Provide macroeconomic interpretation based on context string."""
    # placeholder; would perform RAG lookup or language-model call
    return {"interpretation": "[placeholder] This would use a language model."}

@router.get("/narrative-scenario")
def narrative_scenario(type: str = "parallel", shift: float = 0.0):
    """Generate a BoJ scenario narrative given type and magnitude."""
    return {"narrative": f"A {type} shift of {shift:.4f} prepared by BoJ."}
