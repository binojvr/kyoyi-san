from fastapi.testclient import TestClient
import pytest

from app.main import app
from app import endpoints

client = TestClient(app)

class DummyKdb:
    def __init__(self, *args, **kwargs): pass
    def __enter__(self): return self
    def __exit__(self, exc_type, exc, tb): pass
    def query(self, q):
        # return dummy table representation
        return [["PORT1", "JGB2Y", 1000, 99.5, 99.6, 100]]

@ pytest.fixture(autouse=True)
def patch_kdb(monkeypatch):
    monkeypatch.setattr(endpoints, "KdbClient", DummyKdb)


def test_explain_risk_default():
    resp = client.get("/explain-risk")
    assert resp.status_code == 200
    assert resp.json()["shift"] == 0.0
    assert "result" in resp.json()


def test_explain_risk_custom():
    resp = client.get("/explain-risk?amount=0.0001")
    assert resp.status_code == 200
    assert resp.json()["shift"] == 0.0001


def test_interpret_macro():
    resp = client.get("/interpret-macro?context=test")
    assert resp.status_code == 200
    assert "interpretation" in resp.json()


def test_narrative_scenario():
    resp = client.get("/narrative-scenario?type=parallel&shift=0.0005")
    assert resp.status_code == 200
    assert "narrative" in resp.json()
