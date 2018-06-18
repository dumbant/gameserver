package com.soon.core.db.pool;

import com.soon.core.config.DbConfigInfo;
import com.soon.util.DESUtil;

public class DbPoolMgr {
	
	public static IDBPool initPool(DbConfigInfo configInfo){
		IDBPool pool = null;
		try {
			pool = new BoneCPDBPool(configInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pool;
	}
	
	public static void main(String[] args) {
		System.out.println(DESUtil.encrypt("root"));
		System.out.println(DESUtil.encrypt("123456"));
	}
}
