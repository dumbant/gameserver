package com.soon.core.socket.cmd;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.soon.RouteServer;
import com.soon.core.socket.conn.bean.MessageObject;
import com.soon.util.ClassUtil;
import com.soon.util.LogUtil;

import io.netty.channel.Channel;

public class CmdMgr {
	/**
	 * 缓存命令对象
	 **/
	private static final Map<Integer, Command> cmdCache = new HashMap<Integer, Command>();
	private static final Map<String, AdminCommand> adminCmdCache = new HashMap<String, AdminCommand>();

	private static final int corePoolSize = 4; //线程池中最小线程数量，空闲线程也不会释放
	private static final int maxPoolSize = 8; //最大线程数量
	private static final int keepAliveTime = 5; //多于corePoolSize的线程，空闲保持时间
	private static ThreadPoolExecutor pool;
	
	static {
		TimeUnit unit = TimeUnit.MINUTES;
		LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
		//当线程池阻塞时，对workQueue中未执行的任务采取的策略，DiscardPolicy：不做处理
		RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();
		pool = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, unit, workQueue, handler);
	}
	
	/**
	 * 将命令放入线程池中执行
	 */
	public static void addCmd( Channel channel, MessageObject msgObj) {
		Command command = getCommand(msgObj.getMsgId());
		if(command != null){
			CmdTask cmdTask = new CmdTask(command, channel, msgObj);
			pool.execute(cmdTask);
		}else{
			LogUtil.error("CmdId错误:"+msgObj.getMsgId());
		}
	}
	
	/**
	 * 加载命令集合
	 */
	public static boolean load() {
		try {
			Package pack = RouteServer.class.getPackage();
			Set<Class<?>> allClasses = ClassUtil.getClasses(pack);
			
			for (Class<?> clazz : allClasses) {
				try {
					Cmd cmd = clazz.getAnnotation(Cmd.class);
					if (cmd != null) {
						if (cmdCache.get(cmd.code()) != null) {
							String name = cmdCache.get(cmd.code()).getClass().getName();
							LogUtil.error("CmdId冲突, " + cmd.code() +" "+ name + ", new : " + clazz.getName());
							return false;
						}
						cmdCache.put(cmd.code(), (Command) clazz.newInstance());
						continue;
					}

					AdminCmd adminCmd = clazz.getAnnotation(AdminCmd.class);
					if (adminCmd != null) {
						if (adminCmdCache.get(adminCmd.code()) != null) {
							String name = adminCmdCache.get(adminCmd.code()).getClass().getName();
							LogUtil.error("CmdId冲突, " + adminCmd.code() +" "+ name + ", new : " + clazz.getName());
							return false;
						}
						adminCmdCache.put(adminCmd.code(), (AdminCommand) clazz.newInstance());
						continue;
					}
				} catch (Exception e) {
					LogUtil.error("加载CmdId失败 : " + clazz.getName(), e);
					e.printStackTrace();
				}
			}

			LogUtil.info("cmdCache size : " + cmdCache.size());
			LogUtil.info("adminCmdCache size : " + adminCmdCache.size());
			return true;
		} catch (Exception e) {
			LogUtil.error("命令管理器解析错误", e);
			return false;
		}
	}

	/**
	 * 缓存中获取命令
	 * @param msgId
	 * @return
	 */
	public static Command getCommand(int msgId) {
		return cmdCache.get(msgId);
	}
	
	public static AdminCommand getCommand(String msgId) {
		return adminCmdCache.get(msgId);
	}

	public static Map<String, AdminCommand> getAllAdminCommand() {
		return adminCmdCache;
	}
}
