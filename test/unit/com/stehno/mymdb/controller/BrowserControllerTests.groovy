package com.stehno.mymdb.controller

import com.stehno.mymdb.domain.Movie
import grails.test.ControllerUnitTestCase
import org.gmock.WithGMock
import org.junit.Test
import org.junit.Ignore

@WithGMock
class BrowserControllerTests extends ControllerUnitTestCase {

	@Test @Ignore // FIXME: fix with grails 1.4 refactoring
    void index() {	// just assert no errors
        controller.index()
    }

    @Test @Ignore // FIXME: fix with grails 1.4 refactoring
    void about() {	// just assert no errors
        controller.about()
    }

	@Test @Ignore // FIXME: fix with grails 1.4 refactoring
    void details(){
        def movie = new Movie(id:123,title:'Foozilla')
        mockDomain Movie, [movie]

        controller.params.mid = 123

        def resp = controller.details()

        assertEquals movie, resp.movieInstance
    }
}