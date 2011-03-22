package com.stehno.mymdb.domain
class MymdbRole {
    String name

    static hasMany = [ users: MymdbUser, permissions: String ]
    static belongsTo = MymdbUser

    static constraints = {
        name(nullable: false, blank: false, unique: true)
    }
}
