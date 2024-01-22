package me.wonsik.webflux.kotlin.product

import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import java.time.Duration


/**
 * @author 정원식 (wonsik.cheung)
 */
@RequestMapping("/reactive")
@RestController
class ProductReactiveController (private val productRepository: ProductRepository) {

  private val logger = org.slf4j.LoggerFactory.getLogger(this.javaClass)

  @GetMapping("/products")
  fun getProducts() : Flux<Product> {
    return productRepository.findAll()
  }

  @GetMapping("/products/{id}")
  fun getProduct(@PathVariable id: Long) : Mono<Product> {

    return Mono.delay(Duration.ofSeconds(3))
      .flatMap {
        productRepository.findById(id)
      }
  }

  @GetMapping("/webclient")
  fun webclient() : Mono<String> {
    val httpClient = HttpClient.create()

    val webClient = WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .build()

    return webClient.get().uri("https://www.hangame.com")
            .retrieve()
            .bodyToMono(String::class.java)
  }
}