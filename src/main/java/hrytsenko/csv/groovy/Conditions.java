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
package hrytsenko.csv.groovy;

import hrytsenko.csv.core.Condition;
import hrytsenko.csv.core.Record;

/**
 * The methods to be used in scripts to work with conditions.
 * 
 * @author hrytsenko.anton
 */
public final class Conditions {

    private Conditions() {
    }

    /**
     * Allows to define custom condition using closure in Groovy.
     * 
     * <p>
     * I.e., you can write: <tt>def custom = check({…})</tt>; instead of: <tt>def custom = {…} as Condition</tt>.
     * 
     * @param mediator
     *            the custom condition.
     * 
     * @return the custom condition.
     */
    public static Condition check(Condition condition) {
        return condition;
    }

    /**
     * Returns the logical negation of given condition.
     * 
     * @param condition
     *            the original condition.
     * 
     * @return the condition that represents the logical negation of given condition.
     */
    public static Condition not(final Condition condition) {
        return new Condition() {

            @Override
            public boolean check(Record record) {
                return !condition.check(record);
            }

        };
    }

}
