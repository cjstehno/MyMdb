package com.stehno.mymdb.controller

import grails.converters.JSON
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.AuthenticationException
import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.web.util.WebUtils
import org.springframework.mobile.device.DeviceUtils

class AuthController {
    def shiroSecurityManager

    def index = { redirect(action: "login", params: params) }

    def login = {
        def model = [ username:params.username, rememberMe:(params.rememberMe != null), targetUri:params.targetUri ]

        if( DeviceUtils.getCurrentDevice( request ).isMobile() ){
            render( view:'mobilelogin', model:model )
        } else {
            render( view:'login', model:model )
        }
    }

    def signIn = {
        println "SignIn: ${params.username}"
        def authToken = new UsernamePasswordToken(params.username, params.password as String)

        // Support for "remember me"
        if (params.rememberMe) {
            authToken.rememberMe = true
        }
        
        // If a controller redirected to this page, redirect back
        // to it. Otherwise redirect to the root URI.
        def targetUri = params.targetUri ?: "/"
        
        // Handle requests saved by Shiro filters.
        def savedRequest = WebUtils.getSavedRequest(request)
        if (savedRequest) {
            targetUri = savedRequest.requestURI - request.contextPath
            if (savedRequest.queryString) targetUri = targetUri + '?' + savedRequest.queryString
        }
        
        try {
            // Perform the actual login. An AuthenticationException will be thrown if the username is unrecognised or the password is incorrect.
            SecurityUtils.subject.login(authToken)

            log.info "Redirecting to '${targetUri}'."

            if( DeviceUtils.getCurrentDevice( request ).isMobile() ){
                redirect( controller:'mobile' )
            } else {
                render( [success:true] as JSON )
            }

        } catch (AuthenticationException ex){
            // Authentication failed, so display the appropriate message on the login page.
            log.info "Authentication failure for user '${params.username}'."

            if( DeviceUtils.getCurrentDevice( request ).isMobile() ){
                render( view:'mobilelogin', model:['general':message(code:'login.failed')] )   
            } else {
                render( [success:false, errors:['general':message(code:'login.failed')]] as JSON )
            }
        }
    }

    def signOut = {
        // Log the user out of the application.
        SecurityUtils.subject?.logout()

        // For now, redirect back to the home page.
        redirect(uri: "/")
    }

    def unauthorized = {
        redirect(action: "login", params: params)
    }
}
