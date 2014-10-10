def record = record( ticker : "ORCL", name : "Oracle" )

assert record.ticker == record["ticker"]
