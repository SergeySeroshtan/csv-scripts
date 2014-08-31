# Summary

This application was developed to help automate activities related to processing CSV files.
It's intended for execution of Groovy scripts, that are written using predefined DSL.
Use of scripts provides good flexibility and reduces complexity of application itself.

# Scripts

## Examples

This script calculates number of records, flagged as 'FOUND':

```groovy
def seq = sequence(
    filter({ rec -> rec.get("message") == "FOUND" }).over(aggregate().into("found")),
    aggregate().into("all"))

processCsv(args[0], seq)

def found = seq.pull("found").size()
def all = seq.pull("all").size()

printf "%s / %s", found, all
```

The following script combines data from several CSV files:

```groovy
def seq = sequence(
    filter({ rec -> rec.get("message") == "FOUND" }).over(aggregate().into("found")),
    aggregate().into("all"))

(1..<args.length).each { i -> processCsv(args[i], seq) }

def merged = merge("id", seq.pull("all"), seq.pull("found"))
printf "total: %s", merged.size()

saveCsv(args[0], merged)
```

In this script we merge data to avoid duplicates.

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
