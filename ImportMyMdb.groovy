import groovy.sql.Sql

/*
 * Experimental importer...
 */
class ImportMyMdb {

    static def main( args ){
//        def jdbcUrl = 'jdbc:hsqldb:/home/cjstehno/hsql/mymdb/mymdb' //args[0]
        def infile = new File('/home/cjstehno/Desktop/mymdb.dat' /*args[1]*/)

        def inf
        try {
            def sql = null // Sql.newInstance(jdbcUrl, 'sa', '', 'org.hsqldb.jdbcDriver')

            inf = new DataInputStream( new FileInputStream( infile ) )
            importGenres sql, inf
            importActors sql, inf
            importMovieGenres sql, inf
            importMovieActors sql, inf
            importMovies sql, inf

        } finally {
            if( inf ) inf.close()
        }
    }

    static private def importMovies( sql, inf ){
        def count = inf.readInt()

        def movies = []
        count.times {
            def mov = [:]
            mov.id = inf.readLong()
            mov.title = inf.readUTF()
            mov.version = inf.readLong()
            mov.description = inf.readUTF()
            mov.release_year = inf.readInt()
            mov.storage_name = inf.readUTF()
            mov.storage_index = inf.readInt()
            mov.date_created = inf.readLong()
            mov.last_update = inf.readLong()

            byte[] bytes = new byte[inf.readInt()]
            inf.read(bytes)
            mov.poster = bytes
            
            movies << mov
        }

        new File('/home/cjstehno/Desktop/poster.jpg').withOutputStream { os->
            movies[0].poster.each { b->
                os.write( b as byte )
            }
        }

        println "Loaded ${movies.size()} movies."
    }

    static private def importMovieGenres( sql, inf ){
        def count = inf.readInt()

        def movie_genres = []
        count.times {
            def mg = [:]
            mg.movie_id = inf.readLong()
            mg.genre_id = inf.readLong()
            movie_genres << mg
        }

        println "Loaded ${movie_genres.size()} movie genres..."
    }

    static private def importMovieActors( sql, inf ){
        def count = inf.readInt()

        def movie_actors = []
        count.times {
            def ma = [:]
            ma.movie_id = inf.readLong()
            ma.actor_id = inf.readLong()
            movie_actors << ma
        }

        println "Loaded ${movie_actors.size()} movie actors..."
    }

    static private def importActors( sql, inf ){
        def count = inf.readInt()

        def actors = []
        count.times {
            def actor = [:]
            actor.id = inf.readLong()
            actor.version = inf.readLong()
            actor.first_name = inf.readUTF()
            actor.middle_name = inf.readUTF()
            actor.last_name = inf.readUTF()
            actors << actor
        }

        println "Loaded ${actors.size()} actors..."
    }

    static private def importGenres( sql, inf ){
        def count = inf.readInt()

        def genres = []
        count.times {
            def genre = [:]
            genre.id = inf.readLong()
            genre.version = inf.readLong()
            genre.name = inf.readUTF()
            genres << genre
        }

        println "Loaded ${genres.size()} genres..."
    }
}

