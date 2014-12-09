def records = [
    record(ticker: 'ORCL', exchange: 'NYSE'),
    record(ticker: 'GOOG', exchange: 'NASDAQ'),
    record(ticker: 'MSFT', exchange: 'NASDAQ')
]

def exchanges = records.distinct('exchange')
assert exchanges == ['NYSE', 'NASDAQ']
