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

package com.stehno.mymdb.controller

import grails.converters.JSON

/**
 * 
 *
 * @author cjstehno
 */
class StorageController {

    static allowedMethods = [ list:'GET' ]

    def list = {
        def data = [
            [ id:1001, name:'A', indexed:false, capacity:120, count:106 ]
        ]

        render( [ items:data ] as JSON )
    }
}
