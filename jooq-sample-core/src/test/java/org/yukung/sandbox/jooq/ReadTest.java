package org.yukung.sandbox.jooq;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.yukung.sandbox.db.jooq.gen.tables.Book.*;

import java.sql.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.yukung.sandbox.db.jooq.gen.tables.pojos.Book;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JooqApplication.class)
public class ReadTest {

    private static final Logger log = LoggerFactory.getLogger(ReadTest.class);

    @Autowired
    DSLContext dsl;

    @Test
    @Transactional
    public void select1() {
        List<Book> books = dsl
                .selectFrom(BOOK)
                .fetch().into(Book.class);
    }

    @Test
    @Transactional
    public void select2() {
        List<Book> books = dsl
                .selectFrom(BOOK)
                .orderBy(BOOK.ISBN.asc())
                .limit(3)
                .offset(0)
                .fetch().into(Book.class);
    }

    @Test
    @Transactional
    public void select3() {
        List<Book> books = dsl
                .selectFrom(BOOK)
                .where(BOOK.AUTHOR_ID.eq(2))
                .fetch().into(Book.class);
        log.debug(books.toString());
    }

    @Test
    @Transactional
    public void select4() {
        Date dt = new Date(new java.sql.Date(
                new DateTime("2000-12-31").getMillis()).getTime());
        List<Book> books = dsl
                .selectFrom(BOOK)
                .where(BOOK.AUTHOR_ID.eq(2))
                .and(BOOK.PUBLISH_DATE.le(dt))
                .fetch().into(Book.class);
        log.debug(books.toString());
    }

    @Test
    @Transactional
    public void select5() {
        Date dt = new Date(new java.sql.Date(
                new DateTime("2000-12-31").getMillis()).getTime());

        boolean someParam = true;

        SelectQuery<Record> query = dsl.selectQuery();
        query.addFrom(BOOK);
        query.addConditions(BOOK.AUTHOR_ID.eq(2));
        if (someParam) {
            query.addConditions(BOOK.PUBLISH_DATE.le(dt));
        }
        List<Book> books = query.fetch().into(Book.class);
        log.debug(books.toString());
    }

    @Test
    @Transactional
    public void select6() {
          List<Book> books = dsl
                .select(BOOK.ISBN, BOOK.TITLE)
                .from(BOOK)
                .fetchInto(Book.class);
          books.forEach(s -> {
              assertThat(s.getIsbn(), is(notNullValue()));
              assertThat(s.getTitle(), is(notNullValue()));
              assertThat(s.getAuthorId(), is(nullValue()));
              assertThat(s.getPublishDate(), is(nullValue()));
          });
    }
}
