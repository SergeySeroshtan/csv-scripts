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
