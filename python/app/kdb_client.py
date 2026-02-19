from qpython import qconnection
import os
from pathlib import Path
from dotenv import load_dotenv

# Load shared .env from project root (two levels up from this file)
load_dotenv(Path(__file__).resolve().parents[2] / ".env")

class KdbClient:
    def __init__(self, host=None, port=None):
        self.host = host or os.getenv("KDB_HOST", "localhost")
        self.port = int(port or os.getenv("KDB_PORT", "5000"))
        self.db_dir = os.getenv("KDB_DB_DIR")
        self.conn = qconnection.QConnection(host=self.host, port=self.port)
        self.conn.open()

    def query(self, q_string):
        """Send a raw q query and return the result."""
        return self.conn(q_string)

    def close(self):
        if self.conn is not None:
            self.conn.close()

    # context manager support so we can use `with KdbClient() as kdb:`
    def __enter__(self):
        return self

    def __exit__(self, exc_type, exc_value, traceback):
        self.close()
