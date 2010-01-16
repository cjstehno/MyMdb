package com.stehno.mymdb.domain

class Poster {

	String name
	byte[] data

    static constraints = {
		name(unique:true,size:1..40)
    }
}
