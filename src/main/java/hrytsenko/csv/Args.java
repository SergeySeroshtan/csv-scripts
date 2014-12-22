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
import org.apache.commons.cli.HelpFormatter;
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
    public static final String SCRIPTS_OPT_NAME = "s";
    /**
     * The short name of argument that contains list of values.
     */
    public static final String VALUES_OPT_NAME = "v";

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
        CommandLineParser parser = new BasicParser();
        CommandLine line = parser.parse(getOptions(), args);

        String[] scripts = getValues(line, SCRIPTS_OPT_NAME);
        String[] values = getValues(line, VALUES_OPT_NAME);

        return new Args(scripts, values);
    }

    private static String[] getValues(CommandLine line, String option) {
        return line.hasOption(option) ? line.getOptionValues(option) : new String[0];
    }

    private static Options getOptions() {
        Options options = new Options();
        options.addOption(newOption(SCRIPTS_OPT_NAME, "filenames of scripts to execute", true));
        options.addOption(newOption(VALUES_OPT_NAME, "values to be passed into scripts", false));
        return options;
    }

    private static Option newOption(String name, String description, boolean required) {
        Option option = new Option(name, true, description);
        option.setArgs(UNLIMITED_VALUES);
        option.setRequired(required);
        return option;
    }

    /**
     * Prints help for application.
     */
    public static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("csv-scripts", getOptions());
    }

}
