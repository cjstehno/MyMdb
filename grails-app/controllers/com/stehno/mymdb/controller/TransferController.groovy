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

import com.stehno.mymdb.service.ExportService
import com.stehno.mymdb.service.ImportService
import grails.converters.JSON

/**
 * This controller handles the import and export functions.
 *
 * @author cjstehno
 */
class TransferController {

    ExportService exportService
    ImportService importService

    def exportCollection = {
        response.addHeader('Content-Disposition','attachment; filename=mymdb_export.bin')
        exportService.exportCollection( response.outputStream )
    }

    def importCollection = {
        def confirm = params.confirm

        def data = [:]
        if( confirm ){
            def file = params.file
            importService.importCollection file.inputStream
            
        } else {
            data.success = false
            data.errors = ['confirm':'You must confirm collection replacement.']
        }

        render( contentType:'text/html', text:(data as JSON).toString(false) )
    }
}
