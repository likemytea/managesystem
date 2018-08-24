package com.chenxing.managesystem.config;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.chenxing.common.jdbc.MyJdbcTemplate;
import com.google.common.collect.Lists;

import io.shardingsphere.core.api.ShardingDataSourceFactory;
import io.shardingsphere.core.api.config.MasterSlaveRuleConfiguration;
import io.shardingsphere.core.api.config.ShardingRuleConfiguration;
import io.shardingsphere.core.api.config.TableRuleConfiguration;
import io.shardingsphere.core.api.config.strategy.InlineShardingStrategyConfiguration;

/**
 * Description:
 * 
 * @author liuxing
 * @date 2018年7月23日
 * @version 1.0
 */
@Configuration
public class DBConfig {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 注入jdbctemplate模板
	 * 
	 */
	@Bean(name = "myJdbcTemplatep3")
	public MyJdbcTemplate getJdbcTemplatePrimary1(@Qualifier("shardingDataSource") DataSource dataSource) {
		return new MyJdbcTemplate(dataSource);
	}

	/**
	 * 注入shardingjdbc数据源
	 * 
	 */
	@Bean(name = "shardingDataSource", destroyMethod = "close")
	@Qualifier("shardingDataSource")
	public DataSource getShardingDataSource() throws Exception {
		// 配置真实数据源
		Map<String, DataSource> dataSourceMap = createDataSourceMap();
		// 配置库分片和表分片规则
		ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
		shardingRuleConfig.getTableRuleConfigs().add(getUserTableRuleConfig());
		shardingRuleConfig.getTableRuleConfigs().add(getUserRoleTableRuleConfig());

		shardingRuleConfig.getBindingTableGroups().add("sys_user, sys_role_user");
		// 配置默认的数据源名称（如果不配置此项，没有分库的表在插入数据库的时候，就不知路由到哪个数据库）
		shardingRuleConfig.setDefaultDataSourceName("rbac_shar");

		// 配置读写分离
		shardingRuleConfig.setMasterSlaveRuleConfigs(getMasterSlaveRuleConfiguration());

		DataSource dataSource = null;
		try {
			dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig,
					new HashMap<String, Object>(), new Properties());

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dataSource;
	}

	/** 读写分离规则配置 **/
	public List<MasterSlaveRuleConfiguration> getMasterSlaveRuleConfiguration() throws SQLException {

		MasterSlaveRuleConfiguration masterSlaveRuleConfig1 = new MasterSlaveRuleConfiguration("rbac_shar_0",
				"rbac_shar_0", Arrays.asList("rbac_shar_0_slave0", "rbac_shar_0_slave1"));
		MasterSlaveRuleConfiguration masterSlaveRuleConfig2 = new MasterSlaveRuleConfiguration("rbac_shar_1",
				"rbac_shar_1", Arrays.asList("rbac_shar_1_slave0", "rbac_shar_1_slave1"));
		return Lists.newArrayList(masterSlaveRuleConfig1, masterSlaveRuleConfig2);

	}

	// 配置sys_user 分片规则
	private TableRuleConfiguration getUserTableRuleConfig() {

		TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration();
		orderTableRuleConfig.setLogicTable("sys_user");
		orderTableRuleConfig.setActualDataNodes("rbac_shar_${0..1}.sys_user_${0..1}");
		// 配置分库策略（Groovy表达式配置db规则）
		orderTableRuleConfig.setDatabaseShardingStrategyConfig(
				new InlineShardingStrategyConfiguration("sys_user_id", "rbac_shar_${sys_user_id % 2}"));

		// 配置分表策略（Groovy表达式配置表路由规则）
		orderTableRuleConfig.setTableShardingStrategyConfig(
				new InlineShardingStrategyConfiguration("province_id", "sys_user_${province_id % 2}"));
		return orderTableRuleConfig;
	}

	// 配置sys_role_user 分片规则
	private TableRuleConfiguration getUserRoleTableRuleConfig() {

		TableRuleConfiguration config = new TableRuleConfiguration();
		config.setLogicTable("sys_role_user");
		config.setActualDataNodes("rbac_shar_${0..1}.sys_role_user_${0..1}");
		// 配置分库策略（Groovy表达式配置db规则）
		config.setDatabaseShardingStrategyConfig(
				new InlineShardingStrategyConfiguration("sys_user_id", "rbac_shar_${sys_user_id % 2}"));
		// 配置分表策略（Groovy表达式配置表路由规则）
		config.setTableShardingStrategyConfig(
				new InlineShardingStrategyConfiguration("province_id", "sys_role_user_${province_id % 2}"));
		return config;
	}
	Map<String, DataSource> createDataSourceMap() throws Exception {
		Map<String, DataSource> result = new HashMap<>();
		result.put("rbac_shar", getDataSource("rbac_shar"));
		result.put("rbac_shar_0", getDataSource("rbac_shar_0"));
		result.put("rbac_shar_0_slave0", getDataSource("rbac_shar_0_slave0"));
		result.put("rbac_shar_0_slave1", getDataSource("rbac_shar_0_slave1"));
		result.put("rbac_shar_1", getDataSource("rbac_shar_1"));
		result.put("rbac_shar_1_slave0", getDataSource("rbac_shar_1_slave0"));
		result.put("rbac_shar_1_slave1", getDataSource("rbac_shar_1_slave1"));

		return result;
	}

