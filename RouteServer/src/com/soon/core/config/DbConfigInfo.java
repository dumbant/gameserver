package com.soon.core.config;

import java.io.FileInputStream;
import java.util.Properties;

import com.jolbox.bonecp.BoneCPConfig;
import com.soon.util.FileUtil;
import com.soon.util.Util;

public class DbConfigInfo {

	private String url;
	private String userName;
	private String password;
	private String poolCfgPath;
	
	public DbConfigInfo(String url,String userName,String password,String poolCfgPath){
		this.url = url;
		this.userName = userName;
		this.password = password;
		this.poolCfgPath = poolCfgPath;
	}
	
	public BoneCPConfig getBoneCPConfig() throws Exception{
		String path = FileUtil.joinPath(Util.getProjectPath(),poolCfgPath);
		FileInputStream is = new FileInputStream(path);
		Properties properties = new Properties();
		properties.load(is);
		// 初始化连接池
		BoneCPConfig boneConf = new BoneCPConfig(properties); 
		
		return boneConf;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("url=");
		sb.append(this.url);
		sb.append(",");
		sb.append("userName=");
		sb.append(this.userName);
		sb.append(",");
		sb.append("password=");
		sb.append(this.password);
		
		return sb.toString();
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getPoolCfgPath() {
		return poolCfgPath;
	}

	public void setPoolCfgPath(String poolCfgPath) {
		this.poolCfgPath = poolCfgPath;
	}
	
}
