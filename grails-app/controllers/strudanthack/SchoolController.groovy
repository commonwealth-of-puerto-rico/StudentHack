package strudanthack

import org.springframework.dao.DataIntegrityViolationException

/**
 * SchoolController
 * A controller class handles incoming web requests and performs actions such as redirects, rendering views and so on.
 */
class SchoolController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [schoolInstanceList: School.list(params), schoolInstanceTotal: School.count()]
    }

    def create() {
        [schoolInstance: new School(params)]
    }

    def save() {
        def schoolInstance = new School(params)
        if (!schoolInstance.save(flush: true)) {
            render(view: "create", model: [schoolInstance: schoolInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'school.label', default: 'School'), schoolInstance.id])
        redirect(action: "show", id: schoolInstance.id)
    }

    def show() {
        def schoolInstance = School.get(params.id)
        if (!schoolInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'school.label', default: 'School'), params.id])
            redirect(action: "list")
            return
        }

        [schoolInstance: schoolInstance]
    }

    def edit() {
        def schoolInstance = School.get(params.id)
        if (!schoolInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'school.label', default: 'School'), params.id])
            redirect(action: "list")
            return
        }

        [schoolInstance: schoolInstance]
    }

    def update() {
        def schoolInstance = School.get(params.id)
        if (!schoolInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'school.label', default: 'School'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (schoolInstance.version > version) {
                schoolInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'school.label', default: 'School')] as Object[],
                        "Another user has updated this School while you were editing")
                render(view: "edit", model: [schoolInstance: schoolInstance])
                return
            }
        }

        schoolInstance.properties = params

        if (!schoolInstance.save(flush: true)) {
            render(view: "edit", model: [schoolInstance: schoolInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'school.label', default: 'School'), schoolInstance.id])
        redirect(action: "show", id: schoolInstance.id)
    }

    def delete() {
        def schoolInstance = School.get(params.id)
        if (!schoolInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'school.label', default: 'School'), params.id])
            redirect(action: "list")
            return
        }

        try {
            schoolInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'school.label', default: 'School'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'school.label', default: 'School'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
