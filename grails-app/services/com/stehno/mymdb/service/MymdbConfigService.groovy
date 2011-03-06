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

/**
 * Local fascade for accessing persistent configuration properties.
 *
 * @author cjstehno
 */
class MymdbConfigService {

    def configPropertyService

    private static final String API_KEY_CONFIG = 'tmdb.ApiKey'

    /**
     * Retrieves the TMDB API Key from the persistent configuration properties.
     * 
     * @return
     */
    String getTmdbApiKey(){
        configPropertyService.getProperty(API_KEY_CONFIG)
    }
}
