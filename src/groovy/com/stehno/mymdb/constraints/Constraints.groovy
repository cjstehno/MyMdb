/*
 * Copyright (c) 2011 Christopher J. Stehno (chris@stehno.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stehno.mymdb.constraints

/**
 * A collections of custom constraints.
 *
 * Import this class statically and then use the constraint functions to create the custom validators.
 *
 * @author cjstehno
 */
class Constraints {

    /**
     * Validates that the value is either groovy false (null or empty) or has a size within
     * the given range.
     *
     * classname.propertyname + not.nullorbetween
     *
     * @param sizeRange
     * @return
     */
    static def nullOrBetween( Range sizeRange ){
        return [ validator:{ val-> ( !val || sizeRange.containsWithinBounds(val.size()) ) ?: 'not.nullorbetween' } ]
    }

    /**
     * Validates that the value is either not groovy false (null or empty) and has a size
     * within the given range.
     *
     * classname.propertyname + not.notnullandbetween
     * 
     * @param sizeRange
     * @return
     */
    static def notNullAndBetween( Range sizeRange ){
        return [ validator:{ val-> ( val && sizeRange.containsWithinBounds(val.size()) ) ?: 'not.notnullandbetween' } ]
    }
}
