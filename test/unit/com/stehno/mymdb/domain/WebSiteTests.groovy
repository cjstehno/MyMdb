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

import com.stehno.mymdb.ValidationTestCategory
import grails.test.GrailsUnitTestCase
import org.junit.Test

@Mixin(ValidationTestCategory)
class WebSiteTests extends GrailsUnitTestCase {

	@Test
    void validation_valid() {
		assertValid web( label:'IMDB', url:'http://imdb.com/somepath/to/a/movie' )
    }

	@Test
    void validation_label() {
        def url = 'http://imdb.com/somepath/to/a/movie'
		assertInvalid web( label:null, url:url ), 'label', 'nullable'
        assertInvalid web( label:'', url:url ), 'label', 'blank'
        assertInvalid web( label:'x', url:url ), 'label', 'size.toosmall'
        assertInvalid web( label:('x'*26), url:url ), 'label', 'size.toobig'
    }

	@Test
    void validation_url() {
        def label = 'IMDB'
		assertInvalid web( label:label, url:null ), 'url', 'nullable'
        assertInvalid web( label:label, url:'' ), 'url', 'blank'
        assertInvalid web( label:label, url:'imdb.com/somepath/to/a/movie' ), 'url', 'url.invalid'
        assertInvalid web( label:label, url:"http://foo.com/${str(90)}" ), 'url', 'size.toobig'
    }

	private WebSite web(params){
		def w = new WebSite(params)
		mockForConstraintsTests WebSite.class, [ w ]
		return w
	}
}
