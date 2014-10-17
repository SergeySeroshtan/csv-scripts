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
package hrytsenko.csv.mediator;

import hrytsenko.csv.core.Mediator;
import hrytsenko.csv.core.Record;

/**
 * Combines mediators for sequential processing of records.
 * 
 * @author hrytsenko.anton
 */
public class Sequence extends Container {

    /**
     * Creates empty sequence.
     */
    public Sequence() {
    }

    @Override
    public void mediate(Record record) {
        for (Mediator descendant : descendants()) {
            descendant.mediate(record);
        }
    }

}
