package com.mazaiting;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * 忽略所有消息--继承ChannelHandlerAdapter
 * @author mazaiting
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// 忽略数据
		ByteBuf in = (ByteBuf) msg;
		try {
			System.out.println(in.toString(CharsetUtil.US_ASCII));
		} finally {
			ReferenceCountUtil.release(msg);
		}
		// 释放
//		ReferenceCountUtil.release(msg);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// 打印错误信息
		cause.printStackTrace();
		// 关闭
		ctx.close();
	}
	
}
