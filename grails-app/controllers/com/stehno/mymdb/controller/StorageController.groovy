package com.stehno.mymdb.controller

import com.stehno.mymdb.domain.Storage
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_ADMIN'])
class StorageController {

    def scaffold = Storage
	
}
