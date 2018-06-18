package com.soon.core.db.pool;

import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.soon.util.LogUtil;

public class CPPool implements IDBPool {

	private ComboPooledDataSource dataSource;
	
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
	public CPPool(String poolName, String dbConnUrl, String dbUserName, String dbPassWord, int maxConn, int fallow) {
		try {
			dataSource = new ComboPooledDataSource();
			dataSource.setDriverClass("com.mysql.jdbc.Driver");
			dataSource.setUser(dbUserName);
			dataSource.setPassword(dbPassWord);
			dataSource.setJdbcUrl(dbConnUrl);
			dataSource.setDebugUnreturnedConnectionStackTraces(true);
			// 
			dataSource.setInitialPoolSize(5); // 初始化
			dataSource.setMinPoolSize(5); // 连接池中保留的最小连接数
			dataSource.setMaxPoolSize(maxConn); // 连接池中保留的最大连接数,100
			dataSource.setMaxStatements(50); // 连接池内单个连接所拥有的最大缓存statements数
			dataSource.setMaxIdleTime(60); // 最大空闲时间,60秒内未使用则连接被丢弃
			dataSource.setCheckoutTimeout(5 * 1000); // 获取连接超时5秒
			dataSource.setAcquireRetryAttempts(3); // 获取连接重试次数
			dataSource.setAcquireRetryDelay(1000); // 获取连接重试时间间隔1秒
			dataSource.setAcquireIncrement(5); // 
			dataSource.setIdleConnectionTestPeriod(60); // 每60秒检查所有连接池中的空闲连接
			dataSource.setNumHelperThreads(3); // 异步辅助操作现程
			dataSource.setTestConnectionOnCheckin(true);
			dataSource.setTestConnectionOnCheckout(false);
		} catch (Exception e) {
			LogUtil.error("Init CPool has exception:", e);
		}
	}
	
	/**
	 * 从连接池获得一个可用连接.如果没有空闲的连接且当前连接数小于最大连接 数限制,则创建新连接.
	 * 如原来登记为可用的连接不再有效,则从向量删除之,然后递归调用自己以尝试新的可用连接.
	 */
	@Override
	public Connection getConnection() {
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			LogUtil.error("获取数据库连接失败", e);
			return null;
		}
	}

	@Override
	public void shutdown() {
		try {
			dataSource.close();
		} catch (Exception e) {
			LogUtil.error("dataSource close Exception ", e);
		}
	}

	@Override
	public String getState() {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("总连接数:").append(dataSource.getNumConnectionsDefaultUser());
			sb.append("使用中:").append(dataSource.getNumBusyConnectionsDefaultUser());
			sb.append("空闲数:").append(dataSource.getNumIdleConnectionsDefaultUser());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	@Override
	public int getCurConns() {
		if(dataSource != null){
			try {
				return dataSource.getNumConnectionsDefaultUser();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

}
