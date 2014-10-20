def records = [
    record(ticker : "GOOG", exchange : "NASDAQ"),
    record(ticker : "ORCL", exchange : "NYSE"),
    record(ticker : "MSFT", exchange : "NASDAQ")
]

def NASDAQ = 0

records.each {
    if (it.exchange == "NASDAQ") {
        log "Found ${it.ticker}."

        ++NASDAQ;
    }
}

assert NASDAQ == 2
