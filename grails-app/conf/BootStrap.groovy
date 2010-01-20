
import com.stehno.mymdb.domain.Genre
import com.stehno.mymdb.domain.Actor
import com.stehno.mymdb.domain.Storage
import com.stehno.mymdb.domain.Movie

class BootStrap {

     def init = { servletContext ->
		
		// security bootstrapping
		if( User.count() == 0 ){
			def role = new Role(authority:'ROLE_ADMIN', description:'Admin role').save()
			def admin = new User(
				username:'admin', 
				userRealName:'admin', 
				passwd:authenticateService.encodePassword('admin'),
				enabled:true, 
				email:'admin@servername'
			)
			admin.addToAuthorities(role)
			admin.save()
		}
		
     }

     def destroy = {
     }
} 