package com.soon.route;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.soon.core.socket.conn.bean.MessageObject;
import com.soon.socket.msg.MessageID;
import com.soon.util.LogUtil;

import io.netty.channel.Channel;

/**
 * 管理玩家连接
 * @author songlin
 */
public class PlayerMgr {

	public static final String TEMP_USER_KEY = "TEMP_USER_KEY";
	private static Map<Integer, Player> onlineMap; // 已登录用户
	private static Map<Integer, Player> tempMap; //登陆网关但未经过逻辑服验证

	public static void init() {
		onlineMap = new ConcurrentHashMap<Integer, Player>();
		tempMap = new ConcurrentHashMap<Integer, Player>();
	}
	
	public static Player getOnlinePlayer(int playerId) {
		return onlineMap.get(playerId);
	}
	
	public static boolean checkOnline(int playerId) {
		return onlineMap.containsKey(playerId);
	}
	
	public static void addOnline(int playerId,String key){
		Player player = removeTemp(playerId,key);
		if(player == null){
			LogUtil.error("找不到登陆连接信息,"+playerId+" key:"+key);
			return;
		}
		
		onlineMap.put(playerId, player);
		
		//通知其他服以及客户端登陆成功
		MessageObject msgObj = new MessageObject(MessageID.P_LoginGame, playerId, null);
		ConnectMgr.route(msgObj);
		
		msgObj = new MessageObject(MessageID.C_LoginGame, playerId, null);
		sendToPlayer(player, msgObj);
	}
	
	public static void removeOnline(int playerId,Channel channel){
		Player player = getOnlinePlayer(playerId);
		if(player == null || player.getChannel() != channel){
			return;
		}
		onlineMap.remove(playerId);
		
		//通知其他服以及客户端退出游戏
		MessageObject msgObj = new MessageObject(MessageID.P_LogOutGame, playerId, null);
		ConnectMgr.route(msgObj);
		
		msgObj = new MessageObject(MessageID.C_LogOutGame, playerId, null);
		sendToPlayer(player, msgObj);
	}

	/**
	 * 广播消息,网关这里只有三种消息，1：给一个玩家(sendToPlayer) 2：给所有在线玩家，3:给其他服务器(ConnectMgr.route())
	 * 给好友，家庭成员，队友等发消息，在RelationServer中处理(迭代，分发)
	 * @param msgObj
	 */
	public static void broadcastMessage(MessageObject msgObj){
		int size = onlineMap.size();
		List<Player> users = new ArrayList<Player>(size);
		for (Player user : onlineMap.values()) {
			users.add(user);
		}
		
		for (Player player : users) {
			sendToPlayer(player,msgObj);
		}
	}
	
	public static void sendToPlayer(Player player,MessageObject msgObj){
		if(player == null || msgObj == null){
			return ;
		}
		Channel channel = player.getChannel();
		if(channel != null){
			channel.writeAndFlush(msgObj);
		}
	}
	
	/**
	 * 保存未经过验证的session对象
	 * @param userId 玩家id
	 * @param key 玩家key,客户端的发送来的key值
	 * @param channel 玩家连接
	 */
	public static void addTemp(int playerId, String key, Channel channel) {
		Player player = new Player(playerId, key, channel);
		tempMap.put(playerId, player);
	}
	
	public static Player removeTemp(int playerId,String key) {
		Player temp = tempMap.get(playerId);
		if (temp.getKey().equals(key)) {
			return tempMap.remove(playerId);
		}
		return null;
	}
	
	public static void stop() {
		try {
			for (Player temp : tempMap.values()) {
				temp.getChannel().close();
			}

			for (Player temp : onlineMap.values()) {
				temp.getChannel().close();
			}
		} catch (Exception e) {
			LogUtil.error("关闭客户端连接错误",e);
		}
	}
}

class Player {
	private int playerId;
	private String key ; //客户端每次登陆带的身份标识，每次发起连接时，根据时间戳生成
	private Channel channel; //客户端连接
	private String playerServer; //如果逻辑服部署有多个实例负载均衡，记录玩家连接的实例名
	private String crossServer; //如果跨服部署有多个实例负载均衡，记录玩家连接的实例名
	
	public Player(int playerId,String key,Channel channel){
		this.playerId = playerId;
		this.key = key;
		this.channel = channel;
	}
	
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Channel getChannel() {
		return channel;
	}
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	public String getPlayerServer() {
		return playerServer;
	}
	public void setPlayerServer(String playerServer) {
		this.playerServer = playerServer;
	}
	public String getCrossServer() {
		return crossServer;
	}
	public void setCrossServer(String crossServer) {
		this.crossServer = crossServer;
	}
}


