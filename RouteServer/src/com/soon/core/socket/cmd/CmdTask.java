package com.soon.core.socket.cmd;

import com.soon.core.socket.conn.bean.MessageObject;
import com.soon.util.LogUtil;

import io.netty.channel.Channel;

/**
 * 将请求命令包装为线程任务
 * @author songlin
 */
public class CmdTask implements Runnable {

	private Channel channle;
	private MessageObject msgObj;
	private Command cmd;

	public CmdTask(Command cmd, Channel channle, MessageObject msgObj) {
		this.channle = channle;
		this.msgObj = msgObj;
		this.cmd = cmd;
	}

	@Override
	public void run() {
		try {
			long start = System.currentTimeMillis();
			
			cmd.execute(channle, msgObj);
			
			long usedTime = System.currentTimeMillis() - start;
			if(usedTime > 2000){
				LogUtil.warn("执行Command时间异常,CMD:"+cmd.toString()+" usedTime:"+usedTime);
			}
		} catch (Exception e) {
			LogUtil.error("执行 command 异常, CMD : " + cmd.toString() + ", msgObj : " + msgObj.toString(), e);
		}
	}
}
