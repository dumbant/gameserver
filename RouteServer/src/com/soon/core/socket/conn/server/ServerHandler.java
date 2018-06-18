package com.soon.core.socket.conn.server;

import com.soon.core.socket.cmd.CmdMgr;
import com.soon.core.socket.conn.bean.MessageObject;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("ServerHandler:"+Thread.currentThread().getName());
		if(msg != null){
			MessageObject msgObj= (MessageObject)msg;
			System.out.println(msgObj.toString());
			
			CmdMgr.addCmd(ctx.channel(), msgObj);
		}
	}
	
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
    	System.out.println("channelReadComplete:"+ctx.name()+" "+ctx.channel().toString());
//    	short codeId = 1002;
//    	MessageObject msgObj=new MessageObject(codeId,2000,"收到信息，返回数据".getBytes());
//    	ctx.channel().writeAndFlush(msgObj);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	System.out.println("exceptionCaught"+ctx.name()+" "+ctx.channel().toString());
        cause.printStackTrace();
        ctx.close();
    }

}
