package com.soon.core.config;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

import com.soon.dao.GameSiteInfoDao;
import com.soon.util.FileUtil;
import com.soon.util.LogUtil;
import com.soon.util.Util;

public class Config {
	public static final String DB_GLOBAL = "global";
	public static final String DB_STATIC = "static";
	public static final String DB_LOG = "log";
	public static final String DB_STRATEGY = "player";
	public static final String[] DB_TYPE_ARR = {DB_GLOBAL,DB_STATIC,DB_LOG,DB_STRATEGY};

	public static String serverName = null;
	public static Map<String,DbConfigInfo> dbMap = new HashMap<String,DbConfigInfo>();
	public static Map<String,ServerInfo> serverMap = new HashMap<String,ServerInfo>();
	public static List<GameSiteInfo> gameSiteList = new ArrayList<GameSiteInfo>();
	
	public static void main(String[] args) {
		init(null);
		while(true){
			
		}
	}
	
	public static void init(String path){
		if(!dbMap.isEmpty()){
			return;
		}
		try {
			NetConfigXml.init(path);
			
			// 配置Log4j
			String log4jPath = FileUtil.joinPath(Util.getProjectPath(),"config/log4j.properties");
			PropertyConfigurator.configure(log4jPath);
			
			initGameSiteList();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void loadFromPro() throws Exception{
		String path = FileUtil.joinPath(Util.getProjectPath(),"config/config.properties");
		
		FileInputStream fis = new FileInputStream(path);
		Properties prop = new Properties();
		prop.load(fis);
		
		serverName = prop.getProperty("serverName");
		for (String typeName : Config.DB_TYPE_ARR) {
			loadPro(prop, typeName, dbMap);
		}
	}
	
	private static void loadPro(Properties prop,String nodeName,Map<String,DbConfigInfo> map){
		String url = prop.getProperty("db_"+nodeName);
		String username = prop.getProperty("db_"+nodeName+"_user");
		String password = prop.getProperty("db_"+nodeName+"_pwd");
		String poolCfgPath = prop.getProperty("db_"+nodeName+"_cfg");
		
		DbConfigInfo info = new DbConfigInfo(url, username, password,poolCfgPath);
		map.put(nodeName, info);
		
		LogUtil.info(info.toString());
	}
	
	private static void initGameSiteList() throws Exception{
		GameSiteInfoDao dao = new GameSiteInfoDao();
		gameSiteList = dao.getSiteInfoList();
	}
	
	/**
	 * 当前服务器状态信息
	 */
	public static GameSiteInfo getCurSiteInfo(){
		for (GameSiteInfo info : gameSiteList) {
			if(info.getServerName().equals(serverName)){
				return info;
			}
		}
		return null;
	}
	
	public static Map<String,ServerInfo> getServerMap(){
		return serverMap;
	}
	
	public static List<ServerInfo> getServerConfigList(String serverType){
		List<ServerInfo> list = new ArrayList<ServerInfo>();
		for(Entry<String,ServerInfo> entry : serverMap.entrySet()){
			ServerInfo config = entry.getValue();
			if(config.getServerName().equals(serverType)){
				list.add(config);
			}
		}
		return list;
	}
	
	public static ServerInfo getServerConfig(String name){
		return serverMap.get(name);
	}
	
	public static DbConfigInfo getDbConfig(String name){
		return dbMap.get(name);
	}
	
	public static List<DbConfigInfo> getDbConfigAll(){
		List<DbConfigInfo> list = new ArrayList<DbConfigInfo>();
		for(Entry<String,DbConfigInfo> entry : dbMap.entrySet()){
			list.add(entry.getValue());
		}
		return list;
	}
}
