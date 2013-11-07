package kr.co.adflow.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import kr.co.adflow.netty.handler.BroadCastHandler;

/**
 * HubNSpokeServer
 * 
 * @author typark@adflow.co.kr
 * @version 0.0.1
 */
public class HubNSpokeServer {

	private int[] port = { 3883, 4883 };

	public void run() throws Exception {
		// Configure the server.
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ChannelFuture f = null;
			for (int i = 0; i < port.length; i++) {
				ServerBootstrap boot = new ServerBootstrap();
				boot.group(bossGroup, workerGroup)
						.channel(NioServerSocketChannel.class)
						.option(ChannelOption.SO_BACKLOG, 100)
						.handler(new LoggingHandler(LogLevel.DEBUG))
						.childHandler(new ChannelInitializer<SocketChannel>() {
							@Override
							public void initChannel(SocketChannel ch)
									throws Exception {
								ch.pipeline().addLast(
								// new LoggingHandler(LogLevel.INFO),
										new BroadCastHandler());
							}
						});

				// Start the server.
				f = boot.bind(port[i]).sync();
			}
			// Wait until the server socket is closed.
			f.channel().closeFuture().sync();
		} finally {
			// Shut down all event loops to terminate all threads.
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		new HubNSpokeServer().run();
	}
}
