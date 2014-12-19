package org.yukung.sandbox.jooq

import groovy.sql.Sql
import org.jooq.DSLContext
import org.jooq.RecordHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import org.yukung.sandbox.db.jooq.gen.tables.pojos.Book
import spock.lang.Specification

import static org.yukung.sandbox.db.jooq.gen.tables.Author.AUTHOR
import static org.yukung.sandbox.db.jooq.gen.tables.Book.BOOK

@ContextConfiguration(loader = SpringApplicationContextLoader, classes = JooqApplication)
class ReadSpec extends Specification {

    @Autowired
    DSLContext dsl;

    def "Groovy SQL"() {
        def sql
        setup:
        sql = Sql.newInstance("jdbc:h2:file:~/src/github.com/yukung/jooq-sample/jooq-sample-db/target/jooqdb;DB_CLOSE_DELAY=-1", "sa", "", 'org.h2.Driver')
        expect:
        sql.eachRow("""
        select isbn, title, author_id, publish_date
        from book
        where isbn = '001-8888880001'
        """, {
            println "${it.isbn}:${it.title}:${it.author_id}:${it.publish_date}"
        })
    }

    def "select1"() {
        def b = BOOK.as("b")
        println "<-------------------------------------->"
        expect:
        def books = dsl
                .select(BOOK.ISBN, BOOK.TITLE, BOOK.AUTHOR_ID, BOOK.PUBLISH_DATE)
                .from(BOOK)
                .fetchInto({
            r ->
                println(
                        "${r.getValue(b.ISBN)}: " +
                                "${r.getValue(b.TITLE)}: " +
                                "${r.getValue(b.AUTHOR_ID)}: " +
                                "${r.getValue(b.PUBLISH_DATE)}"
                )
        } as RecordHandler)
    }

    def "select2"() {
        println "<-------------------------------------->"
        when:
        def books = dsl
                .selectFrom(BOOK)
                .orderBy(BOOK.ISBN.asc())
                .limit(3)
                .offset(0)
                .fetch().into(Book.class)
        then:
        books != null
        books.size() == 3
        books.each {
            println it.dump()
        }

    }

    def "select3"() {
        println "<-------------------------------------->"
        expect:
        dsl.select()
                .from(BOOK)
                .join(AUTHOR).on(AUTHOR.ID.eq(BOOK.AUTHOR_ID))
                .fetchMaps()
                .each {
            it.each { e ->
                println "${e.key} : ${e.value}"
            }
        }
    }
}
