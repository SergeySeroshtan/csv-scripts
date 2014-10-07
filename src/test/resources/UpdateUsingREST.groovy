// Description of API: http://openweathermap.org/current.

import groovyx.net.http.*

def records = [
    record(country : "Ukraine", city : "Kiev"),
    record(country : "Poland", city : "Warsaw")
]

def client = new RESTClient("http://api.openweathermap.org/data/2.5/weather")

def seq = sequence(
    apply({
        def resp = client.get(query : [q : "${it['city']},${it['country']}", units : "metric"])
        it.temp = resp.data.main.temp
    }),
    aggregate("results")
)

process(records, seq)

seq["results"].each {
    assert it.temp.isNumber()
}
