# Summary

This application was developed to help automate processing of CSV files.
It allows execute Groovy scripts, that are written using predefined DSL.
Use of such scripts provides good flexibility and reduces complexity of application itself.

# Scripts

## Examples

This script calculates number of records, flagged as 'FOUND':

```groovy
def seq = sequence(
    filter({ it["message"] == "FOUND" }).over(count("found")),
    count("all"))

process(args[0], seq)

println "${seq["found"]} / ${seq["all"]}"
```

The following script combines data from several CSV files:

```groovy
def seq = sequence(
    filter({ it["message"] == "FOUND" }).over(aggregate("found")),
    aggregate("all"))

(1..<args.length).each { i -> process(args[i], seq) }

def merged = merge("id", seq["all"], seq["found"])

save(args[0], merged)
```

## Language

This application provides DSL that simplifies writing of scripts.
Technically it's set of static methods.

Records:

* `load` - to load set of records from CSV file: `def records = load(filename)`
* `save` - to save set of records into CSV file: `save(filename, records)`
* `process` - to process set of records from CSV files: `process(filename, mediators)`
* `combine` - to combine several sets of records in given order: `combine(…)`
* `merge` - to merge several sets of records: `merge("id", …)`
* `map` - to map records by value of certain field: `map("id", …)`

Mediators:

* `apply` - to apply custom mediator: `apply({…})`
* `aggregate` - to collect records: `aggregate(name)`
* `count` - to count records: `count(name)`
* `filter` - to filter by condition: `filter(condition).over(mediators)`
* `sequence` - to combine mediators: `sequence(mediators)`
* `split` - to split copies of record between mediators: `split(mediators)`

Conditions:

* `check` - to apply custom condition: `check({…})`
* `not` - to get the logical negation of condition: `not(condition)`

Record:

* `remove` - to remove certain fields: `apply({ it.remove(…) })`
* `retain` - to retain only certain fields: `apply({ it.retain(…) })`

## Run

To build executable file using Maven:

```
mvn clean package
```

To execute script in command-line mode:

```
java -jar csv-scripts.jar script.groovy dataset.csv
```

Where first argument - is the filename for script; others - are arguments to be passed to the script.

# License

[Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
