def records = load(path: args[0])

def found = 0

records.findAll{
    it.exchange == args[1]
}.each {
    log "Found ${it.ticker}."
    ++found
}

def expected = args[2].toInteger()
assert expected == found
