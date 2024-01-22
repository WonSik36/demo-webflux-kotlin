package me.wonsik.webflux.kotlin.product

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.Thread.sleep
import kotlin.coroutines.coroutineContext


/**
 * @author 정원식 (wonsik.cheung)
 */
@RequestMapping("/coroutine")
@RestController
class ProductCoroutineController (private val productRepository: ProductRepository){

  private val log = org.slf4j.LoggerFactory.getLogger(this.javaClass)

  @FlowPreview
  @GetMapping("/products")
  fun getProducts() : Flow<Product> {
    return productRepository.findAll()
      .asFlow()
  }

  @GetMapping("/products/{id}")
  suspend fun getProduct(@PathVariable id: Long) : Product? {
    log.info("BEFORE CALL: $coroutineContext is executing on thread - ${Thread.currentThread().name}")

    delay(3000)

    return productRepository.findById(id)
      .awaitFirstOrNull()
      .also {
        log.info("AFTER CALL: $coroutineContext is executing on thread - ${Thread.currentThread().name}")
      }
  }
}