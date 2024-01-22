package me.wonsik.netty

import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.ByteBuf
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.util.ReferenceCountUtil


/**
 * @author 정원식 (wonsik.cheung)
 */

class DiscardServerHandler : ChannelInboundHandlerAdapter() {
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        val `in` = msg as ByteBuf
        try {
            while (`in`.isReadable) { // (1)
                print(Char(`in`.readByte().toUShort()))
                System.out.flush()
            }
        } finally {
            ReferenceCountUtil.release(msg) // (2)
        }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        // Close the connection when an exception is raised.
        cause.printStackTrace()
        ctx.close()
    }
}


class DiscardServer(private val port: Int) {
    @Throws(Exception::class)
    fun run() {
        val bossGroup: EventLoopGroup = NioEventLoopGroup() // (1)
        val workerGroup: EventLoopGroup = NioEventLoopGroup()
        try {
            val b = ServerBootstrap() // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel::class.java) // (3)
                    .childHandler(object : ChannelInitializer<SocketChannel>() {
                        // (4)
                        @Throws(Exception::class)
                        override fun initChannel(ch: SocketChannel) {
                            ch.pipeline().addLast(DiscardServerHandler())
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128) // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // (6)

            // Bind and start to accept incoming connections.
            val f: ChannelFuture = b.bind(port).sync() // (7)

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync()
        } finally {
            workerGroup.shutdownGracefully()
            bossGroup.shutdownGracefully()
            println("Server Shutdown Gracefully")
        }
    }

    companion object {
        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            var port = 8080
            if (args.size > 0) {
                port = args[0].toInt()
            }
            DiscardServer(port).run()
        }
    }
}