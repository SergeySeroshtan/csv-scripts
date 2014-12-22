def records = load(path: args[0]).group('exchange')

def exchange = args[1]
def found = records[exchange].size()

log "Found $found stocks for $exchange."

assert args[2].toInteger() == found
