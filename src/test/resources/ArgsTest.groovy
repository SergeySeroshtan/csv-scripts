def records = load(path: args[0]).group('exchange')

assert records[args[1]].size() == args[2].toInteger()
