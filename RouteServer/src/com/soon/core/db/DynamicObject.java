package com.soon.core.db;

public interface DynamicObject {

	public String getInsertSql();
	public String getUpdateSql();
	public String getDeleteSql();

	/**用于批量插入**/
	public StringBuilder getStartSql();
	/**用于批量插入**/
	public StringBuilder getValueSql(StringBuilder sb);
	
}
