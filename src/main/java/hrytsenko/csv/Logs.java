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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The methods for logging.
 * 
 * @author hrytsenko.anton
 */
public final class Logs {

    private static final Logger LOGGER = LoggerFactory.getLogger("SCRIPT");

    private Logs() {
    }

    /**
     * Adds message to the log.
     * 
     * @param message
     *            the message to be added.
     */
    public static void info(String message) {
        LOGGER.info(message);
    }

}
