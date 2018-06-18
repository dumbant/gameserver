/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.soon.core.socket.conn.client;

import com.soon.core.socket.conn.bean.MessageObject;
import com.soon.core.socket.conn.bean.Netty4LinkInfo;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {

	private Netty4LinkInfo linkInfo;
	
    public ClientHandler(Netty4LinkInfo linkInfo) {
    	this.linkInfo = linkInfo;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
//    	short codeId = 1000;
//    	MessageObject msgObj = new MessageObject(codeId, 20000, "Connect To Server".getBytes());
//    	ByteBuf firstMessage = MsgUtil.getByteBuf(msgObj);
//    	
//    	//System.out.println("channelActive:"+ctx.name()+" "+ctx.channel().toString()+" "+msgObj.toString());
//        ctx.writeAndFlush(firstMessage);
        
//        new Thread(new TestBlock(ctx.channel())).start();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
    	System.out.println("channelRead:"+msg.toString()+" "+ctx.name());
//    	short codeId = 10002;
//    	MsgObj msgObj = new MsgObj(codeId, 20000, "Test Socket".getBytes());
        //ctx.write(msgObj);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
    	//System.out.println("channelReadComplete:"+ctx.name()+" "+ctx.channel().toString());
       ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
    	//System.out.println("exceptionCaught:"+ctx.name()+" "+ctx.channel().toString());
    	
        cause.printStackTrace();
        ctx.close();
        
        linkInfo.disConnect();
    }
    
    class TestBlock implements Runnable{
    	Channel channel ;
    	
    	public TestBlock(Channel channel){
    		this.channel = channel;
    	}
    	
		@Override
		public void run() {
			
			int i=1;
			for(;;){
				short codeId = (short)(20+i);
				MessageObject msgObj = new MessageObject(codeId, 20000+i, ("Test Block "+i).getBytes());
				channel.writeAndFlush(msgObj);
				try {
					i++;
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
    	
    }
}
