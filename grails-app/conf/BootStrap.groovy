
import com.stehno.mymdb.domain.Genre
import com.stehno.mymdb.domain.Actor
import com.stehno.mymdb.domain.Storage

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
		new Actor(firstName:'Kurt',lastName:'Russel').save()
		new Actor(firstName:'Adam',lastName:'Sandler').save()
		new Actor(firstName:'Marlon',lastName:'Brando').save()
		new Actor(firstName:'Jack',lastName:'Nicolson').save()
		new Actor(firstName:'Alec',lastName:'Baldwin').save()
		
		new Storage(name:'A', index:1).save()
		new Storage(name:'A', index:2).save()
		new Storage(name:'B', index:1).save()
		
     }

     def destroy = {
     }
} 