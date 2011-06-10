package com.stehno.mymdb.service

import com.stehno.mymdb.transfer.BinaryImporter
import com.stehno.mymdb.transfer.Importer
import com.stehno.mymdb.domain.*

/**
 * Used to import data into the database. This is meant to be used for backup
 * recovery or server relocation. All content data (not security) will be
 * deleted before the import is performed. 
 */
class ImportService {

	static transactional = true

    @Delegate private Importer<? extends InputStream> importer = new BinaryImporter()

	void importCollection( InputStream inputStream ) throws IOException {
        if(log.isInfoEnabled()) log.info 'Importing collection...'

        clearDatabase()

        def data = [:]

        new DataInputStream(inputStream).withStream { ins->
            readByte ins  // version - not used currently, but still needs to be consumed

            genres( ins, data )
            actors( ins, data )
            storageUnits( ins, data )
            posters( ins, data )
            webSites( ins, data )
            movies( ins, data )
        }

        if(log.isInfoEnabled()) log.info 'Collection imported.'
    }

    private void genres( InputStream ins, data ){
        def count = readInt(ins,0)

        if(log.isInfoEnabled()) log.info "--> Importing $count genres..."

        data.genres = [:]

        count.times {
            def id = readLong(ins,0)
            def name = readUTF(ins)

            def genre = new Genre( id:id, name:name )
            genre.save(flush:true)

            data.genres[id] = genre.id 
        }
    }

    private void actors( InputStream ins, data ){
        def count = readInt(ins,0)

        if(log.isInfoEnabled()) log.info "--> Importing $count actors..."

        data.actors = [:]

        count.times {
            def id = readLong(ins,0)
            def first = readUTF(ins)
            def middle = readUTF(ins)
            def last = readUTF(ins)

            def actor = new Actor( firstName:first, middleName:middle, lastName:last )
            actor.save(flush:true)
            
            data.actors[id] =  actor.id
        }
    }

    private void storageUnits( InputStream ins, data ){
        def count = readInt(ins,0)

        if(log.isInfoEnabled()) log.info "--> Importing $count storage units..."

        data.storageUnits = [:]

        count.times {
            def id = readLong(ins,0)
            def name = readUTF(ins)
            def indexed = readBoolean(ins)
            def capacity = readInt(ins,0)

            def unit = new StorageUnit( name:name, indexed:indexed, capacity:capacity )
            unit.save(flush:true)

            data.storageUnits[id] = unit.id

            def slotCount = readInt(ins,0)
            data.movieStorage = [:]

            if(log.isInfoEnabled()) log.info "-----> Importing $slotCount storage unit slots..."

            slotCount.times { s->
                def slotIndex = readInt(ins, null)
                def movieCount = readInt(ins,0)

                if(log.isInfoEnabled()) log.info "--------> Importing $movieCount movie references in slot..."

                movieCount.times { m->
                    def movieId = readLong(ins,0)
                    data.movieStorage[movieId] = [ unit:unit, index:slotIndex ]
                }
            }
        }
    }

    private void posters( InputStream ins, data ){
        def count = readInt(ins,0)

        if(log.isInfoEnabled()) log.info "--> Importing $count posters..."

        data.posters = [:]

        count.times {
            def id = readLong(ins,0)
            def title = readUTF(ins)

            def size = readInt(ins,0)
            def content = readBytes(ins, size)

            def poster = new Poster( title:title, content:content )
            poster.save(flush:true)

            data.posters[id] = poster.id
        }
    }

    private void webSites( InputStream ins, data ){
        def count = readInt(ins,0)

        if(log.isInfoEnabled()) log.info "--> Importing $count sites..."

        data.webSites = [:]
        
        count.times {
            def id = readLong(ins,0)
            def label = readUTF(ins)
            def url = readUTF(ins)

            def site = new WebSite( label:label, url:url)
            site.save(flush:true)

            data.webSites[id] = site.id
        }
    }

    private void movies( InputStream ins, data ){
        def count = readInt(ins,0)

        if(log.isInfoEnabled()) log.info "--> Importing $count movies..."

        count.times {
            def id = readLong(ins,0)
            def title = readUTF(ins)
            def description = readUTF(ins)
            def releaseYear = readInt(ins, 1900)
            def runtime = readInt(ins,0)
            def rating = MpaaRating.values()[readInt(ins,0)]
            def format = Format.values()[readInt(ins,0)]
            def broadcast = Broadcast.values()[readInt(ins,0)]
            def created = date(readLong(ins,0))
            def updated = date(readLong(ins,0))
            def posterId = readLong(ins, null)
            def genreIds = items( ins )
            def actorIds = items( ins )
            def siteIds = items( ins )

            // create movie
            def movie = new Movie()
            movie.title = title
            movie.description = description
            movie.releaseYear = releaseYear
            movie.runtime = runtime
            movie.mpaaRating = rating
            movie.format = format
            movie.broadcast = broadcast
            movie.dateCreated = created
            movie.lastUpdate = updated

            if(posterId){
                movie.poster = Poster.get(data.posters[posterId])
            }

            genreIds?.each { i->
                movie.addToGenres Genre.get(data.genres[i])
            }

            actorIds?.each { i->
                movie.addToActors Actor.get(data.actors[i])
            }

            siteIds?.each { i->
                movie.addToSites WebSite.get(data.webSites[i])
            }

            movie.save(flush:true)

            // setup the storage
            def storage = data.movieStorage[id]
            if( storage ){
                StorageUnit storageUnit = storage.unit

                def storageSlot = new Storage()
                if(storage.index) storageSlot.index = storage.index

                storageUnit.addToSlots storageSlot

                storageSlot.addToMovies movie

                storageUnit.save(flush:true)
            }
        }
    }

    private date( val ){
        val ? new Date(val) : null
    }

    private items( InputStream ins ){
        def ids = []
        def count = readInt(ins,0)
        count.times {
            ids << readLong(ins,0)
        }
        return ids
    }

    /**
     * Another warning... this deletes EVERYTHING except the security data.
     */
    private void clearDatabase(){
        Storage.list().each { storage->
            storage.movies?.clear()
        }

        StorageUnit.list().each deleteAll
        Movie.list().each deleteAll
        Genre.list().each deleteAll
        Actor.list().each deleteAll
        Poster.list().each deleteAll
        WebSite.list().each deleteAll
    }

    private deleteAll = { x-> x.delete(flush:true) }    
}