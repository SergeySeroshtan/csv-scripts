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

import hrytsenko.csv.core.Condition;
import hrytsenko.csv.core.Mediator;
import hrytsenko.csv.core.Record;

/**
 * Applies mediators to all records that meet specified condition.
 * 
 * @author hrytsenko.anton
 */
public class Filter extends Container {

    private Condition condition;

    /**
     * Creates filter, that ignores all records.
     */
    public Filter() {
        condition = new Condition.IgnoreAll();
    }

    /**
     * Sets the condition that will be used to filter records.
     * 
     * @param condition
     *            the condition to filter records.
     * 
     * @return this mediator for chaining.
     */
    public Filter when(Condition condition) {
        this.condition = condition;
        return this;
    }

    @Override
    public void mediate(Record record) {
        if (!condition.check(record)) {
            return;
        }
        for (Mediator descendant : descendants()) {
            descendant.mediate(record);
        }
    }

}
