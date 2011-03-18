/*
 * Copyright (c) 2011 Christopher J. Stehno (chris@stehno.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stehno.mymdb.service

import com.stehno.mymdb.ServiceValidationException
import com.stehno.mymdb.domain.*
import com.stehno.mymdb.dto.*

class MovieFlowService {

    static scope = "session" // TODO: not entirely happy with this
    static transactional = true

    StorageUnitService storageUnitService

    private final flow = [:]

    /**
     * Starts the flow and cleans out any pre-existing data. IF a movieId
     * is specified, it will be stored and the flow will be in "edit" mode.
     *
     * @param movieId id of movie being edited (null for new movie)
     */
    def start( movieId = null ){
        flow.clear()
        if(movieId) populateFlowFromMovie( movieId as Long )
    }

    /**
     * Retrieve the stored DTO, or an empty new instance if none exists for that
     * type. IF a new DTO instance is created, it will not be stored automatically.
     *
     * @param dto the DTO type to be retrieved, should be a class instance
     * @return either the existing populated reference or a new instance of the dto
     */
    def retrieve( Class dto ){
        flow[dto.name] ?: dto.newInstance()
    }

    /**
     * Stores the given DTO in the flow, using its classname as its key. Any
     * pre-existing data for the DTO will be over-written.
     *
     * @param dto the DTO to be stored
     * @return the DTO object will be returned (as a convenience)
     */
    def store( dto ){
        flow[dto.getClass().name] = dto
    }

    /**
     * Copies the movie data into the appropriate DTO objects for use
     * in the flow
     *
     * @param movie the movie being edited
     */
    private def void populateFlowFromMovie( movieId ){
        def movie = Movie.get(movieId)

        flow.movieId = movieId
        
        store(new DetailsDto(
            title:movie.title,
            releaseYear:movie.releaseYear,
            storageId:"${movie.storage?.storageUnit.id}${movie.storage?.index ? ':'+movie.storage?.index : ''}",
            description:movie.description,
            mpaaRating:movie.mpaaRating,
            runtime:movie.runtime,
            format:movie.format
        ))

        if(movie.poster){
            store(new PosterDto(
                posterType:PosterType.EXISTING,
                posterId:movie.poster.id,
                posterName:movie.poster.title
            ))
        } else {
            store(new PosterDto( posterType:PosterType.NONE ))
        }

        if(movie.genres){
            store(new GenreDto(
                genres:movie.genres.collect { it.id }
            ))
        }

        if(movie.actors){
            store(new ActorDto(
                actors:movie.actors.collect { it.id }
            ))
        }

        if(movie.sites){
            def dto = new WebSiteDto()
            movie.sites.each { site ->
                dto.sites[site.label] = site.url
            }
            store(dto)
        }
    }

    /**
     * Uses the given data to populate the movie flow DTOs.
     * 
     * @param movieData
     */
    def void populate( movieData ){
        store(new DetailsDto(
            title:movieData.title,
            releaseYear:movieData.releaseYear,
            description:movieData.description,
            mpaaRating:movieData.rating,
            runtime:movieData.runtime,
            format:Format.UNKNOWN
        ))

        if(movieData.posterUrl){
            store(new PosterDto(
                posterType:PosterType.URL,
                url:movieData.posterUrl,
                posterName:movieData.title
            ))
        } else {
            store(new PosterDto( posterType:PosterType.NONE ))
        }

        if(movieData.genreNames){
            store(new GenreDto(
                genres:movieData.genreNames.collect { genreName->
                    def gen = Genre.findByName(genreName)
                    if(gen){
                        return gen.id
                    } else {
                        // TODO: would like to make creation conditional based on save of movie
                        gen = new Genre(name:genreName)
                        gen.save(flush:true)
                        return gen.id
                    }
                }
            ))
        }

        if(movieData.actorNames){
            store(new ActorDto(
                actors:movieData.actorNames.collect { actorName->
                    def aname = parseName(actorName as String)
                    def act = Actor.findWhere( firstName:aname.first, middleName:aname.middle, lastName:aname.last )
                    if(act){
                        return act.id
                    } else {
                        // TODO: would like to make creation conditional based on save of movie
                        act = new Actor( firstName:aname.first, middleName:aname.middle, lastName:aname.last )
                        act.save(flush:true)
                        return act.id
                    }
                }
            ))
        }

        if(movieData.sites){
            store(new WebSiteDto( sites:movieData.sites ))
        }
    }

    private def parseName( name ){
        def parts = name.split(' ')

        [
            first:(parts.size() > 1 ? parts[0] : ''),
            middle:(parts.size() > 2 ? parts[1] : ''),
            last:parts[ parts.size() == 1 ? 0 : (parts.size()-1) ]
        ]
    }

    /**
     * Saves or updates the movie current stored in the flow data.
     *
     * @throws ServiceValidationException if there are validation errors
     */
    def void save(){
        // TODO: need to account for pessimistic locking version

        def movie = flow.movieId ? Movie.get(flow.movieId) : new Movie()

        def details = retrieve(DetailsDto.class)
        movie.title = details.title
        movie.releaseYear = details.releaseYear
        movie.description = details.description
        movie.mpaaRating = details.mpaaRating
        movie.runtime = details.runtime
        movie.format = details.format

        // set poster
        def poster = retrieve(PosterDto.class)
        if( (poster.posterType == PosterType.URL || poster.posterType == PosterType.FILE) && poster.file ){
            // the content is already in the dto
            def thePoster = new Poster( title:details.title, content:poster.file )
            if( !thePoster.save(flush:true) ) {
                // since its been validated along the way this should rarely happen
                throw new ServiceValidationException('Unable to persist poster.', thePoster.errors)
            } else {
                movie.poster = thePoster
            }

        } else if(poster.posterType == PosterType.EXISTING){
            movie.poster = Poster.get(poster.posterId)

        }

        // genres
        movie.genres?.clear()
        def selectedGenreIds = retrieve(GenreDto.class).genres ?: []
        selectedGenreIds.each { gid->
            movie.addToGenres Genre.get(gid)
        }

        movie.actors?.clear()
        def selectedActorIds = retrieve(ActorDto.class).actors ?: []
        selectedActorIds.each { aid->
            movie.addToActors Actor.get(aid)
        }

        movie.sites?.clear()
        def selectedSites = retrieve(WebSiteDto.class).sites ?: []
        selectedSites.each { lbl,url->
            def ws = new WebSite( label:lbl, url:url )
            ws.save(flush:true)

            movie.addToSites ws
        }

        movie.save(flush:true)

        def storUnit
        def storIdx
        if( details.storageId.contains(':') ){
            ( storUnit, storIdx ) = details.storageId.split(':')
        } else {
            storUnit = details.storageId
        }

        storageUnitService.storeMovie( storUnit as Long, movie.id, storIdx as Integer)

        if( movie.hasErrors() ){
            // since its been validated along the way this should rarely happen
            throw new ServiceValidationException('Unable to persist movie.', movie.errors)
        }
    }
}
