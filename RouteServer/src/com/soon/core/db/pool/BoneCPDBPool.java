package com.soon.core.db.pool;

import java.sql.Connection;
import java.sql.SQLException;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.soon.core.config.DbConfigInfo;
import com.soon.util.DESUtil;
import com.soon.util.LogUtil;

public class BoneCPDBPool implements IDBPool {

	private static final String dbUrl = "jdbc:mysql://%s?characterEncoding=utf-8&autoReconnect=true";
	private BoneCP connectionPool = null;
	private BoneCPConfig config;

	/**
	 * 创建新的连接池构造函数
	 * 
	 * @param poolName
	 *            连接池名字
	 * @param dbConnUrl
	 *            数据库的JDBC URL
	 * @param dbUserName
	 *            数据库帐号或 null
	 * @param dbPassWord
	 *            密码或 null
	 * @param maxConn
	 *            此连接池允许建立的最大连接数
	 */
	public BoneCPDBPool(DbConfigInfo configInfo) throws Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		
		String url =  configInfo.getUrl();
		url = String.format(dbUrl, url);
		
		String name = url.substring(url.lastIndexOf("/")+1);
		String user = configInfo.getUserName();
		String password = configInfo.getPassword();
		
		config = configInfo.getBoneCPConfig();
		config.setPoolName("bonecp_"+name);
		config.setJdbcUrl(configInfo.getUrl());
		config.setUsername(DESUtil.decrypt(user));
		config.setPassword(DESUtil.decrypt(password));

		// Connection助手线程,默认为3,设置大于0时，连接池在每个分区中创建Connection助手线程处理回收关闭后的connection（应用程序会通过助手线程异步地将这个connection放置到一个临时队列中进行处理)。对于应用程序在每个connection上处理大量工作时非常有用。可能会降低运行速度，不过在高并发的应用中会提高性能。
		config.setReleaseHelperThreads(3);
		//设置statement助手线程数，默认为3
		config.setStatementReleaseHelperThreads(3);
		// 缓存prepared statements的大小，默认值：0
		config.setStatementsCacheSize(50);
		// 设置重新获取连接的次数间隔时间。这个参数默认为7000，单位：毫秒。如果小于等于0，BoneCP将设置为1000
		config.setAcquireRetryDelayInMs(3000);
		// 设置重新获取连接的次数。这个参数默认为5
		config.setAcquireRetryAttempts(5);
		// 设置connection的空闲存活时间。这个参数默认为60，单位：分钟。设置为0该功能失效。通过ConnectionTesterThread观察每个分区中的connection，如果这个connection距离最后使用的时间大于这个参数就会被清除。
		config.setIdleMaxAgeInMinutes(1);
		// 设置事务回放功能。这个参数默认为false
		config.setTransactionRecoveryEnabled(true);
		// 通过ConnectionTesterThread观察每个分区中的connection，
		// 如果这个connection距离最后使用的时间大于这个参数并且距离上一次测试的时间大于这个参数就会向数据库发送一条测试语句，如果执行失败则将这个connection清除。
		config.setIdleConnectionTestPeriodInMinutes(10);
		//设置连接池阀值，默认20，小于0或大于100，BoneCP将设置为20
		config.setPoolAvailabilityThreshold(20);
		
		connectionPool = new BoneCP(config);
	}

	public void shutdown() {
		if (connectionPool != null) {
			connectionPool.close();
		}
	}

	public String getState() {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("total:" + connectionPool.getTotalCreatedConnections()).append(",");
			sb.append("used:" + connectionPool.getTotalLeased()).append(",");
			sb.append("free:" + connectionPool.getTotalFree());
		} catch (Exception e) {
			LogUtil.error("Pool Error", e);
		}
		return sb.toString();
	}

	public BoneCP getBoneCp() {
		return connectionPool;
	}

	/**
	 * 从连接池获得一个可用连接.<br>
	 * 如果没有空闲的连接且当前连接数小于最大连接 数限制,则创建新连接. <br>
	 * 如原来登记为可用的连接不再有效,则从向量删除之,然后递归调用自己以尝试新的可用连接.
	 */
	public Connection getConnection() {
		try {
			return connectionPool.getConnection();
		} catch (SQLException e) {
			LogUtil.error("获取数据库连接失败", e);
			return null;
		}
	}

	/**
	 * 获取当前总连接数
	 */
	@Override
	public int getCurConns() {
		if(connectionPool!=null){			
			return connectionPool.getTotalCreatedConnections();
		}
		LogUtil.error("connectionPool is null");
		return 0;		
	}
}
