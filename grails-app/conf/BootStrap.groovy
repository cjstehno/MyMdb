import com.stehno.mymdb.domain.MymdbRole
import com.stehno.mymdb.domain.MymdbUser
import org.apache.shiro.crypto.hash.Sha512Hash

class BootStrap {

    def init = { servletContext ->

        // bootstrap initial user on installation
        if( MymdbUser.count() == 0 ){
            if(log.isWarnEnabled()) log.warn 'Creating roles and default user - change password!'
            
            def adminRole = new MymdbRole(name:"Administrator")
            adminRole.addToPermissions("*:*")
            adminRole.addToPermissions("admin")
            adminRole.save(flush:true)

            def userRole = new MymdbRole(name:"User")
            userRole.addToPermissions("browser:*")
            userRole.addToPermissions("actor:list")
            userRole.addToPermissions("genre:list")
            userRole.addToPermissions("storage:list")
            userRole.addToPermissions("poster:show")
            userRole.addToPermissions("api:*")
            userRole.save(flush:true)

            def user = new MymdbUser(username:'admin', passwordHash:new Sha512Hash('admin').toHex())
            user.save(flush:true)
            user.addToRoles(adminRole)
            user.save(flush:true)
        }

    }

    def destroy = {}
} 