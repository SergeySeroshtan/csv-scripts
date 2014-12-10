[![Build Status](https://travis-ci.org/hrytsenko/csv-scripts.svg?branch=master)](https://travis-ci.org/hrytsenko/csv-scripts)
[![Coverage Status](https://coveralls.io/repos/hrytsenko/csv-scripts/badge.png?branch=master)](https://coveralls.io/r/hrytsenko/csv-scripts?branch=master)

# Summary

This application is intended to help automate processing of CSV files via scripts in Groovy.
Such scripts are useful for prototyping and rapid implementation of custom logic.
Application provides set of classes and methods to simplify writing of such scripts.

# Scripts

Application assumes that CSV file contains ordered set of records.
And each record contains ordered set of fields and their values.

To access fields by name you can use operator [] or properties.
Also, you can access fields by index using operator [].

```groovy
def record = record(ticker: 'ORCL', name: 'Oracle')

assert record.ticker == record['ticker']
assert record.ticker == record[0]
```

Application allows to use any object to update value of field.
But, as the result, field will contain the string representation of this object.
And in case of `null` value, field will contain empty string.

```groovy
def record = record(ticker: 'ORCL', name: 'Oracle')

assert record.exchange == null

record.exchange = 'NYSE'
assert record.exchange == 'NYSE'

record.exchange = null
assert record.exchange == ''

record.remove('exchange')
assert record.exchange == null
```

Each record provides the following convenience methods:

Operation   | Usage
------------|---------------------------------
`contains`  | Check that record contains field.
`remove`    | Remove fields from record.
`rename`    | Rename field in record.
`retain`    | Retain fields in record.
`copy`      | Create copy of record.
`fields`    | Get list of fields.
`values`    | Get map of fields and values.

Additionally, application provides following methods that are useful for work with sets of records:

Operation   | Usage
------------|---------------------------------
`load`      | Load records from CSV file.
`save`      | Save records into CSV file.
`distinct`  | Find distinct values of field.
`map`       | Map the records using the field as unique key.
`group`     | Split the records into groups using the field as key.
`merge`     | Merge the records using the field as unique key.
`record`    | Create record with given values.

Operations `distinct`, `map` and `group` can be applied to any collection:

```groovy
def records = [
    record(ticker: 'ORCL', exchange: 'NYSE'),
    record(ticker: 'GOOG', exchange: 'NASDAQ'),
    record(ticker: 'MSFT', exchange: 'NASDAQ')
]

assert records.distinct('exchange') == distinct('exchange', records)
```

Operations `load` and `save` support the following named arguments:

* `path` - the path to file.
* `records` - the list of records to be saved.
* `charset` - the character set for file, default: UTF-8.
* `separator` - the separator for fields, default: comma.
* `qualifier` - the qualifier for fields, default: double-quote.

You can easily process records during `load`.
Following example converts names of fields to lowercase during load:

```groovy
def records = load(path: args[0]) { record ->
    record.fields().each { field ->
        record.rename(field, field.toLowerCase())
    }
}
```

Operation `merge` allow to use closure as optional argument.

Additionally, you can use operation `log` to add custom message into the log of script.

# Examples

Merge records from several CSV files by identifier:

```groovy
def all = []
(1..<args.length).each {
    all = merge('id', all, load(path: args[it]))
}

log "Save ${all.size()} records into ${args[0]}."
save(path: args[0], records: all)
```

Find records that were added in new version of CSV file:

```groovy
def prev = distinct('id', load(path: args[0]))
def last = load(path: args[1])
def diff = []

last.each {
    if (!prev.contains(it.id)) {
        diff << it
    }
}

log "Found ${diff.size()} new records."
save(path: args[2], records: diff)
```

# Execution

To execute script in command-line mode:

```
java -jar csv-scripts.jar filter_stocks.groovy stocks.csv NASDAQ
```

In this case `filter_stocks.groovy` is the name of script to be executed.
Arguments `stocks.csv` and `NASDAQ` will be passed into this script.
They accessible through variable `args`, as `args[0]` and `args[1]` respectively.

# Dependencies

To add additional dependencies you can use [Grape](http://groovy.codehaus.org/Grape).

For example, this script gets data from RESTful service using class `RESTClient`:

```groovy
@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7.1')
import groovyx.net.http.*

def owmService = new RESTClient('http://api.openweathermap.org/data/2.5/weather')

def cities = load(path: args[0])
cities.each {
    def response = owmService.get(
        query: [q: it.city, units: 'metric'] )
    it.temp = response.data.main.temp
}

save(path: args[1], records: cities)
```
