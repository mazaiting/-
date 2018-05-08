package com.mazaiting;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeServerHandler extends ChannelInboundHandlerAdapter{

	/**
	 * 将会在连接被建立并且准备进行通信时被调用
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 申请4个字节
		final ByteBuf time = ctx.alloc().buffer(4);
		// 存入数据
		time.writeInt((int)(System.currentTimeMillis() / 1000L + 2208988800L));
		// 写入数据
		final ChannelFuture f = ctx.writeAndFlush(time);
		// 设置监听
		f.addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				assert f == future;
				ctx.close();
			}
		});
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// 打印错误信息
		cause.printStackTrace();
		// 关闭
		ctx.close();
	}
}
