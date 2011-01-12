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

class PosterController {

    private static final def DEFAULT_POSTER = '/images/nocover.jpg'

    def movieService

    /**
     * Retrieves the poster image data for a specific movie instance.
     *
     * @param id the movie id whose poster is to be rendered
     */
	def image = {
        def poster = movieService.findPoster(params.id as Long)
        if( !poster ){
            poster = servletContext.getResource(DEFAULT_POSTER).getBytes()
        }
        response.outputStream.withStream { it << poster }
	}

    // TODO: move this to a flow service
    private static final def FLOWKEY = 'movie.flow'

    /**
     * Retrieves the poster image data that is currently held in the movie wizard
     * flow scope, PosterDto.
     */
    def flow = {
        def f = session[FLOWKEY]
        if( f == null || f.poster == null || f.poster.posterType == null || f.poster.posterType == PosterType.NONE ){
            response.outputStream.withStream { it << servletContext.getResource(DEFAULT_POSTER).getBytes() }
        } else if(f.poster.posterType == PosterType.EXISTING){
            response.outputStream.withStream { it << Poster.get(f.poster.posterId).content }
        } else {
            response.outputStream.withStream { it << f.poster.file }
        }
    }
}
