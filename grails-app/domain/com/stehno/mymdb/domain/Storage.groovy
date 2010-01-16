package com.stehno.mymdb.domain

class Storage {

	String name
	int index
	
	static constraints = {
		index(unique:'name',range:1..120)
	}
}
