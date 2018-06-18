package com.soon.core.socket.cmd;

import com.soon.core.socket.conn.bean.MessageObject;

import io.netty.channel.Channel;

public interface Command {

	public abstract void execute(Channel channel, MessageObject msgObj) throws Exception;
}
