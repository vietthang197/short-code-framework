package vn.neo.shortcode.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@ConfigurationProperties(prefix = "database-config")
@Configuration
public class DatabaseSetupConfiguration {
    private boolean enable;
    private List<Instance> instances;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public List<Instance> getInstances() {
        return instances;
    }

    public void setInstances(List<Instance> instances) {
        this.instances = instances;
    }

    public static class Instance {
        private String id;
        private String jdbcUrl;
        private String username;
        private String password;
        private boolean isPrimary = false;
        private String driverClassName;
        private boolean autoCommit = true;
        private String poolName;
        private int maximumPoolSize = 2;
        private int minimumIdle = 1;
        private String ddlAuto = "none";
        private String packageToScan;
        private String dialect;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getJdbcUrl() {
            return jdbcUrl;
        }

        public void setJdbcUrl(String jdbcUrl) {
            this.jdbcUrl = jdbcUrl;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public boolean isPrimary() {
            return isPrimary;
        }

        public void setPrimary(boolean primary) {
            isPrimary = primary;
        }

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

        public boolean isAutoCommit() {
            return autoCommit;
        }

        public void setAutoCommit(boolean autoCommit) {
            this.autoCommit = autoCommit;
        }

        public String getPoolName() {
            return poolName;
        }

        public void setPoolName(String poolName) {
            this.poolName = poolName;
        }

        public int getMaximumPoolSize() {
            return maximumPoolSize;
        }

        public void setMaximumPoolSize(int maximumPoolSize) {
            this.maximumPoolSize = maximumPoolSize;
        }

        public int getMinimumIdle() {
            return minimumIdle;
        }

        public void setMinimumIdle(int minimumIdle) {
            this.minimumIdle = minimumIdle;
        }

        public String getDdlAuto() {
            return ddlAuto;
        }

        public void setDdlAuto(String ddlAuto) {
            this.ddlAuto = ddlAuto;
        }

        public String getPackageToScan() {
            return packageToScan;
        }

        public void setPackageToScan(String packageToScan) {
            this.packageToScan = packageToScan;
        }

        public String getDialect() {
            return dialect;
        }

        public void setDialect(String dialect) {
            this.dialect = dialect;
        }
    }
}
