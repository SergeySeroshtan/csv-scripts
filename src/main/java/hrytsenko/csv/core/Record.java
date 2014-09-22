/**
 * Copyright (C) 2014 Anton Hrytsenko (hrytsenko.anton@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package hrytsenko.csv.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
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
public final class Record {

    private static String toName(String field) {
        return field.toLowerCase();
    }

    private Map<String, String> content;

    /**
     * Creates an empty record.
     */
    public Record() {
        this(new HashMap<String, String>());
    }

    /**
     * Creates the record with specified content.
     * 
     * @param originalContent
     *            the initial content of record.
     */
    public Record(Map<String, String> originalContent) {
        content = new LinkedHashMap<String, String>();
        for (Map.Entry<String, String> entry : originalContent.entrySet()) {
            putAt(entry.getKey(), entry.getValue());
        }
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
     * @return the value of field.
     */
    public String getAt(String field) {
        return content.get(toName(field));
    }

    /**
     * Sets the value of field.
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
        content.put(toName(field), String.valueOf(value));
    }

    /**
     * Removes all specified fields.
     * 
     * @param fields
     *            the set of fields to be removed.
     */
    public void remove(String... fields) {
        Set<String> removedFields = new HashSet<>();
        for (String field : fields) {
            removedFields.add(toName(field));
        }

        Set<String> actualFields = content.keySet();
        actualFields.removeAll(removedFields);
    }

    /**
     * Retains only specified fields.
     * 
     * @param fields
     *            the set of fields to be retained.
     */
    public void retain(String... fields) {
        Set<String> retainedFields = new HashSet<>();
        for (String field : fields) {
            retainedFields.add(toName(field));
        }

        Set<String> actualFields = content.keySet();
        actualFields.retainAll(retainedFields);
    }

    /**
     * Returns the names of all fields.
     * 
     * @return the names of fields.
     */
    public Collection<String> fields() {
        return new LinkedHashSet<>(content.keySet());
    }

    /**
     * Returns the content.
     * 
     * @return the content.
     */
    public Map<String, String> content() {
        return new LinkedHashMap<>(content);
    }

}
