package com.soon.core.socket.conn.codec;

import java.util.List;

import com.soon.core.socket.conn.bean.MessageObject;
import com.soon.core.socket.conn.util.MsgUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class Netty4DataDecode extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		//System.out.println("Netty4DataDecode:"+Thread.currentThread().getName());
		MessageObject obj = MsgUtil.getMessageObject(in);
		out.add(obj);
	}

}
