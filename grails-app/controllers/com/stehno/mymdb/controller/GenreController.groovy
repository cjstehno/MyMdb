package com.stehno.mymdb.controller

import com.stehno.mymdb.domain.Genre
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_ADMIN'])
class GenreController {

   def scaffold = Genre
}
