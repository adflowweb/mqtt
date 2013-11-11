package kr.co.adflow.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * BroadCastHandler
 * 
 * @author typark@adflow.co.kr
 * @version 0.0.1
 */
@Sharable
public class BroadCastHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = Logger
			.getLogger(BroadCastHandler.class.getName());

	private static Vector<ChannelHandlerContext> v = new Vector<ChannelHandlerContext>();

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		try {
			Iterator it = v.iterator();
			for (; it.hasNext();) {
				ChannelHandlerContext c = (ChannelHandlerContext) it.next();
				if (c != ctx) {
					ByteBuf b = Unpooled.copiedBuffer((ByteBuf) msg);
					c.writeAndFlush(b);
				}
			}
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		System.out.println("=========================================================");
		// Close the connection when an exception is raised.
		logger.log(Level.WARNING, "Unexpected exception from downstream.",
				cause);
		ctx.close();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		v.add(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		v.remove(ctx);
	}
}
