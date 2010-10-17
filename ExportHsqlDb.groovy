import groovy.sql.Sql

/*
 * This is an experimental tool to export the data from the older hsql database
 * to the new mysql database. This tool creates the export file that can be
 * imported using the import tool.
 */
class ExportMyMdb {

    static def main( args ){
        def jdbcUrl = 'jdbc:hsqldb:/home/cjstehno/hsql/mymdb/mymdb' //args[0]
        def outfile = new File('/home/cjstehno/Desktop/mymdb.dat' /*args[1]*/)

        def out
        try {
            def sql = Sql.newInstance(jdbcUrl, 'sa', '', 'org.hsqldb.jdbcDriver')

            out = new DataOutputStream( new FileOutputStream( outfile ) )
            exportGenres sql, out
            exportActors sql, out
            exportMovieGenres sql, out
            exportMovieActors sql, out
            exportMovies sql, out

        } finally {
            if( out ) out.close()
        }
    }

    static private def exportMovies( sql, out ){
        def res = sql.firstRow( 'select count(*) as cnt from movie' )
        out.writeInt( res.cnt as Integer )

        sql.eachRow 'select id,title,version,description,release_year,storage_name,storage_index,date_created,last_update,poster from movie',{
            out.writeLong( it.id as Long )
            out.writeUTF( it.title as String )
            out.writeLong( it.version as Long )
            out.writeUTF( it.description as String )
            out.writeInt( it.release_year as Integer )
            out.writeUTF( it.storage_name as String )
            out.writeInt( it.storage_index as Integer )
            out.writeLong( time(it.date_created) )
            out.writeLong( time(it.last_update) )
            out.writeInt( (it.poster as byte[]).size() )
            out.write( it.poster as byte[] )
        }
        out.flush()
    }

    static private def time( tstamp ){
        tstamp ? tstamp.getTime() : -1
    }

    static private def exportMovieGenres( sql, out ){
        def res = sql.firstRow( 'select count(*) as cnt from movie_genres' )
        out.writeInt( res.cnt as Integer )

        sql.eachRow 'select movie_id,genre_id from movie_genres', {
            out.writeLong( it.movie_id as Long )
            out.writeLong( it.genre_id as Long )
        }
        out.flush()
    }

    static private def exportMovieActors( sql, out ){
        def res = sql.firstRow( 'select count(*) as cnt from movie_actors' )
        out.writeInt( res.cnt as Integer )

        sql.eachRow 'select movie_id,actor_id from movie_actors', {
            out.writeLong( it.movie_id as Long )
            out.writeLong( it.actor_id as Long )
        }
        out.flush()
    }

    static private def exportActors( sql, out ){
        def res = sql.firstRow( 'select count(*) as cnt from actor' )
        out.writeInt( res.cnt as Integer )

        sql.eachRow 'select id,version,first_name,middle_name,last_name from actor', {
            out.writeLong( it.id as Long )
            out.writeLong( it.version as Long )
            out.writeUTF( it.first_name as String )
            out.writeUTF( it.middle_name as String )
            out.writeUTF( it.last_name as String )
        }
        out.flush()
    }

    static private def exportGenres( sql, out ){
        def res = sql.firstRow( 'select count(*) as cnt from genre' )
        out.writeInt( res.cnt as Integer )

        sql.eachRow 'select id,version,name from genre', {
            out.writeLong( it.id as Long )
            out.writeLong( it.version as Long )
            out.writeUTF( it.name as String )
        }
        out.flush()
    }
}

