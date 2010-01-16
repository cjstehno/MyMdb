package com.stehno.mymdb.controller

import com.stehno.mymdb.domain.Poster

class PosterController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }
	
	def image = {
        def posterInstance = Poster.get(params.id)
        if (!posterInstance) {
			response.sendError(404,"${message(code: 'default.not.found.message', args: [message(code: 'poster.label', default: 'Poster'), params.id])}")
        } else {
            response.outputStream.withStream {
				it << posterInstance.data
			}
        }
	}

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [posterInstanceList: Poster.list(params), posterInstanceTotal: Poster.count()]
    }

    def create = {
        def posterInstance = new Poster()
        posterInstance.properties = params
        return [posterInstance: posterInstance]
    }

    def save = {
        def posterInstance = new Poster(params)
        if (posterInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'poster.label', default: 'Poster'), posterInstance.id])}"
            redirect(action: "show", id: posterInstance.id)
        }
        else {
            render(view: "create", model: [posterInstance: posterInstance])
        }
    }

    def show = {
        def posterInstance = Poster.get(params.id)
        if (!posterInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'poster.label', default: 'Poster'), params.id])}"
            redirect(action: "list")
        }
        else {
            [posterInstance: posterInstance]
        }
    }

    def edit = {
        def posterInstance = Poster.get(params.id)
        if (!posterInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'poster.label', default: 'Poster'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [posterInstance: posterInstance]
        }
    }

    def update = {
        def posterInstance = Poster.get(params.id)
        if (posterInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (posterInstance.version > version) {
                    
                    posterInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'poster.label', default: 'Poster')] as Object[], "Another user has updated this Poster while you were editing")
                    render(view: "edit", model: [posterInstance: posterInstance])
                    return
                }
            }
            posterInstance.properties = params
            if (!posterInstance.hasErrors() && posterInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'poster.label', default: 'Poster'), posterInstance.id])}"
                redirect(action: "show", id: posterInstance.id)
            }
            else {
                render(view: "edit", model: [posterInstance: posterInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'poster.label', default: 'Poster'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def posterInstance = Poster.get(params.id)
        if (posterInstance) {
            try {
                posterInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'poster.label', default: 'Poster'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'poster.label', default: 'Poster'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'poster.label', default: 'Poster'), params.id])}"
            redirect(action: "list")
        }
    }
}
