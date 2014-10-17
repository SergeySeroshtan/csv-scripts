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
package hrytsenko.csv.groovy;

import groovy.lang.GroovyShell;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

public class ScriptTest {

    private GroovyShell shell;

    @Before
    public void init() {
        shell = App.createShell(new String[] {});
    }

    @Test
    public void testRecord() throws Exception {
        doTest("Record.groovy");
    }

    @Test
    public void testFilter() throws Exception {
        doTest("Filter.groovy");
    }

    private void doTest(String filename) throws URISyntaxException, IOException {
        URL script = Thread.currentThread().getContextClassLoader().getResource(filename);
        shell.evaluate(Files.newBufferedReader(Paths.get(script.toURI()), Charset.forName("UTF-8")));
    }

}
