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

import static java.util.Arrays.copyOf;
import static org.apache.commons.cli.Option.UNLIMITED_VALUES;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Command-line arguments.
 * 
 * @author hrytsenko.anton
 */
public class Args {

    /**
     * The short name of argument that contains list of scripts.
     */
    public static final String SCRIPTS_ARG_NAME = "s";
    /**
     * The short name of argument that contains list of values.
     */
    public static final String VALUES_ARG_NAME = "v";

    private final String[] scripts;
    private final String[] values;

    /**
     * Create arguments.
     * 
     * @param scripts
     *            the list of scripts.
     * @param values
     *            the list of string values.
     */
    public Args(String[] scripts, String[] values) {
        this.scripts = copyOf(scripts, scripts.length);
        this.values = copyOf(values, values.length);
    }

    /**
     * Returns the scripts to execute.
     * 
     * @return the list of filenames.
     */
    public String[] getScripts() {
        return copyOf(scripts, scripts.length);
    }

    /**
     * Returns the list of values to be passed into scripts.
     * 
     * @return the list of string values.
     */
    public String[] getValues() {
        return copyOf(values, values.length);
    }

    /**
     * Parses command-line arguments.
     * 
     * @param args
     *            the command-line arguments.
     * 
     * @return the parsed arguments.
     * 
     * @throws ParseException
     *             if command-line arguments could not be parsed.
     */
    public static Args parseArgs(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption(newOptionWithValues(SCRIPTS_ARG_NAME, "filenames of scripts to execute"));
        options.addOption(newOptionWithValues(VALUES_ARG_NAME, "values to be passed into scripts"));

        CommandLineParser parser = new BasicParser();
        CommandLine line = parser.parse(options, args);

        String[] scripts = getOptionValues(line, SCRIPTS_ARG_NAME);
        String[] values = getOptionValues(line, VALUES_ARG_NAME);

        return new Args(scripts, values);
    }

    private static Option newOptionWithValues(String name, String description) {
        Option option = new Option(name, true, description);
        option.setArgs(UNLIMITED_VALUES);
        return option;
    }

    private static String[] getOptionValues(CommandLine line, String name) {
        return line.hasOption(name) ? line.getOptionValues(name) : new String[0];
    }

}
