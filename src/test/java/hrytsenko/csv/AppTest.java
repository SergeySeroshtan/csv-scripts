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

import static hrytsenko.csv.App.execute;
import static java.lang.Thread.currentThread;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class AppTest {

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyArgs() throws Exception {
        execute(new String[] {});
    }

    @Test
    public void testRecord() throws Exception {
        executeScript("Record.groovy");
    }

    @Test
    public void testExample() throws Exception {
        executeScript("Example.groovy");
    }

    private void executeScript(String filename) throws Exception {
        URI uri = currentThread().getContextClassLoader().getResource(filename).toURI();
        Path path = Paths.get(uri);
        execute(new String[] { path.toAbsolutePath().toString() });
    }

}
