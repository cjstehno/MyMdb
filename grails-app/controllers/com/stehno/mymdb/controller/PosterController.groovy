package com.stehno.mymdb.controller

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

    private static final def FLOWKEY = 'movie.flow'

    def flow = {
        def f = session[FLOWKEY]
        if(f && f.poster.posterType != 'none'){
             response.outputStream.withStream { it << f.poster.file }
        } else {
            response.sendRedirect "${request.contextPath}/images/nocover.jpg"
        }
    }
}
