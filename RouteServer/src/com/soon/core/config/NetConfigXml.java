package com.soon.core.config;

import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.soon.util.FileUtil;
import com.soon.util.Util;

public class NetConfigXml{
	
	public static void init(String path){
		//configPath = path;
		try {
			if(Util.isEmpty(path)){
				path = FileUtil.joinPath(Util.getProjectPath(),"config/server_config.xml");
			}
			SAXReader reader = new SAXReader();
			Document doc = reader.read(path);
			Element root = doc.getRootElement();
			
			Config.serverName = root.attributeValue("servername");
			
			Element dbnode = root.element("db_server");
			parseDbXml(dbnode, "db", Config.dbMap);
			
			Element gamenode = root.element("game_server");
			parseServerXml(gamenode, "game", Config.serverMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void parseDbXml(Element root, String nodeName, Map<String,DbConfigInfo> map) {
		if (root == null) {
			return;
		}
		Iterator<?> itr = root.elementIterator(nodeName);
		while (itr.hasNext()) {
			Element element = (Element) itr.next();
			String name = element.attributeValue("name");
			String address = element.attributeValue("address");
			int port = Integer.valueOf(element.attributeValue("port"));
			
			String user = element.attributeValue("user");
			String password = element.attributeValue("password");
			String poolcfg = element.attributeValue("poolcfg");
			
			String url = address+":"+port+"/"+name;
			DbConfigInfo info = new DbConfigInfo(url,user,password,poolcfg);
			
			map.put(name, info);
			
			System.out.println(info);
		}
	}
	
	private static void parseServerXml(Element root, String nodeName, Map<String,ServerInfo> map) {
		if (root == null) {
			return;
		}
		Iterator<?> itr = root.elementIterator(nodeName);
		while (itr.hasNext()) {
			Element element = (Element) itr.next();
			int id = Integer.valueOf(element.attributeValue("id"));
			String name = element.attributeValue("name");
			String address = element.attributeValue("address");
			int port = Integer.valueOf(element.attributeValue("port"));
			int adminPort = Integer.valueOf(element.attributeValue("adminPort"));
			int webPort = Integer.valueOf(element.attributeValue("webPort"));
			
			ServerInfo info = new ServerInfo();
			info.setId(id);
			info.setServerName(name);
			info.setAddress(address);
			info.setPort(port);
			info.setAdminPort(adminPort);
			info.setWebPort(webPort);
			
			map.put(name, info);
			
			System.out.println(info);
		}
	}
	
}
