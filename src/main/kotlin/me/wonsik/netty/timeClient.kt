package me.wonsik.netty

import io.netty.bootstrap.Bootstrap
import io.netty.buffer.ByteBuf
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.ByteToMessageDecoder
import java.util.*


/**
 * @author 정원식 (wonsik.cheung)
 */

object TimeClient {
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val host = "127.0.0.1"
        val port = 8080
        val workerGroup: EventLoopGroup = NioEventLoopGroup()
        try {
            val b = Bootstrap() // (1)
            b.group(workerGroup) // (2)
            b.channel(NioSocketChannel::class.java) // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true) // (4)
            b.handler(object : ChannelInitializer<SocketChannel>() {
                @Throws(Exception::class)
                override fun initChannel(ch: SocketChannel) {
                    ch.pipeline().addLast(TimeDecoder(), TimeClientHandler())
                }
            })

            // Start the client.
            val f: ChannelFuture = b.connect(host, port).sync() // (5)

            // Wait until the connection is closed.
            f.channel().closeFuture().sync()
        } finally {
            workerGroup.shutdownGracefully()
        }
    }
}


class TimeClientHandler : ChannelInboundHandlerAdapter() {
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        val m = msg as UnixTime
        println(m)
        ctx.close()
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
    }
}


class TimeDecoder : ByteToMessageDecoder() {
    override fun decode(ctx: ChannelHandlerContext?, `in`: ByteBuf, out: MutableList<Any?>) { // (2)
        if (`in`.readableBytes() < 4) {
            return  // (3)
        }
        val time = UnixTime(`in`.readUnsignedInt())
        out.add(time)
    }
}


class UnixTime @JvmOverloads constructor(private val value: Long = System.currentTimeMillis() / 1000L + 2208988800L) {
    fun value(): Long {
        return value
    }

    override fun toString(): String {
        return Date((value() - 2208988800L) * 1000L).toString()
    }
}