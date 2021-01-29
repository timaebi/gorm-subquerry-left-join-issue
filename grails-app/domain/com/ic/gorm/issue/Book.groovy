package com.ic.gorm.issue

class Book {
    String name

    static belongsTo = [author: Author]

    static constraints = {
    }
}
