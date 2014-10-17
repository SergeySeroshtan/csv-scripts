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
package hrytsenko.csv.core;

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

    @Override
    public Object getProperty(String propertyName) {
        return getAt(propertyName);
    }

    @Override
    public void setProperty(String propertyName, Object newValue) {
        putAt(propertyName, newValue);
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
     * @return the value of field of <code>null</code> if such field not found.
     */
    public String getAt(String field) {
        return values.get(normalize(field));
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
        String updatedField = normalize(field);
        if (!fields.contains(updatedField)) {
            fields.add(updatedField);
        }
        values.put(updatedField, String.valueOf(value));
    }

    /**
     * Copies all of the fields and values from given {@link Map}.
     * 
     * @param values
     *            the fields and values to be copied.
     */
    public void putAll(Map<String, ?> values) {
        if (values == null) {
            return;
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
        fields.removeAll(normalize(removedFields));
        values.keySet().retainAll(fields);
    }

    /**
     * Changes the name of field. The order of fields will not be changed.
     * 
     * @param field
     *            the name of field.
     * @param name
     *            the new name for field.
     */
    public void rename(String field, String name) {
        String oldField = normalize(field);
        int pos = fields.indexOf(oldField);
        if (pos == -1) {
            return;
        }

        String newField = normalize(name);
        fields.set(pos, newField);

        String value = values.get(oldField);
        values.remove(oldField);
        values.put(newField, value);
    }

    /**
     * Retains only given fields.
     * 
     * @param retainedFields
     *            the set of fields to be retained.
     */
    public void retain(String... retainedFields) {
        fields.retainAll(normalize(retainedFields));
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
        Map<String, String> orderedValues = new LinkedHashMap<>();
        for (String field : fields) {
            orderedValues.put(field, values.get(field));
        }
        return orderedValues;
    }

    /**
     * Creates copy of this record.
     * 
     * @return the copy of record.
     */
    public Record copy() {
        Record copy = new Record();
        copy.putAll(values());
        return copy;
    }

    private static String normalize(String field) {
        return field.toLowerCase();
    }

    private static Set<String> normalize(String... fields) {
        Set<String> normalizedFields = new LinkedHashSet<>();
        for (String field : fields) {
            normalizedFields.add(normalize(field));
        }
        return normalizedFields;
    }

}
