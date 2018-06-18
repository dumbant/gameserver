package com.soon.route;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.soon.core.config.Config;
import com.soon.core.config.ServerInfo;
import com.soon.core.socket.conn.bean.LinkInfo;
import com.soon.core.socket.conn.bean.MessageObject;
import com.soon.core.socket.conn.bean.Netty4LinkInfo;
import com.soon.core.socket.conn.server.IServerSocket;
import com.soon.core.socket.conn.server.Netty4Server;
import com.soon.util.LogUtil;

/**
 * 管理所有Socket连接
 * @author songlin
 */
public class ConnectMgr {
	public static final String SERVER_PLAYER = "playerserver";//角色数据处理
	public static final String SERVER_BATTLE = "battleserver"; //本地战斗服务器
	public static final String SERVER_CROSS = "crossserver"; //跨服服务器,BOSS，押镖 等跨服活动，跨服商店
	public static final String SERVER_WEB = "webserver"; //登陆,充值验证，返回游戏区服列表
	public static final String SERVER_RELATION = "relationserver";//好友，公会，聊天
	public static final String SERVER_ROUTE = "routeserver"; //消息中转
	
	private IServerSocket server;//当前进程监听服务
	//与其他服务器的连接信息,key: serverName_id
	public static Map<String,LinkInfo> connMap = new HashMap<String,LinkInfo>();
	
	public static void main(String[] args) {
		ConnectMgr mgr = new ConnectMgr();
		mgr.start();
	}
	
	public ConnectMgr(){
	}
	
	public void start(){
		//初始化配置
		Config.init(null);
    	
		//打开服务监听端口
		server = new Netty4Server();
		server.initServer();
		
		//初始化与其他服务器之间的连接
		initConnect();
	}
	
	/**
	 * 发送数据包
	 */
	public static void route(MessageObject msgObj) {
		if ( msgObj != null) {
			int msgId = msgObj.getMsgId();
			
			if(msgId < 5000){
				Player player = PlayerMgr.getOnlinePlayer(msgObj.getPlayerId());
				if(player != null) {
					PlayerMgr.sendToPlayer(player, msgObj);
				}
			}
			
			LinkInfo link = selectLinkInfo(msgId);
			
			if(link != null){
				link.sendMsg(msgObj);
			}
		}
	}
	
	private static LinkInfo selectLinkInfo(int msgId) {
		if(msgId < 5000){
			//另外处理
			return null;
		}
		if(msgId < 6000){
			
		}
		if(msgId < 6500){
			
		}
		if(msgId < 7000){
			
		}
		if(msgId < 7500){
			
		}
		if(msgId < 8000){
			
		}
		return null;
	}
	
	public void stop(){
		server.close();
	}
	
	/**
	 * 初始化与其他服务器之间的连接
	 */
	public void initConnect(){
		Map<String,ServerInfo> map = Config.getServerMap();
		for(Entry<String,ServerInfo> entry : map.entrySet()){
			ServerInfo info = entry.getValue();
			if(info.getServerName().equals(Config.serverName)){
				continue;
			}
			connectServer(entry.getValue());
		}
	}
	
	/**
	 * 连接其他模块服务器,使用Netty4
	 * @param info
	 */
	public void connectServer(ServerInfo info){
		LinkInfo linkInfo = new Netty4LinkInfo(info);
		connMap.put(linkInfo.getName(), linkInfo);
		
		linkInfo.connect();
		if(!linkInfo.isActive()){
			reTryConnect(linkInfo);
		}
	}
	
	public void reTryConnect(LinkInfo info){
		if(info.isActive()){
			return;
		}
		
		ReConnect retry = new ReConnect(info);
		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);// 初始化1个线程
		ScheduledFuture<?> future = executor.scheduleAtFixedRate(retry, 5, 60, TimeUnit.SECONDS);
		retry.setFuture(future);
	}
	
	class ReConnect implements Runnable{
		long startTime = System.currentTimeMillis();
		long waitTime = 60000;
		LinkInfo linkInfo ;
		ScheduledFuture<?> future;
		
		public ReConnect(LinkInfo linkInfo){
			this.linkInfo = linkInfo;
		}
		
		public ScheduledFuture<?> getFuture() {
			return future;
		}

		public void setFuture(ScheduledFuture<?> future) {
			this.future = future;
		}

		@Override
		public void run() {
			linkInfo.connect();
			if(!linkInfo.isActive() && linkInfo.getTryCount() >= 10){
				future.cancel(true);
				LogUtil.info("结束重连："+linkInfo.getIp()+":"+linkInfo.getPort());
			}
		}
		
	}
}
