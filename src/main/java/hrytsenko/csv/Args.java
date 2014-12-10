package hrytsenko.csv;

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
        this.scripts = scripts;
        this.values = values;
    }

    /**
     * Returns the scripts to execute.
     * 
     * @return the list of filenames.
     */
    public String[] getScripts() {
        return scripts;
    }

    /**
     * Returns the list of values to be passed into scripts.
     * 
     * @return the list of string values.
     */
    public String[] getValues() {
        return values;
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
