/*
   Copyright 2010 Christopher J. Stehno (chris@stehno.com)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.stehno.mymdb.controller

import com.stehno.mymdb.domain.Poster
import com.stehno.mymdb.dto.PosterType
import grails.converters.JSON
import com.stehno.mymdb.dto.PosterDto

class PosterController {

    def movieFlowService

    private static final def DEFAULT_POSTER = '/images/nocover.jpg'

    def list = {
        def posters = Poster.list().collect { [name:it.title, id:it.id]}
        render( [posters:posters] as JSON )
    }

    def show = {
        def poster = Poster.get(params.id)
        if( !poster ){
            response.outputStream.withStream { it << servletContext.getResource(DEFAULT_POSTER).getBytes() }
        } else {
            response.outputStream.withStream { it << poster.content }
        }
    }

    /**
     * Retrieves the poster image data that is currently held in the movie wizard
     * flow scope, PosterDto.
     */
    def flow = {
        def poster = movieFlowService.retrieve(PosterDto.class)
        if( poster.posterType == PosterType.NONE ){
            response.outputStream.withStream { it << servletContext.getResource(DEFAULT_POSTER).getBytes() }

        } else if( poster.posterType == PosterType.EXISTING ){
            response.outputStream.withStream { it << Poster.get(poster.posterId).content }

        } else if(poster.file && poster.file.size() > 0){
            response.outputStream.withStream { it << poster.file }

        } else {
            response.outputStream.withStream { it << servletContext.getResource(DEFAULT_POSTER).getBytes() }
        }
    }
}
