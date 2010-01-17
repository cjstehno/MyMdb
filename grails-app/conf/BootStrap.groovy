
import com.stehno.mymdb.domain.Genre
import com.stehno.mymdb.domain.Actor
import com.stehno.mymdb.domain.Storage
import com.stehno.mymdb.domain.Movie

class BootStrap {

     def init = { servletContext ->
	 
        new Genre(name:'Comedy').save()
        new Genre(name:'Drama').save()
        new Genre(name:'Action').save()
        new Genre(name:'SciFi').save()
        new Genre(name:'Horror').save()
        new Genre(name:'Romance').save()
		new Genre(name:'Suspense').save()
		new Genre(name:'Mystery').save()
		new Genre(name:'Childrens').save()
		new Genre(name:'Educational').save()
		new Genre(name:'Slapstick').save()
		new Genre(name:'Porn').save()
		
		new Actor(firstName:'Michael', middleName:'J',lastName:'Fox').save()
		new Actor(firstName:'Kurt',    middleName:'', lastName:'Russel').save()
		new Actor(firstName:'Adam',    middleName:'', lastName:'Sandler').save()
		new Actor(firstName:'Marlon',  middleName:'', lastName:'Brando').save()
		new Actor(firstName:'Jack',    middleName:'', lastName:'Nicolson').save()
		new Actor(firstName:'Alec',    middleName:'', lastName:'Baldwin').save()
     }

     def destroy = {
     }
} 