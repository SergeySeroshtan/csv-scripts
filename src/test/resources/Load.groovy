def records = load(path: args[0]) { record ->
    record.fields().each { field ->
        record.rename(field, field.toLowerCase())
    }
}

records.each { record ->
    assert record.contains("ticker")
}
