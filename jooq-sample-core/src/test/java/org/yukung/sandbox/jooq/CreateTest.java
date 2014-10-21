package org.yukung.sandbox.jooq;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.yukung.sandbox.db.jooq.gen.tables.Book.*;

import java.sql.Date;

import org.joda.time.DateTime;
import org.jooq.DSLContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.yukung.sandbox.db.jooq.gen.tables.pojos.Book;
import org.yukung.sandbox.db.jooq.gen.tables.records.BookRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JooqApplication.class)
public class CreateTest {

    private static final Logger log = LoggerFactory.getLogger(CreateTest.class);

    @Autowired
    DSLContext dsl;

    @Test
    @Transactional
    public void insert1() {
        BookRecord book = dsl.newRecord(BOOK);
        book.setIsbn("999-9999999999");
        book.setAuthorId(4);
        book.setTitle("進撃の巨人 （１）");
        Date date = new java.sql.Date(new DateTime("2010-03-17").getMillis());
        book.setPublishDate(date);

        book.store();

        Book actual = dsl.selectFrom(BOOK).where(BOOK.ISBN.eq("999-9999999999")).fetchOneInto(Book.class);
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getIsbn(), is(book.getIsbn()));
        assertThat(actual.getAuthorId(), is(book.getAuthorId()));
        assertThat(actual.getTitle(), is(book.getTitle()));
        assertThat(actual.getPublishDate(), is(book.getPublishDate()));
    }

    @Test
    @Transactional
    public void insert2() {
        Date publishDate = new Date(new DateTime("2000-03-17").getMillis());
        int result = dsl
                .insertInto(BOOK, BOOK.ISBN, BOOK.TITLE, BOOK.AUTHOR_ID, BOOK.PUBLISH_DATE)
                .values("999-9999999999", "進撃の巨人 （１）", 4, publishDate)
                .execute();
    }

    @Test
    @Transactional
    public void insert3() {
        Date publishDate = new Date(new DateTime("2000-03-17").getMillis());
        int result = dsl
                .insertInto(BOOK)
                .set(BOOK.ISBN, "999-9999999999")
                .set(BOOK.TITLE, "進撃の巨人 （１）")
                .set(BOOK.AUTHOR_ID, 4)
                .set(BOOK.PUBLISH_DATE, publishDate)
                .execute();
    }
}
