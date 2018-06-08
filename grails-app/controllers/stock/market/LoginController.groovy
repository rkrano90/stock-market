package stock.market

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.SpringSecurityUtils

import javax.servlet.http.HttpServletResponse

class LoginController {

    SpringSecurityService springSecurityService

    def index() {
        if(!springSecurityService.isLoggedIn()) {
           return [loginMsg:params.loginMsg]
        }
        redirect(controller:'stockMarket', action:'index')
    }

    def saveUser = {
        def user = new User()
        user.username = params.registerUsername
        user.password = params.registerPassword
        if(!user.save(flush:true, failOnError:true)){
            return render(view:'index', model:[loginMsg:"User Already Exists"])
        }
        render(view:'index', model:[loginMsg:"User Created"])
    }

    def authAjax() {
        response.setHeader'Location', SpringSecurityUtils.securityConfig.auth.ajaxLoginFormUrl
        response.sendError HttpServletResponse.SC_UNAUTHORIZED
    }

    def failedLogin = {
        render(view:'index', model:[loginMsg:"Incorrect Username or Password"])
    }
}
