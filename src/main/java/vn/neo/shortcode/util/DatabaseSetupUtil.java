package vn.neo.shortcode.util;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import vn.neo.shortcode.config.PersistenceCreator;
import vn.neo.shortcode.properties.DatabaseSetupConfiguration;

@Component
@EnableConfigurationProperties
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
@ConditionalOnProperty(value = "database-config.enable", havingValue = "true", matchIfMissing = false)
public class DatabaseSetupUtil {
    private final DatabaseSetupConfiguration configuration;
    private final ApplicationContext applicationContext;

    public DatabaseSetupUtil(ApplicationContext applicationContext, DatabaseSetupConfiguration configuration) {
        this.applicationContext = applicationContext;
        this.configuration = configuration;

        setup();
    }

    private void setup() {
        AutowireCapableBeanFactory factory =
                applicationContext.getAutowireCapableBeanFactory();

        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) factory;
        for (DatabaseSetupConfiguration.Instance instance : configuration.getInstances()) {
            PersistenceCreator persistenceCreator = new PersistenceCreator(instance);

            HikariDataSource dataSource = persistenceCreator.getDataSource();
            BeanDefinition hikariDefinition = BeanDefinitionBuilder.genericBeanDefinition(HikariDataSource.class, () -> dataSource).getBeanDefinition();
            hikariDefinition.setBeanClassName(HikariDataSource.class.getName());
            hikariDefinition.setPrimary(instance.isPrimary());
            hikariDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
            hikariDefinition.setAutowireCandidate(true);
            registry.registerBeanDefinition(instance.getId(), hikariDefinition);

            LocalContainerEntityManagerFactoryBean factoryBean = persistenceCreator.getEntityManager(dataSource);
            BeanDefinition entityManagerDefinition = BeanDefinitionBuilder.genericBeanDefinition(LocalContainerEntityManagerFactoryBean.class, () -> factoryBean).getBeanDefinition();
            entityManagerDefinition.setBeanClassName(LocalContainerEntityManagerFactoryBean.class.getName());
            entityManagerDefinition.setPrimary(instance.isPrimary());
            entityManagerDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
            entityManagerDefinition.setAutowireCandidate(true);
            registry.registerBeanDefinition(instance.getId() + "EntityManager", entityManagerDefinition);

            PlatformTransactionManager transactionManager = persistenceCreator.getTransactionManager(factoryBean);
            BeanDefinition transactionManagerDefinition = BeanDefinitionBuilder.genericBeanDefinition(PlatformTransactionManager.class, () -> transactionManager).getBeanDefinition();
            transactionManagerDefinition.setBeanClassName(PlatformTransactionManager.class.getName());
            transactionManagerDefinition.setPrimary(instance.isPrimary());
            transactionManagerDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
            transactionManagerDefinition.setAutowireCandidate(true);
            registry.registerBeanDefinition(instance.getId() + "TransactionManager", transactionManagerDefinition);
        }
    }
}
