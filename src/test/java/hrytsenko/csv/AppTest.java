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

import static java.lang.Thread.currentThread;

import java.io.InputStream;

import org.junit.Test;

public class AppTest {

    @Test
    public void testRecord() throws Exception {
        doTest("Record.groovy");
    }

    @Test
    public void testExample() throws Exception {
        doTest("Example.groovy");
    }

    private void doTest(String filename) {
        InputStream scriptStream = currentThread().getContextClassLoader().getResourceAsStream(filename);
        App.execute(scriptStream, new String[] {});
    }

}
