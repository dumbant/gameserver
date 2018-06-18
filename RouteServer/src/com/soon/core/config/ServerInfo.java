package com.soon.core.config;

public class ServerInfo {

	private int id;
	private String serverName;
	private String address;
	private int port;
	private int adminPort;
	private int webPort;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public int getAdminPort() {
		return adminPort;
	}
	public void setAdminPort(int adminPort) {
		this.adminPort = adminPort;
	}
	public int getWebPort() {
		return webPort;
	}
	public void setWebPort(int webPort) {
		this.webPort = webPort;
	}
	
}
