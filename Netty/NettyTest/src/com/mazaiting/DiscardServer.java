package com.mazaiting;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 忽略消息的服务器
 * @author mazaiting
 */
public class DiscardServer {
	/**端口*/
	private int port;
	
	public DiscardServer(int port) {
		this.port = port;
	}
	
	public void run() throws Exception{
		// 创建处理I/O操作的多线程事件循环器
		// 接收进来的连接
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		// 处理已经被接收的连接
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			// 启动NIO服务的辅助启动类
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap
				// 设置组
				.group(bossGroup, workerGroup)
				// 设置管道
				.channel(NioServerSocketChannel.class)
				// 设置过滤
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						// 设置过滤
						ch.pipeline().addLast(new DiscardServerHandler());
					}					
				})
				// 后台日志
				.option(ChannelOption.SO_BACKLOG, 128)
				// 保持存活
				.childOption(ChannelOption.SO_KEEPALIVE, true);
				
				// 绑定端口并开启服务器
				ChannelFuture future = bootstrap.bind(port).sync();
				// 等待知道这个服务器关闭
				future.channel().closeFuture().sync();
		} finally {
			// 关闭
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}		
	}
	
	public static void main(String[] args) throws Exception {
		int port = 8081;
		new DiscardServer(port).run();
	}
}
