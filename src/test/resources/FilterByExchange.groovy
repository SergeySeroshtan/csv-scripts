def records = [
    record(ticker : "GOOG", name : "Google", exchange : "NASDAQ"),
    record(ticker : "ORCL", name : "Oracle", exchange : "NYSE"),
    record(ticker : "MSFT", name : "Microsoft", exchange : "NASDAQ")
]

def NYSE = 0
def NASDAQ = 0

def seq = sequence(
    filter({ it.exchange == "NYSE" }).over(apply({ ++NYSE })),
    filter({ it.exchange == "NASDAQ" }).over(apply({ ++NASDAQ }))
)

process(records, seq)

assert NYSE == 1 && NASDAQ == 2
