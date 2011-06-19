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

import com.stehno.mymdb.fetch.MovieDataProvider
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

/**
 * This service gathers movie data from the available MovieDataProviders and aggregates the results for the controller.
 *
 * Any bean with the MovieDataProvider interface will be registered.
 *
 * @author cjstehno
 */
class MovieFetchService implements InitializingBean, ApplicationContextAware {

    static transactional = false

    private ApplicationContext applicationContext
    private final Map<String,MovieDataProvider> providers = [:]

    def search( String title ){
        def results = []

        // FIXME: http://github.com/cjstehno/MyMdb/issues/76
        providers.each { name, provider->
            results.addAll( provider.searchFor( title ) )
        }

        return results.sort { it.providerId }
    }

    def fetch( String providerId, movieId ){
        def provider = providers[providerId]
        if( provider ){
            return provider.fetch(movieId)
        }
        throw new IllegalArgumentException('ProviderId not found!')
    }

    void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext
    }

    void afterPropertiesSet() {
        applicationContext.getBeansOfType(MovieDataProvider.class).each { beanName, provider->
            providers[provider.getProviderId()] = provider
        }
    }
}
