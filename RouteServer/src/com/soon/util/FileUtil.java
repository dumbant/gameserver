package com.soon.util;

public class FileUtil {

	public static String separator(String path){
		if(!Util.isEmpty(path)){
			path = path.replace("\\", "/");
		}
		return path;
	}
	
	public static String joinPath(String... pathArr){
		String path = "";
		int len = pathArr.length ;
		for(int i=0 ; i<len; i++){
			String str = pathArr[i] ;
			if(str == null || str.isEmpty()) {
				continue ;
			}
			str = str.replace("\\", "/") ;
			if( !path.isEmpty() ){
				if(str.indexOf(":/") > 0) {
					path = str ;
					continue ;
				}
				if(str.startsWith("/")) {
					str = str.substring(1) ;
				}
				if( !path.endsWith("/") ){
					path +="/" ;
				}
			}
			
			path += str;
		}
		return path ;
	}
}
