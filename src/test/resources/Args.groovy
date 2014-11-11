def records = load(path: args[0])

def total = 0

records.each {
    if (it.exchange == args[1]) {
        log "Found ${it.ticker}."

        ++total;
    }
}

assert total == args[2].toInteger()
