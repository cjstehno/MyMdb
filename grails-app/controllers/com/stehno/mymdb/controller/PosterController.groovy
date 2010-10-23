package com.stehno.mymdb.controller

import com.stehno.mymdb.domain.Movie

class PosterController {

    def movieService

	def image = {
        def poster = movieService.findPoster(params.id as Long)
        if( poster ){
            response.outputStream.withStream { it << poster }
        } else {
            response.sendRedirect "${request.contextPath}/images/nocover.jpg"
        }
	}
}
