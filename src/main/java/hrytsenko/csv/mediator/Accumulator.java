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

import hrytsenko.csv.core.Holder;
import hrytsenko.csv.core.Mediator;

/**
 * Accumulator contains data bound to name.
 * 
 * Thus this data can be accessed only through this name.
 * 
 * @author hrytsenko.anton
 */
public abstract class Accumulator implements Mediator, Holder {

    private String name;

    /**
     * Assigns name for value.
     * 
     * @param name
     *            the name for value.
     * 
     * @return this mediator for chaining.
     */
    public Mediator into(String name) {
        this.name = name;
        return this;
    }

    @Override
    public Object getAt(String name) {
        if (name == null) {
            return null;
        }

        return name.equals(this.name) ? value() : null;
    }

    /**
     * Returns the accumulated value.
     * 
     * @return the accumulated value.
     */
    protected abstract Object value();

}
