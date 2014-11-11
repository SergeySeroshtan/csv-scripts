def record = record(ticker : 'ORCL', name : 'Oracle')

assert record.ticker == record['ticker']

record.exchange = 'NYSE'
assert record.exchange == 'NYSE'

record.exchange = null
assert record.exchange == 'null'

record.remove('exchange')
assert record.exchange == null
