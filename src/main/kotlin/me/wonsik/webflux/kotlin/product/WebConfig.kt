package me.wonsik.webflux.kotlin.product

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.function.server.coRouter


/**
 * @author 정원식 (wonsik.cheung)
 */
@Configuration
//@EnableWebFlux
class WebConfig (
  private val productRepository: ProductRepository
) : WebFluxConfigurer {

  @Bean
  fun route() = coRouter {
    val productHandler = ProductHandler(productRepository)

    "/router/products".nest {
      GET("/", productHandler::getProducts)
      GET("/{id}", productHandler::getProduct)
    }
  }
}