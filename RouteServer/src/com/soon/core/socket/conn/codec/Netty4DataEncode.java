package com.soon.core.socket.conn.codec;

import com.soon.core.socket.conn.bean.MessageObject;
import com.soon.core.socket.conn.util.MsgUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class Netty4DataEncode extends MessageToByteEncoder<MessageObject> {

	@Override
	protected void encode(ChannelHandlerContext ctx, MessageObject msg, ByteBuf out)
			throws Exception {
		//System.out.println("Netty4DataEncode:"+Thread.currentThread().getName());
		//System.out.println(msg.toString());
		
		out.writeBytes(MsgUtil.getByteBuf(msg));
		ctx.write(out);
		ctx.flush();
		out.release();
	}

}
