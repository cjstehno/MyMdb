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
 * Constraint requiring that a String value be null, or have a size between two specified values.
 *
 * Params:
 *  start (int) starting value of range
 *  end (int) ending value of range
 */
class NullOrSizeBetweenConstraint {

    static expectsParams = ['start', 'end']
    static defaultMessageCode = 'default.not.nullorbetween.message'

    def supports = { type ->
        return type!= null && String.class.isAssignableFrom(type);
    }

    def validate = { propertyValue ->
        propertyValue != null && (start..end).containsWithinBounds(propertyValue.size())
    }
}
