# Summary

This application is intended to help automate processing of CSV files via scripts.
Such scripts are useful for prototyping and rapid implementation of custom logic.
Application provides set of classes and methods to simplify writing of scripts.

This application uses Groovy as scripting language.

# Scripts

We consider that CSV file contains ordered set of records.
And each record contains ordered set of fields and their values.

The following operations are useful to work with records:

Operation   | Usage
------------|---------------------------------
`load`      | To load records from CSV file.
`save`      | To save records into CSV file.
`process`   | To apply mediators to records.
`distinct`  | To get distinct values of field.
`combine`   | To combine records.
`merge`     | To merge records by key.
`map`       | To map records by key.
`remove`    | To remove fields from record.
`rename`    | To rename field in record.
`retain`    | To retain fields in record.
`info`      | To add message into the log.

To process records we use mediators, that implement some logic.
Mediators can be combined to implement more complex logic.

The following operations are useful to create mediators:

Operation   | Usage
------------|------------------------------------------
`apply`     | To apply custom logic to record.
`aggregate` | To aggregate all processed records.
`count`     | To count the number of records.
`filter`    | To filter records by condition.
`sequence`  | To combine other mediators into sequence.
`split`     | To split record between other mediators.
`check`     | To check condition.
`not`       | To negate condition.

# Execution

To execute script in command-line mode:

```
java -jar csv-scripts.jar script.groovy input.csv output.csv
```

In this case `script.groovy` is the name of script to be executed.
Arguments `input.csv` and `output.csv` will be passed into this script.
They can be accessed through variable `args`, as `args[0]` and `args[1]` respectively.

# Examples

Merge records from several CSV files where different records were updated:

```groovy
def seq = sequence(
    filter({ it["status"] == "UPDATED" }).over(aggregate("updated")),
    aggregate("all"))

(1..<args.length).each { process(load(args[it]), seq) }

def merged = merge("id", seq["all"], seq["updated"])
save(args[0], merged)
```

Find records that were added in new version of CSV file:

```groovy
def ids = map("id", load(args[0]))

def seq = sequence(
    filter({ !ids.contains(it["id"]) }).over(aggregate("diff")))

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

# Build

To build executable file using Maven:

```
mvn clean package
```

# License

[Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
