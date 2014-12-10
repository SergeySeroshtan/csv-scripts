package hrytsenko.csv;

import static hrytsenko.csv.Args.parseArgs;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests parsing of arguments.
 * 
 * @author hrytsenko.anton
 */
public class ArgsTest {

    @Test
    public void testEmpty() throws Exception {
        Args args = parseArgs(new String[] {});
        assertTrue(args.getScripts().length == 0);
        assertTrue(args.getValues().length == 0);
    }

    @Test
    public void testArgs() throws Exception {
        Args args = parseArgs(new String[] { "-s", "stocks.groovy", "-v", "stocks.csv", "NASDAQ" });
        assertArrayEquals(new String[] { "stocks.groovy" }, args.getScripts());
        assertArrayEquals(new String[] { "stocks.csv", "NASDAQ" }, args.getValues());
    }

}
