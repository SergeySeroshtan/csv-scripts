Collection.metaClass.distinct = { field ->
    distinct(field, delegate)
}

Collection.metaClass.map = { field ->
    map(field, delegate)
}

Collection.metaClass.group = { field ->
    group(field, delegate)
}
