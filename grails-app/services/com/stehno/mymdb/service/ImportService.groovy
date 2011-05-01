package com.stehno.mymdb.service

import com.stehno.mymdb.domain.*

/**
 * Used to import data into the database. This is meant to be used for backup
 * recovery or server relocation. All content data (not security) will be
 * deleted before the import is performed. 
 */
class ImportService {

	static transactional = true

	void importCollection( InputStream inputStream ){
        if(log.isInfoEnabled()) log.info 'Importing collection...'

        clearDatabase()

        def data = [:]

        new DataInputStream(inputStream).withStream { ins->
            ins.readByte()  // version - not used currently

            genres( ins, data )
            actors( ins, data )
            storageUnits( ins, data )
            posters( ins, data )
            webSites( ins, data )
            movies( ins, data )
        }

        if(log.isInfoEnabled()) log.info 'Collection imported.'
    }

    private void genres( DataInputStream ins, data ){
        def count = ins.readInt()

        if(log.isInfoEnabled()) log.info "--> Importing $count genres..."

        data.genres = [:]

        count.times {
            def id = ins.readLong()
            def name = ins.readUTF()

            def genre = new Genre( id:id, name:name )
            genre.save(flush:true)

            data.genres[id] = genre.id 
        }
    }

    private void actors( DataInputStream ins, data ){
        def count = ins.readInt()

        if(log.isInfoEnabled()) log.info "--> Importing $count actors..."

        data.actors = [:]

        count.times {
            def id = ins.readLong()
            def first = ins.readUTF()
            def middle = ins.readUTF()
            def last = ins.readUTF()

            def actor = new Actor( firstName:first, middleName:middle, lastName:last )
            actor.save(flush:true)
            
            data.actors[id] =  actor.id
        }
    }

    private void storageUnits( DataInputStream ins, data ){
        def count = ins.readInt()

        if(log.isInfoEnabled()) log.info "--> Importing $count storage units..."

        data.storageUnits = [:]

        count.times {
            def id = ins.readLong()
            def name = ins.readUTF()
            def indexed = ins.readBoolean()
            def capacity = ins.readInt()

            def unit = new StorageUnit( name:name, indexed:indexed, capacity:capacity )
            unit.save(flush:true)

            data.storageUnits[id] = unit.id
        }
    }

    private void posters( DataInputStream ins, data ){
        def count = ins.readInt()

        if(log.isInfoEnabled()) log.info "--> Importing $count posters..."

        data.posters = [:]

        count.times {
            def id = ins.readLong()
            def title = ins.readUTF()

            def size = ins.readInt()
            def content = new byte[size]
            ins.readFully(content)

            def poster = new Poster( title:title, content:content )
            poster.save(flush:true)

            data.posters[id] = poster.id
        }
    }

    private void webSites( DataInputStream ins, data ){
        def count = ins.readInt()

        if(log.isInfoEnabled()) log.info "--> Importing $count sites..."

        data.webSites = [:]
        
        count.times {
            def id = ins.readLong()
            def label = ins.readUTF()
            def url = ins.readUTF()

            def site = new WebSite( label:label, url:url)
            site.save(flush:true)

            data.webSites[id] = site.id
        }
    }

    private void movies( DataInputStream ins, data ){
        def count = ins.readInt()

        if(log.isInfoEnabled()) log.info "--> Importing $count movies..."

        count.times {
            def id = ins.readLong()
            def title = ins.readUTF()
            def description = ins.readUTF()
            def releaseYear = readInt(ins, 1900)
            def runtime = ins.readInt()
            def rating = MpaaRating.values()[ins.readInt()]
            def format = Format.values()[ins.readInt()]
            def broadcast = Broadcast.values()[ins.readInt()]
            def created = date(ins.readLong())
            def updated = date(ins.readLong())
            def posterId = readLong(ins, null)
            def storageIndex = readInt(ins, null)
            def storageUnitId = readLong(ins, null)
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

            if( storageUnitId ){
                // storage
                def storageUnit = StorageUnit.get(data.storageUnits[storageUnitId])

                def storage = new Storage( storageUnit:storageUnit, index:storageIndex )
                storageUnit.addToSlots storage
                storageUnit.save(flush:true)

                movie.storage = storage
                movie.save(flush:true)
            }
        }
    }

    private readInt( DataInputStream ins, Integer defaultVal ){
        ins.readInt() ?: defaultVal
    }

    private readLong( DataInputStream ins, Long defaultVal ){
        ins.readLong() ?: defaultVal
    }

    private date( val ){
        val ? new Date(val) : null
    }

    private items( DataInputStream ins ){
        def ids = []
        def count = ins.readInt()
        count.times {
            ids << ins.readLong()
        }
        return ids
    }

    /**
     * Another warning... this deletes EVERYTHING except the security data.
     */
    private void clearDatabase(){
        Movie.list().each deleteAll
        Genre.list().each deleteAll
        Actor.list().each deleteAll
        StorageUnit.list().each deleteAll
        Poster.list().each deleteAll
        WebSite.list().each deleteAll
    }

    private deleteAll = { x-> x.delete(flush:true) }    
}