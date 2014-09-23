# Summary

This application is intended to help automate processing of CSV files.
It may execute Groovy scripts, that are written using predefined constructs.
Use of these scripts provides flexibility and reusability.

# Scripts

This application provides set of constructs that simplifies writing of scripts.
Technically it's embedded classes and static methods that are imported into the Groovy shell.

For work with record sets:

* `load` - to load  record set from CSV file: `def records = load(filename)`
* `save` - to save record set into CSV file: `save(filename, records)`
* `process` - to process record set from CSV files: `process(filename, mediators)`
* `combine` - to combine several record sets: `combine(…)`
* `merge` - to merge several record sets: `merge("id", …)`
* `map` - to map records by value of field: `map("id", …)`

For building processing flows:

* `apply` - to apply custom logic: `apply({…})`
* `aggregate` - to collect records: `aggregate(name)`
* `count` - to count records: `count(name)`
* `filter` - to filter by condition: `filter(condition).over(mediators)`
* `sequence` - to combine mediators: `sequence(mediators)`
* `split` - to split copies of record between mediators: `split(mediators)`

For work with conditional logic:

* `check` - to apply custom condition: `check({…})`
* `not` - to get the logical negation of condition: `not(condition)`

For work with log:

* `info` - add message to log: `info "message"`

Also class `Record` provides some methods to work with fields:

* `remove` - to remove certain fields: `apply({ it.remove(…) })`
* `retain` - to retain only certain fields: `apply({ it.retain(…) })`

## Examples

Calculate number of updated records:

```groovy
def seq = sequence(
    filter({ it["status"] == "UPDATED" }).over(count("updated")),
    count("total"))

process(args[0], seq)

info "${seq["updated"]} / ${seq["total"]}."
```

Merge records from several CSV files where different records were updated:

```groovy
def seq = sequence(
    filter({ it["status"] == "UPDATED" }).over(aggregate("updated")),
    aggregate("all"))

(1..<args.length).each { i -> process(args[i], seq) }

def merged = merge("id", seq["all"], seq["updated"])
save(args[0], merged)
```

Find records that were added in new version of CSV file:

```groovy
def old = map("id", load(args[0]))

def seq = sequence(
    filter({ old.contains(it["id"]) }).over(aggregate("diff")))

process(args[1], seq)
save(args[2], seq["diff"])
```

# Build

To build executable file using Maven:

```
mvn clean package
```

# Execute

To execute script in command-line mode:

```
java -jar csv-scripts.jar script.groovy input.csv output.csv
```

In this case `script.groovy` is the name of script to be executed.
Arguments `input.csv` and `output.csv` will be passed into this script.
They can be accessed through variable `args`, as `args[0]` and `args[1]` respectively.

# License

[Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
