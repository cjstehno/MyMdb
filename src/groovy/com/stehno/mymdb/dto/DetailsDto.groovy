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
package com.stehno.mymdb.dto

class DetailsDto {

    String title
    String description
    Integer releaseYear
    String storageName
    Integer storageIndex

    static constraints = {
        title(validator:{ it && (1..100).contains(it.size()) })
        description(size:0..2000)
        releaseYear(nullable:false, range:1900..2100)
        storageName(validator:{ !it || (1..40).contains(it.size()) })
        storageIndex(nullable:true, min:0)
    }

    // TODO: see if there is a way to require both storage name and index if one is specified
}

