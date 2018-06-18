package com.soon.core.socket.conn.bean;

import java.util.List;

/**
 * 网络传输间的消息对象
 * @author songlin
 */
public class MessageObject {
	
	private int msgId;
	private int playerId; // 发送消息的玩家ID
	private byte[] content;
	
	public MessageObject(int msgId,int playerId,byte[] content){
		this.msgId = msgId;
		this.playerId = playerId;
		if(content != null){
			this.content = content;
		}
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("playerId : ").append(playerId);
		sb.append(", msgId : ").append(Integer.toHexString(msgId));
		sb.append(", content : ");
		sb.append(new String(content));
		
		return sb.toString();
	}

	public int getMsgId() {
		return msgId;
	}

	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	
}
