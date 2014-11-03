[![Build Status](https://travis-ci.org/hrytsenko/csv-scripts.svg?branch=master)](https://travis-ci.org/hrytsenko/csv-scripts)
[![Coverage Status](https://coveralls.io/repos/hrytsenko/csv-scripts/badge.png?branch=master)](https://coveralls.io/r/hrytsenko/csv-scripts?branch=master)

# Summary

This application is intended to help automate processing of CSV files via scripts.
Such scripts are useful for prototyping and rapid implementation of custom logic.
Application provides set of classes and methods to simplify writing of scripts.

This application uses Groovy as scripting language.

# Scripts

We consider that CSV file contains ordered set of records.
And each record contains ordered set of fields and their values.

To access fields of record you can use overloaded operator [] or properties:

```groovy
def record = record(ticker : "ORCL", name : "Oracle")

assert record.ticker == record["ticker"]
```

You can use any object to update value of field.
As a result, field will contain the string representation of this object.

```groovy
def record = record(ticker : "ORCL", name : "Oracle")

assert record.exchange == null

record.exchange = "NYSE"
assert record.exchange == "NYSE"

record.exchange = null
assert record.exchange == "null"

record.remove("exchange")
assert record.exchange == null
```

Record supports following operations:

Operation   | Usage
------------|---------------------------------
`remove`    | To remove fields from record.
`rename`    | To rename field in record.
`retain`    | To retain fields in record.
`copy`      | To create copy of record.

The following operations are useful for work with sets of records:

Operation   | Usage
------------|---------------------------------
`load`      | To load records from CSV file.
`save`      | To save records into CSV file.
`distinct`  | To get distinct values of field.
`combine`   | To combine records from different sets into one.
`merge`     | To merge records from different sets using the value of field as unique key.
`map`       | To get mapping of records using the value of field as unique key.
`group`     | To split records into groups using the value of field as key.

Operations `load` and `save` support the following named arguments:

* `path` - the path to file.
* `records` - the list of records to be saved.
* `charset` - the charset for file, default: UTF-8.
* `fieldSeparator` - the separator for fields, default: comma.
* `fieldQualifier` - the qualifier for fields, default: double-quote.

Additionally, you can use operation `log` to add custom message into the log of script.

# Examples

Script that merges records from several CSV files:

```groovy
def all = []
(1..<args.length).each {
    all = merge("id", all, load(path: args[it]))
}

log "Save ${all.size()} records into ${args[0]}."
save(path: args[0], records: all)
```


Script that finds records that were added in new version of CSV file:

```groovy
def previous = distinct("id", load(path: args[0]))
def current = load(path: args[1])
def diff = []

current.each {
    if (!previous.contains(it.id)) {
        diff.add(it)
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

