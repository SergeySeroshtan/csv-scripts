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
package hrytsenko.csv.core;

/**
 * Conditions are intended to check that record meets criteria.
 * 
 * @author hrytsenko.anton
 */
public interface Condition {

    /**
     * Checks that record meets criteria.
     * 
     * @param record
     *            the record to be checked.
     * 
     * @return <code>true</code> if record meets criteria; <code>false</code> otherwise.
     */
    boolean check(Record record);

    /**
     * Condition that ignores all records.
     * 
     * @author hrytsenko.anton
     */
    class IgnoreAll implements Condition {

        @Override
        public boolean check(Record record) {
            return false;
        }

    }

}
