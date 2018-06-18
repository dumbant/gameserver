package com.soon.core.db.pool;

import java.sql.Connection;

public interface IDBPool {

	public Connection getConnection();

	public void shutdown();
	
	public String getState();
	
	public int getCurConns();	
}
