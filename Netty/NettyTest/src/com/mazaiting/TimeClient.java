package com.mazaiting;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

	public class TimeClient {
		public static void main(String[] args) throws InterruptedException {
			String host = "127.0.0.1";
			int post = 8083;
			EventLoopGroup workerGroup = new NioEventLoopGroup();
			
			try {
				Bootstrap bootstrap = new Bootstrap();
				bootstrap
					.group(workerGroup)
					.channel(NioSocketChannel.class)
					.option(ChannelOption.SO_KEEPALIVE, true)
					.handler(new ChannelInitializer<SocketChannel>() {
	
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new TimeClientHandler());
						}
					});
				// 开启客户端
				ChannelFuture future = bootstrap.connect(host, post).sync();
				
				// 等待直到连接关闭
				future.channel().closeFuture().sync();
			} finally {
				workerGroup.shutdownGracefully();
			}
		}
	}
