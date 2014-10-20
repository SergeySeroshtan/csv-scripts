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

import static java.lang.System.exit;
import static java.util.Arrays.copyOfRange;
import static java.util.Collections.singletonMap;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application that executes Groovy scripts.
 * 
 * @author hrytsenko.anton
 */
public final class App {

    private static final Logger LOGGER = LoggerFactory.getLogger("APP");

    private App() {
    }

    /**
     * Creates environment and executes script.
     * 
     * <p>
     * First argument is the filename of script, and all others will be passed into this script.
     * 
     * @param args
     *            the command-line arguments
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            LOGGER.error("Script not defined.");
            exit(-1);
        }

        String scriptFilename = args[0];
        try {
            InputStream scriptStream = Files.newInputStream(Paths.get(scriptFilename), StandardOpenOption.READ);
            execute(scriptStream, copyOfRange(args, 1, args.length));
        } catch (Exception exception) {
            LOGGER.error("Could not execute script.", exception);
            exit(-1);
        }

        exit(0);
    }

    /**
     * Executes script.
     * 
     * @param scriptStream
     *            the stream to be read.
     * @param args
     *            the arguments to be passed into script.
     */
    public static void execute(InputStream scriptStream, String[] args) {
        Binding binding = new Binding(singletonMap("args", args));
        GroovyShell shell = new GroovyShell(binding, configuration());

        shell.evaluate(new InputStreamReader(scriptStream, Charset.forName("UTF-8")));
    }

    private static CompilerConfiguration configuration() {
        ImportCustomizer importCustomizer = new ImportCustomizer();

        importCustomizer.addImports(Record.class.getCanonicalName());

        importCustomizer.addStaticStars(Logs.class.getCanonicalName());
        importCustomizer.addStaticStars(Records.class.getCanonicalName());

        CompilerConfiguration configuration = new CompilerConfiguration();
        configuration.addCompilationCustomizers(importCustomizer);
        return configuration;
    }

}
