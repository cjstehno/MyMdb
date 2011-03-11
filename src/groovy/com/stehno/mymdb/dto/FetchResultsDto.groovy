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

/**
 * DTO for the Movie fetch panel of the movie flow. This DTO has two modes:
 *
 * Created Mode:
 *  User enters a title which is passed forward.
 *
 * Fetched Mode
 *  User selects fetched movie (selectedId, and providerId) which will be
 *  used to fetch the movie data.
 */
class FetchResultsDto {

    String title

    String selectedId
    String providerId

    static constraints = {
        title( nullable:true, size:1..100 )
        selectedId( nullable:true, blank:true )
        providerId( nullable:true, blank:true )
    }
}
