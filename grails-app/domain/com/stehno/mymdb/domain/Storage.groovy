package com.stehno.mymdb.domain

class Storage {

	String name
	int index
	
	static constraints = {
		index(range:1..120)
	}
}
