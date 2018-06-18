package com.soon.core.config;

import java.util.Date;

public class GameSiteInfo {

	private String serverName;
	private String address;
	private int port;
	private int webPort;
	
	private int capacity;	
	/**0:关闭，1：流畅，2：火爆，3：拥挤，4：维护中**/
	private int state;
	/**新服开服时间**/
	private Date openTime;
	/**停服维护开始时间**/
	private Date closeStartTime;
	/**停服维护结束时间**/
	private Date closeEndTime;
	
	/**0:关闭，1：流畅，2：火爆，3：拥挤，4：维护中**/
	public enum ServerState{
		CLOSE,NORMAL,HOT,CROWD,MAINTENANCE
	}
	
	public Date getCloseStartTime() {
		return closeStartTime;
	}
	public void setCloseStartTime(Date closeStartTime) {
		this.closeStartTime = closeStartTime;
	}
	public Date getCloseEndTime() {
		return closeEndTime;
	}
	public void setCloseEndTime(Date closeEndTime) {
		this.closeEndTime = closeEndTime;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public Date getOpenTime() {
		return openTime;
	}
	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}
	public int getWebPort() {
		return webPort;
	}
	public void setWebPort(int webPort) {
		this.webPort = webPort;
	}
	
}
