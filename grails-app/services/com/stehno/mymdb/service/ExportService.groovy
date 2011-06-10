package com.stehno.mymdb.service

import com.stehno.mymdb.transfer.Exporter
import com.stehno.mymdb.domain.*
import com.stehno.mymdb.transfer.BinaryExporter

class ExportService {

    static transactional = false

    private static final byte FORMAT_VERSION = (byte)1
    @Delegate private Exporter<? extends OutputStream> exporter = new BinaryExporter()

    /**
     * Exports all collection data, basically everything except security data to the given OutputSteam.
     * 
     * @param outputStream
     */
    void exportCollection( OutputStream outputStream ){
        if(log.isInfoEnabled()) log.info 'Exporting collection...'

        new DataOutputStream(outputStream).withStream { out->
            writeByte out, FORMAT_VERSION

            genres out
            actors out
            storageUnits out
            posters out
            webSites out
            movies out
        }

        if(log.isInfoEnabled()) log.info 'Done exporting collection.'
    }

    private void genres( OutputStream out ){
        def genreList = Genre.list()
        def count = genreList.size()

        if(log.isInfoEnabled()) log.info "--> Exporting $count genres..."

        writeInt out, count

        genreList.each { genre->
            writeLong out, genre.id
            writeUTF out, genre.name
        }
    }

    private void actors( OutputStream out ){
        def actorList = Actor.list( order:'lastName' )
        def count = actorList.size()

        if(log.isInfoEnabled()) log.info "--> Exporting $count actors..."

        writeInt out, count

        actorList.each { actor->
            writeLong out, actor.id
            writeUTF out, actor.firstName
            writeUTF out, actor.middleName
            writeUTF out, actor.lastName
        }
    }

    private void storageUnits( OutputStream out ){
        def units = StorageUnit.list()
        def count = units.size()

        if(log.isInfoEnabled()) log.info "--> Exporting $count storage units..."

        writeInt out, count

        units.each { unit->
            writeLong out, unit.id
            writeUTF out, unit.name
            writeBoolean out, unit.indexed
            writeInt out, unit.capacity

            def slotCount = unit.slots ? unit.slots.size() : 0

            writeInt out, slotCount

            if(log.isInfoEnabled()) log.info "-----> Exporting $slotCount storage unit slots..."

            unit.slots?.each { slot->
                writeInt out, slot.index

                def slotMovieCount = slot.movies ? slot.movies.size() : 0

                writeInt out, slotMovieCount

                if(log.isInfoEnabled()) log.info "--------> Exporting $slotMovieCount movies references in slot..."

                slot.movies?.each { mov->
                    writeLong out, mov.id
                }
            }
        }
    }

    private void webSites( OutputStream out ){
        def sites = WebSite.list()
        def count = sites.size()

        if(log.isInfoEnabled()) log.info "--> Exporting $count web sites..."

        writeInt out, count

        sites.each { site->
            writeLong out, site.id
            writeUTF out, site.label
            writeUTF out, site.url
        }
    }

    private void posters( OutputStream out ){
        def posters = Poster.list()
        def count = posters.size()

        if(log.isInfoEnabled()) log.info "--> Exporting $count posters..."

        writeInt out, count

        posters.each { poster->
            writeLong out, poster.id
            writeUTF out, poster.title
            writeInt out, poster.content.size()
            writeBytes out, poster.content
        }
    }

    private void movies( OutputStream out ){
        def movies = Movie.list()
        def count = movies.size()

        if(log.isInfoEnabled()) log.info "--> Exporting $count movies..."

        writeInt out, count

        movies.each { movie->
            writeLong out, movie.id
            writeUTF out, movie.title
            writeUTF out, movie.description
            writeInt out, movie.releaseYear
            writeInt out, movie.runtime

            writeInt out, movie.mpaaRating.ordinal()
            writeInt out, movie.format.ordinal()
            writeInt out, movie.broadcast.ordinal()

            writeLong out, movie.dateCreated?.getTime()
            writeLong out, movie.lastUpdate?.getTime()

            writeLong out, movie.poster?.id

            writeItems out, movie.genres
            writeItems out, movie.actors
            writeItems out, movie.sites
        }
    }

    private void writeItems( OutputStream out, items ){
        def count = items?.size() ?: 0

        writeInt out, count

        items.each {
            writeLong out, it.id
        }
    }
}
