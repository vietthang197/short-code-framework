package vn.neo.shortcode.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import vn.neo.shortcode.properties.DatabaseSetupConfiguration;

import java.util.HashMap;
import java.util.Map;

public class PersistenceCreator {
    private final DatabaseSetupConfiguration.Instance instance;

    public PersistenceCreator(DatabaseSetupConfiguration.Instance instance) {
        this.instance = instance;
    }

    public LocalContainerEntityManagerFactoryBean getEntityManager(HikariDataSource ds) {
        LocalContainerEntityManagerFactoryBean em =
                new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(ds);

        HibernateJpaVendorAdapter vendorAdapter =
                new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", instance.getDdlAuto());
        properties.put("hibernate.dialect", instance.getDialect());
        em.setJpaPropertyMap(properties);
        em.setPackagesToScan(instance.getPackageToScan());
        return em;
    }

    public HikariDataSource getDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDataSourceClassName(instance.getDriverClassName());
        dataSource.setJdbcUrl(instance.getJdbcUrl());
        dataSource.setUsername(instance.getUsername());
        dataSource.setPassword(instance.getPassword());
        dataSource.setMaximumPoolSize(instance.getMaximumPoolSize());
        dataSource.setMinimumIdle(instance.getMinimumIdle());
        dataSource.setAutoCommit(instance.isAutoCommit());
        dataSource.setPoolName(instance.getPoolName());
        return dataSource;
    }

    public PlatformTransactionManager getTransactionManager(LocalContainerEntityManagerFactoryBean entityManager) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManager.getObject());
        return transactionManager;
    }
}
