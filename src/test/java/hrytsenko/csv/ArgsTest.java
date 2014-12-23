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

import static hrytsenko.csv.Args.parseArgs;
import static org.junit.Assert.assertArrayEquals;

import org.apache.commons.cli.ParseException;
import org.junit.Test;

/**
 * Tests parsing of arguments.
 * 
 * @author hrytsenko.anton
 */
public class ArgsTest {

    @Test(expected = ParseException.class)
    public void testEmpty() throws Exception {
        parseArgs(new String[] {});
    }

    @Test
    public void testArgs() throws Exception {
        Args args = parseArgs(new String[] { "-s", "stocks.groovy", "-v", "stocks.csv", "NASDAQ" });
        assertArrayEquals(new String[] { "stocks.groovy" }, args.getScripts().toArray());
        assertArrayEquals(new String[] { "stocks.csv", "NASDAQ" }, args.getValues().toArray());
    }

}
