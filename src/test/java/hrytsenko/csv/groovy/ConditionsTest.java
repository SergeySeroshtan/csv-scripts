package hrytsenko.csv.groovy;

import static hrytsenko.csv.groovy.Conditions.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hrytsenko.csv.core.Condition;
import hrytsenko.csv.core.Record;

import org.junit.Test;

public class ConditionsTest {

    @Test
    public void testNot() {
        Condition ignoreAll = new Condition.IgnoreAll();

        Record emptyRecord = new Record();

        assertFalse(ignoreAll.check(emptyRecord));
        assertTrue(not(ignoreAll).check(emptyRecord));
    }

}
