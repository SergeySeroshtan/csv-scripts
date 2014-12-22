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
import static hrytsenko.csv.Args.printHelp;
import static java.lang.System.exit;
import static java.lang.Thread.currentThread;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.newBufferedReader;
import static java.util.Collections.singletonMap;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.cli.ParseException;
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
        try {
            execute(args);
        } catch (ParseException exception) {
            printHelp();
            exit(-1);
        } catch (Exception exception) {
            LOGGER.error("Could not execute script.", exception);
            exit(-1);
        }

        exit(0);
    }

    /**
     * Executes script with given arguments.
     * 
     * <p>
     * First argument should contain path to file with script. Other arguments are optional and they will be passed into
     * scripts as is.
     * 
     * @param args
     *            arguments for execution.
     */
    protected static void execute(String[] args) throws ParseException, IOException {
        Args parsedArgs = parseArgs(args);

        Binding binding = new Binding(singletonMap("args", parsedArgs.getValues()));
        GroovyShell shell = new GroovyShell(binding, configuration());

        executeEmbeddedScripts(shell);

        for (String script : parsedArgs.getScripts()) {
            Path path = Paths.get(script);
            LOGGER.info("Execute: {}.", path.getFileName());
            try (BufferedReader reader = newBufferedReader(path, UTF_8)) {
                shell.evaluate(reader);
            }
        }
    }

    private static void executeEmbeddedScripts(GroovyShell shell) throws IOException {
        try {
            shell.evaluate(currentThread().getContextClassLoader().getResource("ext/Collections.groovy").toURI());
        } catch (URISyntaxException exception) {
            throw new IOException("Invalid path for embedded script.", exception);
        }
    }

    private static CompilerConfiguration configuration() {
        ImportCustomizer importCustomizer = new ImportCustomizer();

        importCustomizer.addImports(Record.class.getCanonicalName());

        importCustomizer.addStaticStars(IO.class.getCanonicalName());
        importCustomizer.addStaticStars(Logs.class.getCanonicalName());
        importCustomizer.addStaticStars(Records.class.getCanonicalName());

        CompilerConfiguration configuration = new CompilerConfiguration();
        configuration.addCompilationCustomizers(importCustomizer);
        return configuration;
    }

}
