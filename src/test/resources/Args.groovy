def records = load(path: args[0])

def found = 0

records.each {
    if (it.exchange == args[1]) {
        log "Found ${it.ticker}."
        ++found
    }
}

def expected = args[2].toInteger()
assert expected == found
