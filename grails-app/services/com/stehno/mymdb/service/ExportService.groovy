package com.stehno.mymdb.service

import com.stehno.mymdb.domain.Genre
import com.stehno.mymdb.domain.Actor
import com.stehno.mymdb.domain.StorageUnit

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

        out.writeInt units.size()

        units.each { unit->
            out.writeLong unit.id
            writeUTF out, unit.name
            out.writeBoolean unit.indexed
            out.writeInt unit.capacity
        }
    }

    private void writeUTF( DataOutputStream out, String str ){
        out.writeUTF( str ?: '' )
    }
}
