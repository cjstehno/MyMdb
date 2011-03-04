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
package com.stehno.mymdb.domain

class Actor {

    String firstName
    String middleName
    String lastName

    static belongsTo = Movie
    static hasMany = [movies:Movie]
    
    static constraints = {
        firstName( nullable:true, blank:true, size:1..25 )
        middleName( nullable:true, blank:true, size:1..25 )
        lastName( blank:false, size:1..25 )
    }
	
    static transients = ['displayName', 'fullName']

    def getDisplayName(){
        def dn = lastName
        if( firstName || middleName ){
            dn += ", $firstName${firstName && middleName ? ' ' : ''}$middleName"
        }
        return dn
    }

    def getFullName(){
        "${firstName ? firstName + ' ' : ''}${middleName ? middleName + ' ' : ''}$lastName"
    }
}
