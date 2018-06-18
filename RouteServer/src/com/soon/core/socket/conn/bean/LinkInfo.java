package com.soon.core.socket.conn.bean;

import com.soon.core.config.ServerInfo;

/**
 * 记录与Socket服务端的连接信息
 * @author songlin
 */
public abstract class LinkInfo {

	private String name;
	private String ip;
	private int port;
	private long refrshTime; //最后一次正常连接时间
	private int load; //负载数量
	private int tryCount; //断线重连次数
	private boolean isActive; //是否连接正常
	
	public abstract void connect();
	public abstract void sendMsg(MessageObject msgObj);
	public abstract void disConnect();
	
	public LinkInfo(){
		
	}
	
	public LinkInfo(String name,String ip,int port){
		this.name = name;
		this.ip = ip;
		this.port = port;
	}
	
	public LinkInfo(ServerInfo info){
		this.name = info.getServerName();
		this.ip = info.getAddress();
		this.port = info.getPort();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public long getRefrshTime() {
		return refrshTime;
	}
	public void setRefrshTime(long refrshTime) {
		this.refrshTime = refrshTime;
	}
	public int getLoad() {
		return load;
	}
	public void setLoad(int load) {
		this.load = load;
	}
	public int getTryCount() {
		return tryCount;
	}
	public void setTryCount(int tryCount) {
		this.tryCount = tryCount;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
}
