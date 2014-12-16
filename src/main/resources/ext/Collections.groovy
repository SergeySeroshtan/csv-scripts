Collection.metaClass.distinct = { field ->
    distinct(field, delegate)
}

Collection.metaClass.group = { field ->
    group(field, delegate)
}

Collection.metaClass.map = { field ->
    map(field, delegate)
}

Collection.metaClass.merge = { field, other ->
    merge(field, delegate, other)
}

Collection.metaClass.merge = { field, other, closure ->
    merge(field, delegate, other, closure)
}
