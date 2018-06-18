package com.soon.core.socket.conn.client;

import com.soon.core.socket.conn.bean.Netty4LinkInfo;
import com.soon.core.socket.conn.codec.Netty4DataDecode;
import com.soon.core.socket.conn.codec.Netty4DataEncode;
import com.soon.core.socket.conn.util.MsgUtil;
import com.soon.util.LogUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class Netty4Client {
	
	public static void main(String[] args) {
		//initSocket();
	}

	public static void initSocket(Netty4LinkInfo linkInfo) {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			LogUtil.info("开始连接:"+linkInfo.getIp()+":"+linkInfo.getPort());
            Bootstrap b = new Bootstrap();
            b.group(group)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY, true)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline p = ch.pipeline();
                    /* 参数说明：
					  * 1：数据包最大长度
					  * 2:len开始位置,第一节字节开始就是0
					  * 3:len占据的字节数,这里用的是short,占2个字节
					  * 4:len值与buf中len位置后的数据的长度之间的差，如len=100,是表示整个buf的长度,那么len字节位置后面的真实长度是100-2(len占的长度)
					  * 5:返回的结果从什么位置开始，0表示从头开始返回整个buf，2：表示去掉开始的两个字节(len)，只返回数据体
					  */
					 p.addLast(new LengthFieldBasedFrameDecoder(MsgUtil.MAXLEN,0,2,-2,0));
					 p.addLast(new Netty4DataEncode());
					 p.addLast(new Netty4DataDecode());
					 p.addLast(new ClientHandler(linkInfo));
                }
            });

            ChannelFuture f = b.connect(linkInfo.getIp(), linkInfo.getPort()).sync();
           
            linkInfo.setGroup(group);
            linkInfo.setChannel(f.channel());
            linkInfo.setActive(true);
       		linkInfo.setTryCount(0);
       		linkInfo.setRefrshTime(System.currentTimeMillis());
       	
       		LogUtil.info("连接成功:"+linkInfo.getIp()+":"+linkInfo.getPort());
		}catch(Exception e){
			LogUtil.error("连接失败 IP:"+linkInfo.getIp()+":"+linkInfo.getPort(),e);
			linkInfo.setTryCount(linkInfo.getTryCount()+1);
			group.shutdownGracefully();
		}  
	}
}
