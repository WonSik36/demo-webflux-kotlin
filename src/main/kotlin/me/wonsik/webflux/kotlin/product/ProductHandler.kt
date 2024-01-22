package me.wonsik.webflux.kotlin.product

import kotlinx.coroutines.delay
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.web.reactive.function.server.*


/**
 * @author 정원식 (wonsik.cheung)
 */
class ProductHandler (private val productRepository: ProductRepository) {

  suspend fun getProducts(request: ServerRequest) : ServerResponse {
    val products = productRepository.findAll().asFlow()
    return ServerResponse.ok().bodyAndAwait(products)
  }

  suspend fun getProduct(request: ServerRequest) : ServerResponse {
    val productId = request.pathVariable("id").toLong()
    val product = productRepository.findById(productId).awaitFirstOrNull()

    delay(3000)

    return product?.let {
      ServerResponse.ok().bodyValueAndAwait(product)
    } ?: ServerResponse.notFound().buildAndAwait()
  }
}