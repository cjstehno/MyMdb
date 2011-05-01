package com.stehno.mymdb.service

import com.stehno.mymdb.domain.*

class ExportService {

    static transactional = false

    private static final byte FORMAT_VERSION = 1

    /**
     * Exports all collection data, basically everything except security data to the given OutputSteam.
     * 
     * @param outputStream
     */
    void exportCollection( OutputStream outputStream ){
        if(log.isInfoEnabled()) log.info 'Exporting collection...'

        new DataOutputStream(outputStream).withStream { out->
            out.writeByte FORMAT_VERSION

            genres out
            actors out
            storageUnits out
            posters out
            webSites out
            movies out
        }

        if(log.isInfoEnabled()) log.info 'Done exporting collection.'
    }

    private void genres( DataOutputStream out ){
        def genreList = Genre.list()
        def count = genreList.size()

        if(log.isInfoEnabled()) log.info "--> Exporting $count genres..."

        out.writeInt count

        genreList.each { genre->
            out.writeLong genre.id
            writeUTF out, genre.name
        }
    }

    private void actors( DataOutputStream out ){
        def actorList = Actor.list()
        def count = actorList.size()

        if(log.isInfoEnabled()) log.info "--> Exporting $count actors..."

        out.writeInt count

        actorList.each { actor->
            out.writeLong actor.id
            writeUTF out, actor.firstName
            writeUTF out, actor.middleName
            writeUTF out, actor.lastName
        }
    }

    private void storageUnits( DataOutputStream out ){
        def units = StorageUnit.list()
        def count = units.size()

        if(log.isInfoEnabled()) log.info "--> Exporting $count storage units..."

        out.writeInt count

        units.each { unit->
            out.writeLong unit.id
            writeUTF out, unit.name
            out.writeBoolean unit.indexed
            out.writeInt unit.capacity
        }
    }

    private void webSites( DataOutputStream out ){
        def sites = WebSite.list()
        def count = sites.size()

        if(log.isInfoEnabled()) log.info "--> Exporting $count web sites..."

        out.writeInt count

        sites.each { site->
            out.writeLong site.id
            out.writeUTF site.label
            out.writeUTF site.url
        }
    }

    private void posters( DataOutputStream out ){
        def posters = Poster.list()
        def count = posters.size()

        if(log.isInfoEnabled()) log.info "--> Exporting $count posters..."

        out.writeInt count

        posters.each { poster->
            out.writeLong poster.id
            writeUTF out, poster.title
            out.writeInt poster.content.size()
            out.write( poster.content, 0, poster.content.size() )
        }
    }

    private void movies( DataOutputStream out ){
        def movies = Movie.list()
        def count = movies.size()

        if(log.isInfoEnabled()) log.info "--> Exporting $count movies..."

        out.writeInt count

        movies.each { movie->
            out.writeLong movie.id
            writeUTF out, movie.title
            writeUTF out, movie.description
            writeInt out, movie.releaseYear
            writeInt out, movie.runtime

            out.writeInt movie.mpaaRating.ordinal()
            out.writeInt movie.format.ordinal()
            out.writeInt movie.broadcast.ordinal()

            writeLong out, movie.dateCreated?.getTime()
            writeLong out, movie.lastUpdate?.getTime()

            writeLong out, movie.poster?.id

            writeInt out, movie.storage?.index
            writeLong out, movie.storage?.storageUnit?.id

            writeItems out, movie.genres
            writeItems out, movie.actors
            writeItems out, movie.sites
        }
    }

    private void writeItems( DataOutputStream out, items ){
        def count = items?.size() ?: 0

        out.writeInt count

        items.each {
            out.writeLong it.id
        }
    }

    private void writeUTF( DataOutputStream out, String str ){
        out.writeUTF( str ?: '' )
    }

    private void writeInt( DataOutputStream out, Integer i ){
        out.writeInt( i ?: 0 )
    }

    private void writeLong( DataOutputStream out, Long i ){
        out.writeLong( i ?: 0 )
    }
}
