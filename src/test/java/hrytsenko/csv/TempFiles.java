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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Utility methods for work with temporary files in tests.
 * 
 * @author hrytsenko.anton
 */
public final class TempFiles {

    /**
     * The default character set for temporary files.
     */
    public static final Charset UTF_8 = Charset.forName("UTF-8");

    private TempFiles() {
    }

    /**
     * Creates temporary file.
     * 
     * @return the absolute path to file.
     * 
     * @throws IOException
     *             if file could not be created.
     */
    public static String createTempFile() throws IOException {
        Path path = Files.createTempFile(null, ".csv");
        path.toFile().deleteOnExit();
        return path.toAbsolutePath().toString();
    }

    /**
     * Writes data into temporary file.
     * 
     * @param path
     *            the path to file.
     * @param data
     *            the data to be written to file.
     * @param charset
     *            the character set to be used for file.
     * 
     * @throws IOException
     *             if data could not be written.
     */
    public static void writeTempFile(String path, String data, Charset charset) throws IOException {
        try (BufferedWriter dataWriter = Files.newBufferedWriter(Paths.get(path), charset, StandardOpenOption.WRITE)) {
            dataWriter.append(data);
        }
    }

    /**
     * Reads data from temporary file.
     * 
     * @param path
     *            the path to file.
     * @param charset
     *            the character set used in file.
     * 
     * @return the read data.
     * 
     * @throws IOException
     *             if data could not be read.
     */
    public static String readTempFile(String path, Charset charset) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(path), charset)) {
            StringBuilder data = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                data.append(line).append("\n");
            }
            return data.toString();
        }
    }

}
