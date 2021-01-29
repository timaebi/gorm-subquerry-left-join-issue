package com.ic.gorm.issue


import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import spock.lang.Specification

import javax.persistence.criteria.JoinType

@Integration
@Rollback
class JoinIssueSpec extends Specification {

    def setup() {
        if (Author.count() == 0) {
            List<Author> authors = [
                    new Author(name: 'Alfred'),
                    new Author(name: 'Buff'),
            ]
            authors[1].addToBooks(new Book(name: "Buff's Autobiography"))
            authors[1].addToBooks(new Book(name: "Buff's Fantasy"))
            authors*.save(failOnError: true)
        }
    }

    def cleanup() {
    }

    void "test left join on DetachedCriteria"() {
        when:
        List<Author> authors = Author.where {
            join('books', JoinType.LEFT)
            books {
                or {
                    isNull('id')
                    ilike('name', '%biography%')
                }
            }
        }.list()

        then: "left join is used in criteria"
        authors.size() == 2
    }

    void "test left join in sub query"() {
        when:
        List<Author> authors = Author.where {
            'in'('id', Author.where {
                join('books', JoinType.LEFT)
                books {
                    or {
                        isNull('id')
                        ilike('name', '%biography%')
                    }
                }
            }.id())
        }.list()

        then: "left join on sub criteria should be used"
        authors.size() == 2
    }
}
