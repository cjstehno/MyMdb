package com.stehno.mymdb.service

import com.stehno.mymdb.domain.Actor
import com.stehno.mymdb.domain.Genre
import com.stehno.mymdb.domain.StorageUnit

class ImportService {

	static transactional = true
	
	void importCollection( InputStream inputStream ){
        def data = [:]

        new DataInputStream(inputStream).withStream { ins->
            ins.readByte()  // version - not used currently

            data.genres = genres( ins )
            data.actors = actors( ins )
            data.storageUnits = storageUnits( ins )
        }

        data.genres.each { kv->
            def genre = Genre.findByName( kv.value.name )
            if( !genre ){
                genre = new Genre( name:kv.value.name )
                genre.save(flush:true)
            }
            kv.value = genre
        }

        data.actors.each { kv->
            def actor = Actor.findWhere( firstName:kv.value.firstName, middleName:kv.value.middleName, lastName:kv.value.lastName )
            if( !actor ){
                actor = new Actor( firstName:kv.value.firstName, middleName:kv.value.middleName, lastName:kv.value.lastName )
                actor.save(flush:true)
            }
            kv.value = actor
        }

        data.storageUnits.each { kv->
            def unit = StorageUnit.findByName( kv.value.name )
            if( !unit ){
                unit = new StorageUnit( name:kv.value.name, indexed:kv.value.indexed, capacity:kv.value.capacity )
                unit.save(flush:true)
            }
            kv.value = unit
        }
    }

    private genres( DataInputStream ins ){
        def genres = [:]

        ins.readInt().times {
            def id = ins.readLong()
            def name = ins.readUTF()
            genres[id] = [ id:id, name:name ]
        }

        return genres
    }

    private actors( DataInputStream ins ){
        def actors = [:]

        ins.readInt().times {
            def id = ins.readLong()
            def first = ins.readUTF()
            def middle = ins.readUTF()
            def last = ins.readUTF()
            actors[id] =  [ id:id, firstName:first, middleName:middle, lastName:last ]
        }

        return actors
    }

    private storageUnits( DataInputStream ins ){
        def units = [:]

        ins.readInt().times {
            def id = ins.readLong()
            def name = ins.readUTF()
            def indexed = ins.readBoolean()
            def capacity = ins.readInt()
            units[id] = [ id:id, name:name, indexed:indexed, capacity:capacity ]
        }

        return units
    }
}