
import com.stehno.mymdb.domain.Poster
import com.stehno.mymdb.domain.Role
import com.stehno.mymdb.domain.User

class BootStrap {

    def init = { servletContext ->
        // TODO: remove this - seed images for dev
        new Poster( title:'Alpha', content:'http://t0.gstatic.com/images?q=tbn:ANd9GcRMpibYq5mPC0rSL-M42s-k1oeaQmidaCuXtNxr4wlc1c0MuYz6'.toURL().getBytes() ).save()
        new Poster( title:'Bravo', content:'http://t3.gstatic.com/images?q=tbn:ANd9GcRvedcQP3UBeuTPT7FVickMWObATt1hqcyIHvuw1vRKW58W5oJB'.toURL().getBytes() ).save()
        new Poster( title:'Charlie', content:'http://t3.gstatic.com/images?q=tbn:ANd9GcR-BBkTzvuffVar6zlkFiHbCYzh4oMpkocwNCjPkrWPuSy2OfdX'.toURL().getBytes() ).save()
    }

    def destroy = {}
} 