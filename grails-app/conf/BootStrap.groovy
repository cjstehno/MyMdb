
import com.stehno.mymdb.domain.Poster
import com.stehno.mymdb.domain.Role
import com.stehno.mymdb.domain.User

class BootStrap {
	def authenticateService
	
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

        // TODO: remove this - seed images for dev
        new Poster( title:'Alpha', content:'http://t0.gstatic.com/images?q=tbn:ANd9GcRMpibYq5mPC0rSL-M42s-k1oeaQmidaCuXtNxr4wlc1c0MuYz6'.toURL().getBytes() ).save()
//        new Poster( title:'Bravo', content:'http://t3.gstatic.com/images?q=tbn:ANd9GcRvedcQP3UBeuTPT7FVickMWObATt1hqcyIHvuw1vRKW58W5oJB'.toURL().getBytes() ).save()
//        new Poster( title:'Charlie', content:'http://t3.gstatic.com/images?q=tbn:ANd9GcR-BBkTzvuffVar6zlkFiHbCYzh4oMpkocwNCjPkrWPuSy2OfdX'.toURL().getBytes() ).save()
    }

    def destroy = {}
} 