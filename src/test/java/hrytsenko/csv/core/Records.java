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

import java.util.Collection;

import org.junit.Assert;

/**
 * The utility class to create records for tests.
 * 
 * @author hrytsenko.anton
 */
public final class Records {

    /**
     * Creates record with given content.
     * 
     * @param content
     *            the list of fields and their values to be added to record.
     * 
     * @return the created record with given fields and values.
     */
    public static Record createRecord(String... content) {
        Record record = new Record();
        for (int i = 0; i < content.length; i += 2) {
            record.putAt(content[i], content[i + 1]);
        }
        return record;
    }

    /**
     * Asserts that record contains all specified fields in given order.
     * 
     * @param record
     *            the record to be validated.
     * @param expectedFields
     *            the list of expected fields.
     */
    public static void assertFields(Record record, String... expectedFields) {
        Collection<String> actualFields = record.fields();
        Assert.assertArrayEquals(expectedFields, actualFields.toArray());
    }

    private Records() {
    }

}
