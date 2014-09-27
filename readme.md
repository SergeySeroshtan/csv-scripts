# Summary

This application is intended to help automate processing of CSV files.
It may execute Groovy scripts, that are written using predefined constructs.
Use of these scripts provides flexibility and reusability.

# Scripts

This application provides set of constructs that simplifies writing of scripts.
Technically it's embedded classes and static methods that are accessible in Groovy shell.

For work with records:

* `load` - to load records from CSV file.
* `save` - to save records into CSV file.
* `process` - to process records.
* `combine` - to combine records.
* `merge` - to merge records by value of field.
* `map` - to map records by value of field.
* `remove` - to remove fields from record.
* `rename` - to rename field in record.
* `retain` - to retain fields in record.

For work with flows:

* `apply` - to apply custom logic.
* `aggregate` - to collect records.
* `count` - to count records.
* `filter` - to filter records by condition.
* `sequence` - to apply mediators to record.
* `split` - to apply mediators to the copy of record.
* `check` - to apply custom condition.
* `not` - to negate condition.

For logging:

* `info` - to add message into log.

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

# Examples

Calculate number of updated records:

```groovy
def seq = sequence(
    filter({ it["status"] == "UPDATED" }).over(count("updated")),
    count("total"))

process(load(args[0]), seq)

info "${seq['updated']} / ${seq['total']}."
```

Merge records from several CSV files where different records were updated:

```groovy
def seq = sequence(
    filter({ it["status"] == "UPDATED" }).over(aggregate("updated")),
    aggregate("all"))

(1..<args.length).each { i -> process(load(args[i]), seq) }

def merged = merge("id", seq["all"], seq["updated"])
save(args[0], merged)
```

Find records that were added in new version of CSV file:

```groovy
def old = map("id", load(args[0]))

def seq = sequence(
    filter({ old.contains(it["id"]) }).over(aggregate("diff")))

process(load(args[1]), seq)
save(args[2], seq["diff"])
```
Get temperature using REST service from OpenWeatherMap:

```groovy
import groovyx.net.http.RESTClient

def client = new RESTClient("http://api.openweathermap.org/data/2.5/weather")

def seq = sequence(
    apply({
        def resp = client.get(query : [q : "${it['city']},${it['country']}", units : "metric"])
        it["temp"] = resp.data.main.temp
    }),
    aggregate("results")
)

process(load(args[0]), seq)
save(args[1], seq["results"])
```

# License

[Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
