package com.soon.core.socket.conn.bean;

import com.soon.core.config.ServerInfo;
import com.soon.core.socket.conn.client.Netty4Client;

import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;

public class Netty4LinkInfo extends LinkInfo {

	public Netty4LinkInfo(ServerInfo info){
		super(info);
	}
	
	private EventLoopGroup group;
	private Channel channel;

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	public EventLoopGroup getGroup() {
		return group;
	}

	public void setGroup(EventLoopGroup group) {
		this.group = group;
	}

	public void connect(){
		Netty4Client.initSocket(this);
	}
	
	public void sendMsg(MessageObject msgObj){
		if(channel != null && channel.isActive()){
			channel.writeAndFlush(msgObj);
		}
	}
	
	public void disConnect(){
		this.setActive(false);
		this.setLoad(0);
    	
		if(channel != null){
			channel.close();
			channel = null;
		}
		if(group != null){
			group.shutdownGracefully();
			group = null;
		}
	}
}
