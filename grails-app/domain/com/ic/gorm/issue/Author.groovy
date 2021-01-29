package com.ic.gorm.issue

class Author {
    String name

    static hasMany = [books: Book]

    static constraints = {
    }
}
