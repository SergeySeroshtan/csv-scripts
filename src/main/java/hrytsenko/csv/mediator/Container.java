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

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableCollection;
import hrytsenko.csv.core.Holder;
import hrytsenko.csv.core.Mediator;

import java.util.Collection;
import java.util.List;

/**
 * Combines other mediators.
 * 
 * @author hrytsenko.anton
 */
public abstract class Container implements Mediator, Holder {

    private List<Mediator> descendants;

    /**
     * Creates an empty container.
     */
    protected Container() {
        descendants = emptyList();
    }

    /**
     * Replaces mediators in container.
     * 
     * @param mediators
     *            the set of mediators to be added to container.
     * 
     * @return this mediator for chaining.
     */
    public Mediator over(Mediator... mediators) {
        descendants = asList(mediators);
        return this;
    }

    @Override
    public Object getAt(String name) {
        if (name == null) {
            return null;
        }

        Collection<Mediator> mediators = descendants();
        for (Mediator mediator : mediators) {
            if (mediator instanceof Holder) {
                Object value = ((Holder) mediator).getAt(name);
                if (value != null) {
                    return value;
                }
            }
        }

        return null;
    }

    /**
     * Returns the set from mediators that are present in container.
     * 
     * @return the ordered set of mediators.
     */
    protected Collection<Mediator> descendants() {
        return unmodifiableCollection(descendants);
    }

}
