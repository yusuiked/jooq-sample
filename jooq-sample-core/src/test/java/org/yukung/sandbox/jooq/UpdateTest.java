package org.yukung.sandbox.jooq;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.jooq.impl.DSL.*;
import static org.yukung.sandbox.db.jooq.gen.tables.Book.*;

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
public class UpdateTest {

    private static final Logger log = LoggerFactory.getLogger(UpdateTest.class);

    @Autowired
    DSLContext dsl;

    @Test
    @Transactional
    public void update1() {
        int result = dsl
                .update(BOOK)
                .set(BOOK.TITLE, "xxxxx")
                .where(BOOK.ISBN.eq("999-9999999999"))
                .execute();
    }

    @Test
    @Transactional
    public void update2() {
        int result = dsl
                .update(BOOK)
                .set(
                    row(BOOK.TITLE),
                    row("xxxxx"))
                .where(BOOK.ISBN.eq("999-9999999999"))
                .execute();
    }

    @Test
    @Transactional
    public void udpate3() {
        BookRecord book = dsl
                .selectFrom(BOOK)
                .where(BOOK.ISBN.eq("001-0000000001"))
                .fetchOne();
        String origin = book.getTitle();
        book.setTitle("xxxxxxxxxx");
        book.store();

        Book actual = dsl
                .selectFrom(BOOK)
                .where(BOOK.ISBN.eq("001-0000000001"))
                .fetchOneInto(Book.class);
        assertThat(actual.getTitle(), is(not(origin)));
    }
}
