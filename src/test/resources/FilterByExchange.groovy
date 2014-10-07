def records = [
    record(ticker : "GOOG", name : "Google", exchange : "NASDAQ"),
    record(ticker : "ORCL", name : "Oracle", exchange : "NYSE"),
    record(ticker : "MSFT", name : "Microsoft", exchange : "NASDAQ")
]

def seq = sequence(
    filter({ it.exchange == "NYSE" }).over(count("NYSE")),
    filter({ it.exchange == "NASDAQ" }).over(count("NASDAQ"))
)

process(records, seq)

assert seq["NYSE"] == 1
assert seq["NASDAQ"] == 2
