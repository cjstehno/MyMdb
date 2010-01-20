package com.stehno.mymdb.controller

import com.stehno.mymdb.domain.Actor
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_ADMIN'])
class ActorController {

	def scaffold = Actor
}
