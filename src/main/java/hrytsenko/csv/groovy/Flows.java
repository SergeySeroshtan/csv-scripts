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
package hrytsenko.csv.groovy;

import hrytsenko.csv.core.Condition;
import hrytsenko.csv.core.Mediator;
import hrytsenko.csv.core.Record;
import hrytsenko.csv.mediator.Filter;
import hrytsenko.csv.mediator.Sequence;
import hrytsenko.csv.mediator.Splitter;

/**
 * The methods to be used in scripts to work with mediators.
 * 
 * @author hrytsenko.anton
 */
public final class Flows {

    private Flows() {
    }

    /**
     * Allows to define custom mediator using closure in Groovy.
     * 
     * <p>
     * I.e., you can write: <tt>def custom = apply({…})</tt>; instead of: <tt>def custom = {…} as Mediator</tt>.
     * 
     * @param mediator
     *            the custom mediator.
     * 
     * @return the custom mediator.
     */
    public static Mediator apply(Mediator mediator) {
        return mediator;
    }

    /**
     * Creates {@link Filter} with specified condition.
     * 
     * @param condition
     *            the custom condition.
     * 
     * @return the created mediator.
     */
    public static Filter filter(Condition condition) {
        return new Filter().when(condition);
    }

    /**
     * Creates {@link Sequence} of mediators.
     * 
     * @param mediators
     *            the set of mediators for {@link Sequence}.
     * 
     * @return the created mediator.
     */
    public static Sequence sequence(Mediator... mediators) {
        Sequence sequence = new Sequence();
        sequence.over(mediators);
        return sequence;
    }

    /**
     * Creates {@link Splitter} over set of mediators.
     * 
     * @param mediators
     *            the set of mediators for {@link Splitter}.
     * 
     * @return the created mediator.
     */
    public static Splitter split(Mediator... mediators) {
        Splitter splitter = new Splitter();
        splitter.over(mediators);
        return splitter;
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
