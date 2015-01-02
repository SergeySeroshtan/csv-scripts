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

import static hrytsenko.csv.App.main;
import static hrytsenko.csv.Args.SCRIPTS_OPT_NAME;
import static hrytsenko.csv.Args.VALUES_OPT_NAME;
import static hrytsenko.csv.TempFiles.createTempFile;
import static hrytsenko.csv.TempFiles.writeTempFile;
import static java.lang.Thread.currentThread;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

/**
 * Tests for application based on Groovy scripts.
 * 
 * <p>
 * These tests use temporary files, see {@link TempFiles}.
 * 
 * @author hrytsenko.anton
 */
public class AppTest {

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void testArgsEmpty() throws Exception {
        exit.expectSystemExitWithStatus(-1);
        main(new String[] {});
    }

    @Test
    public void testArgs() throws Exception {
        String tempFilePath = createTempFile();
        String tempFileData = "ticker,exchange\nGOOG,NASDAQ\nORCL,NYSE\nMSFT,NASDAQ";
        writeTempFile(tempFilePath, tempFileData, UTF_8);
        executeScript("ArgsTest.groovy", tempFilePath, "NASDAQ", "2");
    }

    @Test
    public void testCollections() throws Exception {
        executeScript("CollectionsTest.groovy");
    }

    @Test
    public void testFiles() throws Exception {
        String tempFilePath = createTempFile();
        String tempFileData = "TICKER,EXCHANGE\nGOOG,NASDAQ\nORCL,NYSE\nMSFT,NASDAQ";
        writeTempFile(tempFilePath, tempFileData, UTF_8);
        executeScript("FilesTest.groovy", tempFilePath);
    }

    @Test
    public void testRecords() throws Exception {
        executeScript("RecordsTest.groovy");
    }

    private void executeScript(String script, String... values) throws Exception {
        URI uri = currentThread().getContextClassLoader().getResource(script).toURI();
        Path path = Paths.get(uri).toAbsolutePath();

        List<String> args = new ArrayList<>();
        args.add("-" + SCRIPTS_OPT_NAME);
        args.add(path.toString());
        if (values.length > 0) {
            args.add("-" + VALUES_OPT_NAME);
            args.addAll(asList(values));
        }

        exit.expectSystemExitWithStatus(0);
        main(args.toArray(new String[args.size()]));
    }

}
