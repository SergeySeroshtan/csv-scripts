def record = record(ticker : "GOOG", name : "Google")
assert record["ticker"] == "GOOG"

assert record["exchange"] == null
record["exchange"] = "NYSE"
assert record["exchange"] == "NYSE"

assert record["exchange"] != null
record["exchange"] = "NASDAQ"
assert record["exchange"] == "NASDAQ"
