/*
 * #%L
 * csv-scripts
 * %%
 * Copyright (C) 2014 Anton Hrytsenko
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package hrytsenko.csv;

import groovy.lang.GroovyObjectSupport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Record contains set of fields and their values.
 * 
 * <p>
 * The names of fields are considered as case insensitive. All names are stored in lower-case.
 * 
 * @author hrytsenko.anton
 */
public final class Record extends GroovyObjectSupport {

    private List<String> fields;
    private Map<String, String> values;

    /**
     * Creates an empty record.
     */
    public Record() {
        fields = new ArrayList<>();
        values = new HashMap<>();
    }

    /**
     * Returns value of field.
     *
     * <p>
     * Also overloads operator <tt>[]</tt> in Groovy.
     * 
     * @param field
     *            the name of field.
     * 
     * @return the value of field of <code>null</code> if it not found.
     */
    public String getAt(String field) {
        String fieldInner = asInner(field);
        return values.get(fieldInner);
    }

    @Override
    public Object getProperty(String propertyName) {
        return getAt(propertyName);
    }

    /**
     * Sets the value of field. The value is the string representation of given object.
     *
     * <p>
     * Also overloads operator <tt>[]</tt> in Groovy.
     * 
     * @param field
     *            the name of field.
     * @param value
     *            the new value for field.
     */
    public void putAt(String field, Object value) {
        String fieldInner = asInner(field);
        if (!fields.contains(fieldInner)) {
            fields.add(fieldInner);
        }
        values.put(fieldInner, String.valueOf(value));
    }

    @Override
    public void setProperty(String propertyName, Object newValue) {
        putAt(propertyName, newValue);
    }

    /**
     * Copies all of the fields and values from given {@link Map}.
     * 
     * @param values
     *            the fields and values to be copied.
     */
    public void putAll(Map<String, ?> values) {
        if (values == null) {
            throw new IllegalArgumentException("Values not defined.");
        }

        for (Map.Entry<String, ?> value : values.entrySet()) {
            putAt(value.getKey(), value.getValue());
        }
    }

    /**
     * Removes all given fields.
     * 
     * @param removedFields
     *            the set of fields to be removed.
     */
    public void remove(String... removedFields) {
        Set<String> removedFieldsInner = asInner(removedFields);
        fields.removeAll(removedFieldsInner);
        values.keySet().retainAll(fields);
    }

    /**
     * Changes the name of field. The order of fields will not be changed.
     * 
     * @param oldField
     *            the old name of field.
     * @param newField
     *            the new name for field.
     */
    public void rename(String oldField, String newField) {
        String oldFieldInner = asInner(oldField);
        int pos = fields.indexOf(oldFieldInner);
        if (pos == -1) {
            throw new IllegalArgumentException("No such field.");
        }

        String newFieldInner = asInner(newField);
        fields.set(pos, newFieldInner);

        String value = values.get(oldFieldInner);
        values.remove(oldFieldInner);
        values.put(newFieldInner, value);
    }

    /**
     * Retains only given fields.
     * 
     * @param retainedFields
     *            the set of fields to be retained.
     */
    public void retain(String... retainedFields) {
        Set<String> retainedFieldsInner = asInner(retainedFields);
        fields.retainAll(retainedFieldsInner);
        values.keySet().retainAll(fields);
    }

    /**
     * Returns the names of all fields.
     * 
     * @return the names of fields.
     */
    public Collection<String> fields() {
        return new LinkedHashSet<>(fields);
    }

    /**
     * Returns the values of all fields.
     * 
     * @return the values of all fields.
     */
    public Map<String, String> values() {
        Map<String, String> copiedValues = new LinkedHashMap<>();
        for (String field : fields) {
            copiedValues.put(field, values.get(field));
        }
        return copiedValues;
    }

    /**
     * Creates copy of this record.
     * 
     * @return the copy of record.
     */
    public Record copy() {
        Record copy = new Record();
        for (String field : fields) {
            copy.putAt(field, values.get(field));
        }
        return copy;
    }

    private static String asInner(String field) {
        if (field == null || field.isEmpty()) {
            throw new IllegalArgumentException("Invalid name of field.");
        }

        return field.toLowerCase();
    }

    private static Set<String> asInner(String... fields) {
        Set<String> fieldsInner = new LinkedHashSet<>();
        for (String field : fields) {
            fieldsInner.add(asInner(field));
        }
        return fieldsInner;
    }

}
