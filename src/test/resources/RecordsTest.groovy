def record = record(ticker : 'ORCL', name : 'Oracle')
assert record.fields() == ['ticker', 'name']

assert record.ticker == record['ticker']
assert record.ticker == record[0]

record.exchange = 'NYSE'
assert record.exchange == 'NYSE'

record.exchange = null
assert record.exchange == ''

record.remove('exchange')
assert record.exchange == null
