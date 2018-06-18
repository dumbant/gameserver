package com.soon.util;

import java.io.File;

public class Util {

	public static String getProjectPath(){
		//file:/E:/work/workspace/gameserver/RouteServer/bin/
		File classPath = new File(Thread.currentThread().getContextClassLoader().getResource("").toString().replace("%20", " "));
		
		//file:\E:\work\workspace\gameserver\RouteServer
		String projectPath = classPath.getParentFile().getPath();
		projectPath = projectPath.substring(5, projectPath.length());
		
		return FileUtil.separator(projectPath);
	}
	
	public static boolean isEmpty(String str){
		if(str == null || str.isEmpty()){
			return true;
		}
		return false;
	}
}