	private DruidDataSource getDataSource(String dsname) throws Exception {
		DruidDataSource ds = createDefaultDruidDataSource();
		if ("rbac_shar".equals(dsname)) {
			ds.setDriverClassName("com.mysql.jdbc.Driver");
			ds.setUrl("jdbc:mysql://172.16.31.43:3306/rbac_shar?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
			ds.setUsername("liuxing");
			ds.setPassword("Liuxing009!");
			return ds;
		} else if ("rbac_shar_0".equals(dsname)) {
			ds.setDriverClassName("com.mysql.jdbc.Driver");
			ds.setUrl(
					"jdbc:mysql://172.16.31.43:3306/rbac_shar_0?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
			ds.setUsername("liuxing");
			ds.setPassword("Liuxing009!");
			return ds;
		} else if ("rbac_shar_0_slave0".equals(dsname)) {
			ds.setDriverClassName("com.mysql.jdbc.Driver");
			ds.setUrl(
					"jdbc:mysql://172.16.31.43:3306/rbac_shar_0_slave0?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
			ds.setUsername("liuxing");
			ds.setPassword("Liuxing009!");
			return ds;
		} else if ("rbac_shar_0_slave1".equals(dsname)) {
			ds.setDriverClassName("com.mysql.jdbc.Driver");
			ds.setUrl(
					"jdbc:mysql://172.16.31.43:3306/rbac_shar_0_slave1?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
			ds.setUsername("liuxing");
			ds.setPassword("Liuxing009!");
			return ds;
		} else if ("rbac_shar_1".equals(dsname)) {
			ds.setDriverClassName("com.mysql.jdbc.Driver");
			ds.setUrl(
					"jdbc:mysql://172.16.31.43:3306/rbac_shar_1?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
			ds.setUsername("liuxing");
			ds.setPassword("Liuxing009!");
			return ds;
		} else if ("rbac_shar_1_slave0".equals(dsname)) {
			ds.setDriverClassName("com.mysql.jdbc.Driver");
			ds.setUrl(
					"jdbc:mysql://172.16.31.43:3306/rbac_shar_1_slave0?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
			ds.setUsername("liuxing");
			ds.setPassword("Liuxing009!");
			return ds;
		} else if ("rbac_shar_1_slave1".equals(dsname)) {
			ds.setDriverClassName("com.mysql.jdbc.Driver");
			ds.setUrl(
					"jdbc:mysql://172.16.31.43:3306/rbac_shar_1_slave1?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
			ds.setUsername("liuxing");
			ds.setPassword("Liuxing009!");
			return ds;
		} else {
			throw new Exception("did not discover this datasource name!");
		}
	}

	public DruidDataSource createDefaultDruidDataSource() {
		DruidDataSource druidDataSource = new DruidDataSource();
		druidDataSource.setMaxWait(60000l);
		druidDataSource.setMaxActive(5);
		druidDataSource.setInitialSize(1);
		druidDataSource.setMinIdle(1);
		druidDataSource.setTimeBetweenEvictionRunsMillis(3000l);
		druidDataSource.setMinEvictableIdleTimeMillis(300000l);
		druidDataSource.setConnectionProperties("druid.stat.slowSqlMillis=3000");
		druidDataSource.setValidationQuery("SELECT 'x'");
		druidDataSource.setTestWhileIdle(true);
		druidDataSource.setTestOnBorrow(false);
		druidDataSource.setTestOnReturn(false);
		return druidDataSource;
	}
	// /** 数据源1 start */
	// @Bean(name = "dataSourcep3", destroyMethod = "close", initMethod = "init")
	// @ConfigurationProperties("spring.datasource.p3")
	// public com.alibaba.druid.pool.DruidDataSource getDataSourcep1() {
	//
	// log.info("initializing data source p3....！");
	// DruidDataSource druidDataSource = new DruidDataSource();
	// return druidDataSource;
	// }
	//
	// @Bean(name = "myJdbcTemplatep3")
	// public MyJdbcTemplate getJdbcTemplatePrimary1(@Qualifier("dataSourcep3")
	// DataSource dataSource) {
	// return new MyJdbcTemplate(dataSource);
	// }
}
