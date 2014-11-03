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
import static hrytsenko.csv.TempFiles.UTF_8;
import static hrytsenko.csv.TempFiles.createTempFile;
import static hrytsenko.csv.TempFiles.writeTempFile;
import static java.lang.System.arraycopy;
import static java.lang.Thread.currentThread;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

/**
 * Tests for application based on Groovy scripts.
 * 
 * <p>
 * These tests use temporary files, see {@link TempFiles}.
 * 
 * @author hrytsenko.anton
 */
public class AppTest {

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyArgs() throws Exception {
        execute(new String[] {});
    }

    @Test
    public void testArgs() throws Exception {
        String tempFilePath = createTempFile();
        String tempFileData = "ticker,exchange\nGOOG,NASDAQ\nORCL,NYSE\nMSFT,NASDAQ";
        writeTempFile(tempFilePath, tempFileData, UTF_8);
        executeScript("Args.groovy", tempFilePath, "NASDAQ", "2");
    }

    @Test
    public void testRecord() throws Exception {
        executeScript("Record.groovy");
    }

    private void executeScript(String scriptFilename, String... scriptArgs) throws Exception {
        URI uri = currentThread().getContextClassLoader().getResource(scriptFilename).toURI();
        Path path = Paths.get(uri);

        String[] args = new String[scriptArgs.length + 1];
        args[0] = path.toAbsolutePath().toString();
        arraycopy(scriptArgs, 0, args, 1, scriptArgs.length);

        execute(args);
    }

}
