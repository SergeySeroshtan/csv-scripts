/**
 * Copyright (C) 2014 Anton Hrytsenko (hrytsenko.anton@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package hrytsenko.csv.mediator;

import hrytsenko.csv.core.Mediator;
import hrytsenko.csv.core.Record;

/**
 * Splits copies of record between mediators.
 * 
 * @author hrytsenko.anton
 */
public class Splitter extends Container {

    /**
     * Creates empty splitter.
     */
    public Splitter() {
    }

    @Override
    public void mediate(Record record) {
        for (Mediator descendant : descendants()) {
            Record copy = new Record();
            copy.putAll(record.content());
            descendant.mediate(copy);
        }
    }

}
