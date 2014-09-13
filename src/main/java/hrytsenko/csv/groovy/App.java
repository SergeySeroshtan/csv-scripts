package hrytsenko.csv.groovy;

import static java.lang.System.exit;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

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
        GroovyShell shell = new GroovyShell(bindings(args), configuration());
        try {
            InputStream scriptStream = Files.newInputStream(Paths.get(scriptFilename), StandardOpenOption.READ);
            shell.evaluate(new InputStreamReader(scriptStream, Charset.forName("UTF-8")));
        } catch (Exception exception) {
            LOGGER.error("Could not execute script.", exception);
            exit(-1);
        }

        exit(0);
    }

    private static CompilerConfiguration configuration() {
        ImportCustomizer importCustomizer = new ImportCustomizer();

        importCustomizer.addStarImports("hrytsenko.csv.core");
        importCustomizer.addStarImports("hrytsenko.csv.mediator");

        importCustomizer.addStaticStars(ScriptsDSL.class.getCanonicalName());
        importCustomizer.addStaticStars(ScriptsIO.class.getCanonicalName());
        importCustomizer.addStaticStars(ScriptsUtils.class.getCanonicalName());

        CompilerConfiguration configuration = new CompilerConfiguration();
        configuration.addCompilationCustomizers(importCustomizer);
        return configuration;
    }

    private static Binding bindings(String[] args) {
        Binding binding = new Binding();
        String[] scriptArgs = Arrays.copyOfRange(args, 1, args.length);
        binding.setVariable("args", scriptArgs);
        return binding;
    }

    private App() {
    }

}
