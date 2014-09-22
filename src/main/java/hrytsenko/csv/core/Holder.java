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
package hrytsenko.csv.core;

/**
 * If mediator contains one or more values it should implement this interface.
 * 
 * @author hrytsenko.anton
 */
public interface Holder {

    /**
     * Gets value by its name.
     * 
     * <p>
     * In Groovy you can use overloaded operator <tt>[]</tt>.
     * 
     * @param name
     *            the name of required value.
     * 
     * @return the value or <code>null</code> if nothing found.
     */
    Object getAt(String name);

}
