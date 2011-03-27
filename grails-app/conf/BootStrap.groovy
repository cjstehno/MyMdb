import com.stehno.mymdb.domain.MymdbRole
import com.stehno.mymdb.domain.MymdbUser
import org.apache.shiro.crypto.hash.Sha512Hash

class BootStrap {

    def init = { servletContext ->

        // bootstrap initial user on installation
        if( MymdbUser.count() == 0 ){
            def adminRole = new MymdbRole(name:"Administrator")
            adminRole.addToPermissions("*:*")
            adminRole.addToPermissions("admin")
            adminRole.save(flush:true)

            def userRole = new MymdbRole(name:"User")
            userRole.addToPermissions("browser:*")
            userRole.save(flush:true)

            def user = new MymdbUser(username:'admin', passwordHash:new Sha512Hash('admin').toHex())
            user.save(flush:true)
            user.addToRoles(adminRole)
            user.save(flush:true)
        }
        
    }

    def destroy = {}
} 