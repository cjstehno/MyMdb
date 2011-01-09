/*
Copyright 2010 Christopher J. Stehno (chris@stehno.com)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.stehno.mymdb.dto

/**
 * MovieManager wizard DTO for the poster panel. The posterType field value will specify which of the other properties should be specified.
 *
 * NONE - no value
 * FILE - file property (uploaded file data)
 * URL - url property (url of image to be copied), should populate file data
 * EXISTING - posterId of existing poster
 */
class PosterDto {

    PosterType posterType
    String url = 'http://'
    byte[] file
    long posterId
}

enum PosterType {
    NONE, FILE, EXISTING, URL
}

