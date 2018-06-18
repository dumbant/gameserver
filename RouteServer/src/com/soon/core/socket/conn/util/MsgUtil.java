package com.soon.core.socket.conn.util;

import com.google.protobuf.AbstractMessage.Builder;
import com.soon.core.socket.conn.bean.MessageObject;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.google.protobuf.Message;

public class MsgUtil {
	public static final int MAXLEN = Short.MAX_VALUE;
	public static final String KEY = "SdouNmgbl3i5n43";
	public static final byte[] KEY_BYTE = KEY.getBytes();
	
	public static void main(String[] args) {
		System.out.println(Short.MAX_VALUE);
		System.out.println(Integer.MAX_VALUE);
	}
	
	public static MessageObject buildMessage(short code, int playerId, Message message) {
		MessageObject msg = new MessageObject(code, playerId, message.toByteArray());
		return msg;
	}
	
	public static MessageObject buildMessage(short code, int playerId, Builder<?> messageBuilder) {
		MessageObject msg = new MessageObject(code, playerId,messageBuilder.build().toByteArray());
		return msg;
	} 
	
	public static MessageObject getMessageObject(ByteBuf buf){
		if(buf != null){
			int bufLen = buf.readableBytes();
			short len = buf.readShort();
			if(bufLen < len){
				System.err.println("数据错误");
				return null;
			}
			int codeId = buf.readInt();
			int playerId = buf.readInt();
			byte[] data = new byte[len-10];
			buf.readBytes(data);
			byte[] content = MsgUtil.decryptForDis(data);
			
			return new MessageObject(codeId, playerId,content);
		}
		return null;
	}
	
	public static ByteBuf getByteBuf(MessageObject msgObj){
		byte[] data = MsgUtil.encryptForDis(msgObj.getContent());
		
		//10 = short(len) + int(msgId) + int(playerId)
		byte[] packetBytes = new byte[data.length + 8];
		
		// 添加包长
		short length = (short)packetBytes.length;
		packetBytes[0] = (byte) (length >> 8);
		packetBytes[1] = (byte) (length);
		
		//添加消息编号
		int msgId = msgObj.getMsgId();
		packetBytes[2] = (byte) (msgId >> 24);
		packetBytes[3] = (byte) (msgId >> 16);
		packetBytes[4] = (byte) (msgId >> 8);
		packetBytes[5] = (byte) (msgId);
		
		//添加消playerId
		int playerId = msgObj.getPlayerId();
		packetBytes[6] = (byte) (playerId >> 24);
		packetBytes[7] = (byte) (playerId >> 16);
		packetBytes[8] = (byte) (playerId >> 8);
		packetBytes[9] = (byte) (playerId);
		
		System.arraycopy(data, 0, packetBytes, 10, data.length);
		
		// 1024*1024 = 1048576;1M
		return Unpooled.directBuffer(length,1048576).writeBytes(packetBytes);
	}
	
	/**
	 * 位加密
	 */
	public static byte[] encryptForDis (byte[] bytes){
		if(bytes == null){
			return new byte[0];
		}
		byte[] mes = new byte[KEY_BYTE.length+bytes.length];
		System.arraycopy(KEY_BYTE,0,mes,0,KEY_BYTE.length);
		System.arraycopy(bytes,0,mes,KEY_BYTE.length,bytes.length);

		byte buff;
		for(int i=0; i<mes.length; i+=5){
			if(i + 3 > mes.length - 1) break;
			buff = (byte) ~mes[i + 2];
			mes[i + 2] = mes[i + 3];
			mes[i + 3] = buff;
		}
		return mes;
	}

	/**
	 * 位解密
	 */
	public static byte[] decryptForDis (byte[] bytes){
		if(bytes == null){
			return new byte[0];
		}
		byte buff;
		for(int i=0; i<bytes.length; i+=5){
			if(i + 3 > bytes.length - 1) break;
			buff = bytes[i + 2];
			bytes[i + 2] = (byte) ~bytes[i + 3];
			bytes[i + 3] = buff;
		}
		byte[] mes = new byte[bytes.length-KEY_BYTE.length];
		System.arraycopy(bytes,KEY_BYTE.length,mes,0,bytes.length-KEY_BYTE.length);
		return mes;
	}
}
