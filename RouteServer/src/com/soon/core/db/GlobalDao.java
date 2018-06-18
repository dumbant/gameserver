package com.soon.core.db;

import java.sql.Connection;

import com.soon.core.config.Config;
import com.soon.core.config.DbConfigInfo;
import com.soon.core.db.pool.DbPoolMgr;
import com.soon.core.db.pool.IDBPool;

public class GlobalDao extends AbstractDao {
	private static IDBPool pool;
	
	@Override
	public Connection getConnection() {
		if(pool == null){
			DbConfigInfo configInfo = Config.getDbConfig(Config.DB_GLOBAL);
			if(configInfo != null){
				pool = DbPoolMgr.initPool(configInfo);
			}
		}
		return pool.getConnection();
	}

	public void shutdown(){
		if (pool != null) {
			pool.shutdown();
			pool = null;
		}
	}
}
