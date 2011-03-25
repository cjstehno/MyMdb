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
package com.stehno.mymdb.domain

import org.junit.After
import org.junit.Before
import org.junit.Test
import grails.test.GrailsUnitTestCase
import com.stehno.mymdb.ValidationTestCategory

/**
 * 
 *
 * @author cjstehno
 */
@Mixin(ValidationTestCategory)
class MovieTests extends GrailsUnitTestCase {

    def movie

    @Before
    void before(){
        setUp()

        this.movie = movie(
            title:'Testing',
            releaseYear:2000,
            storage:new Storage(index:2),
            description:'Something interesting',
            mpaaRating:MpaaRating.UNKNOWN,
            format:Format.UNKNOWN,
            broadcast:Broadcast.UNKNOWN
        )
    }

	@Test
    void validation_valid() {
        assertValid movie
    }

    @Test
    void validation_title(){
        movie.title = str(101)
        assertInvalid movie, 'title', 'size.toobig'

        movie.title = ''
        assertInvalid movie, 'title', 'blank'

        movie.title = 'x'
        assertValid movie

        movie.title = null
        assertInvalid movie, 'title', 'nullable'
    }

    @Test
    void validation_description(){
        movie.description = null
        assertValid movie

        movie.description = ''
        assertValid movie

        movie.description = str(2001)
        assertInvalid movie, 'description', 'maxSize.exceeded'
    }

    @Test
    void validation_releaseYear(){
        movie.releaseYear = 1899
        assertInvalid movie, 'releaseYear', 'range.toosmall'

        movie.releaseYear = 2101
        assertInvalid movie, 'releaseYear', 'range.toobig'

        movie.releaseYear = null
        assertValid movie
    }

    @Test
    void validation_sites(){
        movie.sites = [ new WebSite( label:'Foo', url:'http://foo.com') ]
        assertValid movie
    }

    @Test
    void validation_storage(){
        movie.storage = null
        assertValid movie
    }

    @Test
    void validation_runtime(){
        movie.runtime = -1
        assertInvalid movie, 'runtime', 'min.notmet'

        movie.runtime = 0
        assertValid movie

        movie.runtime = 1231241
        assertValid movie
    }

    @Test
    void validation_rating(){
        movie.mpaaRating = null
        assertInvalid movie, 'mpaaRating', 'nullable'

        movie.mpaaRating = MpaaRating.PG
        assertValid movie
    }

    @Test
    void validation_format(){
        movie.format = null
        assertInvalid movie, 'format', 'nullable'

        movie.format = Format.DVD
        assertValid movie
    }

    @Test
    void ratings(){
        assertEquals MpaaRating.UNKNOWN, MpaaRating.fromLabel('Unknown')
        assertEquals MpaaRating.G, MpaaRating.fromLabel('G')
        assertEquals MpaaRating.PG, MpaaRating.fromLabel('PG')
        assertEquals MpaaRating.PG_13, MpaaRating.fromLabel('PG-13')
        assertEquals MpaaRating.R, MpaaRating.fromLabel('R')
        assertEquals MpaaRating.NC_17, MpaaRating.fromLabel('NC-17')
        assertEquals MpaaRating.UNRATED, MpaaRating.fromLabel('Unrated')
        assertEquals MpaaRating.UNKNOWN, MpaaRating.fromLabel('blah')
    }

    @Test
    void formats(){
        assertEquals Format.UNKNOWN, Format.fromLabel('Unknown')
        assertEquals Format.VCD, Format.fromLabel('VCD')
        assertEquals Format.DVD, Format.fromLabel('DVD')
        assertEquals Format.DVD_R, Format.fromLabel('DVD-R')
        assertEquals Format.BLURAY, Format.fromLabel('BluRay')
        assertEquals Format.UNKNOWN, Format.fromLabel('blah')
    }

    @Test
    void broadcasts(){
        assertEquals Broadcast.UNKNOWN, Broadcast.fromLabel('Unknown')
        assertEquals Broadcast.MOVIE, Broadcast.fromLabel('Movie')
        assertEquals Broadcast.TV_MOVIE, Broadcast.fromLabel('TV Movie')
        assertEquals Broadcast.TV_SERIES, Broadcast.fromLabel('TV Series')
        assertEquals Broadcast.TV_SPECIAL, Broadcast.fromLabel('TV Special')
        assertEquals Broadcast.OTHER, Broadcast.fromLabel('Other')
        assertEquals Broadcast.UNKNOWN, Broadcast.fromLabel('blah')
    }

    @After
    void after(){
        tearDown()
        
        this.movie = null
    }

	private def movie(params){
		def m = new Movie(params)
		mockForConstraintsTests Movie.class, [ m ]
		return m
	}
}
