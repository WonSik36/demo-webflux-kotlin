package me.wonsik.webflux.kotlin

import io.r2dbc.spi.ConnectionFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer

import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory

import org.springframework.context.annotation.Bean
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebHandler
import reactor.core.publisher.Hooks
import reactor.core.publisher.Mono
import reactor.netty.http.server.HttpServer
import reactor.netty.resources.LoopResources
import reactor.netty.tcp.TcpResources


@SpringBootApplication
class WebfluxKotlinApplication {

//  @Bean
  fun nettyServerCustomizer() : NettyServerCustomizer {
    return NettyServerCustomizer { server: HttpServer ->
      server.runOn(LoopResources.create("myEventLoop", 1, true))
    }
  }

//  @Bean
  fun webHandler() : WebHandler {
    return WebHandler { TODO("Not yet implemented") }
  }
}

fun main(args: Array<String>) {
  Hooks.onOperatorDebug()
  runApplication<WebfluxKotlinApplication>(*args)
}
