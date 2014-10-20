package org.yukung.sandbox.jooq;

import org.jooq.DSLContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JooqConfiguration.class)
public class ReadTest {

    private static final Logger log = LoggerFactory.getLogger(ReadTest.class);

    @Autowired
    DSLContext dsl;

    @Test
    @Transactional
    public void select() {
    }

}
