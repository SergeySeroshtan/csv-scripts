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

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import groovy.lang.GroovyObjectSupport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * Record contains set of fields and their values.
 * 
 * <p>
 * Name of field could not be <code>null</code> or empty.
 * 
 * <p>
 * Names of field are case-sensitive. I.e. script should take care of naming of fields.
 * 
 * @author hrytsenko.anton
 */
public class Record extends GroovyObjectSupport {

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
     * @param field
     *            the name of field.
     * 
     * @return the value of field of <code>null</code> if it not found.
     */
    public String getAt(String field) {
        validateNotEmpty(field);

        return values.get(field);
    }

    @Override
    public Object getProperty(String propertyName) {
        return getAt(propertyName);
    }

    /**
     * Sets the value of field. The value is the string representation of given object.
     * 
     * <p>
     * If value is <code>null</code>, then {@link StringUtils#EMPTY} used.
     * 
     * @param field
     *            the name of field.
     * @param value
     *            the new value for field.
     */
    public void putAt(String field, Object value) {
        validateNotEmpty(field);

        if (!fields.contains(field)) {
            fields.add(field);
        }
        values.put(field, value == null ? EMPTY : value.toString());
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
     * Checks that record contains the specified field.
     * 
     * @param field
     *            the field whose presence is checked.
     * 
     * @return <code>true</code> if record contains field and <code>false</code> otherwise.
     */
    public boolean contains(String field) {
        validateNotEmpty(field);

        return fields.contains(field);
    }

    /**
     * Removes all given fields.
     * 
     * @param removedFields
     *            the set of fields to be removed.
     */
    public void remove(String... removedFields) {
        validateContains(removedFields);

        fields.removeAll(asList(removedFields));
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
        validateContains(oldField);

        int pos = fields.indexOf(oldField);
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
        validateContains(retainedFields);

        fields.retainAll(asList(retainedFields));
        values.keySet().retainAll(fields);
    }

    /**
     * Returns the names of all fields.
     * 
     * @return the names of fields.
     */
    public Collection<String> fields() {
        return new ArrayList<>(fields);
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

    private void validateNotEmpty(String validatedField) {
        if (isEmpty(validatedField)) {
            throw new IllegalArgumentException("Empty field.");
        }
    }

    private void validateContains(String... validatedFields) {
        for (String validatedField : validatedFields) {
            validateNotEmpty(validatedField);

            if (!contains(validatedField)) {
                throw new IllegalArgumentException(format("Field %s not found.", validatedField));
            }
        }
    }

}
