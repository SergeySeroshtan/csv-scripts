# Summary

This application was developed to help automate activities related to processing CSV files.
It's intended for execution of Groovy scripts, that are written using predefined DSL.
Use of scripts provides good flexibility and reduces complexity of application itself.

# Scripts

## Examples

This script calculates number of records, flagged as 'FOUND':

```groovy
def seq = sequence(
    filter({ it["message"] == "FOUND" }).over(aggregate().into("found")),
    aggregate().into("all"))

processCsv(args[0], seq)

def found = seq.pull("found").size()
def all = seq.pull("all").size()

printf "%s / %s", found, all
```

The following script combines data from several CSV files:

```groovy
def seq = sequence(
    filter({ it["message"] == "FOUND" }).over(aggregate().into("found")),
    aggregate().into("all"))

(1..<args.length).each { i -> processCsv(args[i], seq) }

def merged = merge("id", seq.pull("all"), seq.pull("found"))
printf "total: %s", merged.size()

saveCsv(args[0], merged)
```

In this script we merge data to avoid duplicates.

## DSL

This application provides DSL that simplifies writing of scripts.

Processing:

* `aggregate` - to collect records: `aggregate().into(…)`
* `apply` - to apply custom mediator: `apply({…})`
* `filter` - to filter records by condition: `filter({…}).over(mediators)`
* `sequence` - to combine mediators: `sequence(mediators)`
* `split` - to split record between mediators: `split(mediators)`

Input/output:

* `loadCsv` - to load records from CSV file: `def records = loadCsv(filename)`
* `saveCsv` - to save records into CSV file: `saveCsv(filename, records)`
* `processCsv` - to process records from CSV files: `processCsv(filename, mediators)`

Utilities:

* `merge` - to merge several record sets: `merge("id", …)`

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
