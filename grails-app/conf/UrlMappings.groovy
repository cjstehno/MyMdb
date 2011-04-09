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

class UrlMappings {
    static mappings = {
        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }
        
        "/"(view:"/index")
        "500"(view:'/error')

        //
        // movie flow mappings
        //

        "/movie/fetch"(controller:"movieFetch"){
            action = [GET:"show", POST:"save"]
        }
        "/movie/fetch/search"(controller:"movieFetch", action:'search')
        "/movie/fetch/preview/$provider/$movie"(controller:"movieFetch", action:'preview')

        "/movie/details/$id?"(controller:"movieDetails"){
            action = [GET:"show", POST:"save"]
        }
        '/movie/details/storage'(controller:'movieDetails', action:'storage')

        "/movie/poster"(controller:"moviePoster"){
            action = [GET:"show", POST:"save"]
        }
        "/movie/poster/fetch"(controller:"moviePoster", action:'fetch')
        "/movie/poster/select"(controller:"moviePoster", action:'select')
        "/movie/poster/clear"(controller:"moviePoster", action:'clear')

        "/movie/genres"(controller:"movieGenres"){
            action = [GET:"show", POST:"save"]
        }
        '/movie/genres/list'(controller:'movieGenres', action:'list')

        "/movie/actors"(controller:"movieActors"){
            action = [GET:"show", POST:"save"]
        }
        '/movie/actors/list'(controller:'movieActors', action:'list')

        "/movie/websites"(controller:"movieWebSite"){
            action = [GET:"show", POST:"save"]
        }
        "/movie/websites/sites"(controller:"movieWebSite", action:'sites')

        "/movie/summary"(controller:"movieSummary"){
            action = [GET:"show", POST:"save"]
        }

        "/api/categories/$filter"(controller:'api', action:'categories')
        "/api/list/$filter"(controller:'api', action:'list')
        "/api/fetch/$id"(controller:'api', action:'fetch')
	}
}
