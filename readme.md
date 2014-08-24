# Summary

This application was developed to help automate activities related to processing CSV files.
It's intended for execution of Groovy scripts, that are written using predefined DSL.
Use of scripts provides good flexibility and reduces complexity of application itself.

# Scripts

Example of script that calculates number of records, flagged as 'FOUND':

```groovy
def seq = sequence(
    filter({ rec -> rec.get("message") == "FOUND" }).then(aggregate().into("found")),
    aggregate().into("all"));

processCsv(args[0], seq);

def found = seq.valueOf("found").size();
def all = seq.valueOf("all").size();

printf "%s / %s", found, all
```

To execute this script, run next command:

```
java -jar csv-scripts.jar coverage.groovy dataset.csv
```

First argument is the name filename of script, and all other arguments will be passed into script, i.e.:

* coverage.groovy - the name of script.
* dataset.csv - the name of CSV file, that will be passed into script.


# License

[Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
