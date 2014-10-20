package org.yukung.sandbox.jooq;

import javax.sql.DataSource;

import net.sf.log4jdbc.Log4jdbcProxyDataSource;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class JooqConfiguration {

    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        builder.setType(EmbeddedDatabaseType.H2);
        builder.addScript("classpath:sql/00_init.sql");
        EmbeddedDatabase ds = builder.build();
        return new Log4jdbcProxyDataSource(ds);
    }

    @Bean
    public TransactionAwareDataSourceProxy transactionAwareDataSource() {
        return new TransactionAwareDataSourceProxy(dataSource());
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public DataSourceConnectionProvider connectionProvider() {
        return new DataSourceConnectionProvider(transactionAwareDataSource());
    }

    @Bean
    public ExceptionTranslator exceptionTranslator() {
        return new ExceptionTranslator();
    }

    @Bean
    public DefaultConfiguration configuration() {
        DefaultConfiguration defaultConfiguration = new DefaultConfiguration();
        defaultConfiguration.set(connectionProvider());
        defaultConfiguration.set(new DefaultExecuteListenerProvider(exceptionTranslator()));
        defaultConfiguration.set(SQLDialect.H2);
        return defaultConfiguration;
    }

    @Bean
    public DSLContext dsl() {
        return new DefaultDSLContext(configuration());
    }
}
