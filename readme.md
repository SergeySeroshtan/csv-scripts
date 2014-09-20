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

processCsv(args[0], seq)

printf "%s / %s", seq["found"], seq["all"]
```

The following script combines data from several CSV files:

```groovy
def seq = sequence(
    filter({ it["message"] == "FOUND" }).over(aggregate("found")),
    aggregate("all"))

(1..<args.length).each { i -> processCsv(args[i], seq) }

def merged = merge("id", seq["all"], seq["found"])

saveCsv(args[0], merged)
```

## Language

This application provides DSL that simplifies writing of scripts.

Processing:

* `aggregate` - to collect records: `aggregate(…)`
* `apply` - to apply custom mediator: `apply({…})`
* `count` - to count records: `count(…)`
* `filter` - to filter records by condition: `filter({…}).over(mediators)`
* `sequence` - to combine mediators: `sequence(mediators)`
* `split` - to split record between mediators: `split(mediators)`

Input/output:

* `loadCsv` - to load records from CSV file: `def records = loadCsv(filename)`
* `saveCsv` - to save records into CSV file: `saveCsv(filename, records)`
* `processCsv` - to process records from CSV files: `processCsv(filename, mediators)`

Utilities:

* `combine` - to combine several sets of records in given order: `combine(…)`
* `merge` - to merge several sets of records: `merge("id", …)`
* `map` - to map records by value of certain field: `map("id", …)`

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
