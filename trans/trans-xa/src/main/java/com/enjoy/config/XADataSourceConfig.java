package com.enjoy.config;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import javax.transaction.UserTransaction;
import java.util.Properties;

@Configuration
public class XADataSourceConfig {
	@Autowired
	private Environment env;

	@Bean(name = "dataSourceOne")
	@Primary
	public DataSource jamesDataSource(Environment env) {
		AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
		ds.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
		ds.setUniqueResourceName("dataSourceOne");
		ds.setPoolSize(5);
		ds.setXaProperties(build("spring.datasource.druid.dataSourceOne."));
		return ds;
	}

	@Bean(name = "dataSourceTwo")
	public DataSource peterDataSource(Environment env) {
		AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
		Properties prop = build("spring.datasource.druid.dataSourceTwo.");
		ds.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
		ds.setUniqueResourceName("dataSourceTwo");
		ds.setPoolSize(5);
		ds.setXaProperties(prop);

		return ds;
	}

	@Bean("dbsOneJdbcTemplate")
	public JdbcTemplate jamesJdbcTemplate(@Qualifier("dataSourceOne") DataSource ds) {
		return new JdbcTemplate(ds);
	}

	@Bean("dbsTowJdbcTemplate")
	public JdbcTemplate peterJdbcTemplate(@Qualifier("dataSourceTwo") DataSource ds) {
		return new JdbcTemplate(ds);
	}

	@Bean
	public JtaTransactionManager regTransactionManager () {
		UserTransactionManager userTransactionManager = new UserTransactionManager();
		UserTransaction userTransaction = new UserTransactionImp();
		return new JtaTransactionManager(userTransaction, userTransactionManager);
	}

	private Properties build(String prefix) {
		Properties prop = new Properties();
		prop.put("url", env.getProperty(prefix + "url"));
		prop.put("username", env.getProperty(prefix + "username"));
		prop.put("password", env.getProperty(prefix + "password"));

		return prop;
	}

}
