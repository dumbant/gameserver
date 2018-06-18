package com.soon.core.socket.conn.server;

import com.soon.core.config.Config;
import com.soon.core.config.ServerInfo;
import com.soon.core.socket.conn.codec.Netty4DataDecode;
import com.soon.core.socket.conn.codec.Netty4DataEncode;
import com.soon.core.socket.conn.util.MsgUtil;
import com.soon.util.LogUtil;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Netty4Server implements IServerSocket{
	private EventLoopGroup bossGroup ;
	private EventLoopGroup workerGroup;
	
	public static void main(String[] args) {
		new Netty4Server().initServer();
	}
	
	public void initServer(){
		bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        
        try {
        	ServerInfo serverInfo = Config.getServerConfig(Config.serverName);
        	
        	 ServerBootstrap b = new ServerBootstrap();
             b.group(bossGroup, workerGroup)
              .channel(NioServerSocketChannel.class)
              .option(ChannelOption.SO_BACKLOG, 100)
              .handler(new LoggingHandler(LogLevel.INFO))
              .childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						 ChannelPipeline p = ch.pipeline();
						 
						 p.addLast(new LoggingHandler(LogLevel.INFO));

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
						 p.addLast(new ServerHandler());
						 /*
						  * 1.使用一个新线程运行ServerHandler，ServerHandler会绑定这一个线程，
						  * 如果ServerHandler发生阻塞，pipeline仍然接收数据，但数据会在ServerHandler中阻塞。
						  * 
						  * 2.不使用新线程，如果ServerHandler发生阻塞，就会阻塞整个pipeline无法接收新数据
						  * 
						  * 总结：对于服务端来说，这里使用新线程意义不大。Handler不能处理需要长时间的任务，影响并发性能。
						  * 所以还是需要在ServerHandler中接收到数据时，开启新线程处理逻辑，使Handler不会发生阻塞.
						  */
						 //EventExecutor executor = new DefaultEventExecutor();
						 //p.addLast(executor,new ServerHandler());
					}
              	});
              
             // Start the server.
             b.bind(serverInfo.getPort()).sync();
             LogUtil.info("开始服务成功："+serverInfo.getPort());
        }catch(Exception e){
        	LogUtil.error("启动服务失败",e);
        	close();
        }
	}
	
	public void close(){
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
		bossGroup = null;
		workerGroup = null;
	}
}
