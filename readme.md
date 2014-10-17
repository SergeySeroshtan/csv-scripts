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
`process`   | To apply specified mediators to all records in set.
`remove`    | To remove fields from record.
`rename`    | To rename field in record.
`retain`    | To retain fields in record.
`info`      | To add message into the log.

For example, this script merges data from several CSV files:

```groovy
def all = []
(1..<args.length).each {
    all = merge("id", all, load(args[it]))
}

info "Save ${all.size()} records into ${args[0]}."
save(args[0], all)
```

To process separate records we use mediators.
Each mediator contains logic to be applied to records.
Mediators can be combined to create more complex logic.

The following operations are useful to create mediators:

Operation   | Usage
------------|------------------------------------------
`apply`     | To apply custom logic to record.
`filter`    | To filter records by condition.
`sequence`  | To combine other mediators into sequence.
`split`     | To split record between other mediators.
`check`     | To check condition.
`not`       | To negate condition.

For example, this script finds records that were added in new version of CSV file:

```groovy
def old = distinct("id", load(args[0]))

def diff = []
process(load(args[1]),
    filter({ !old.contains(it.id) }).over(apply({ diff.add(it) }))
)

info "Found ${diff.size()} new records."
save(args[2], diff)
```

# Execution

To execute script in command-line mode:

```
java -jar csv-scripts.jar script.groovy input.csv output.csv
```

In this case `script.groovy` is the name of script to be executed.
Arguments `input.csv` and `output.csv` will be passed into this script.
They can be accessed through variable `args`, as `args[0]` and `args[1]` respectively.

# Build

[![Build Status](https://travis-ci.org/hrytsenko/csv-scripts.svg?branch=master)](https://travis-ci.org/hrytsenko/csv-scripts)

To build executable file from command-line, execute: `mvn clean package`.

Also, the following plugins are configured to run during `verify`:

* [FindBugs Maven Plugin](http://mojo.codehaus.org/findbugs-maven-plugin/) - for static analysis.
* [License Maven Plugin](http://mojo.codehaus.org/license-maven-plugin/) - to check license headers.
