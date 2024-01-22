package me.wonsik.webflux.kotlin.product

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import reactor.blockhound.BlockHound
import reactor.core.publisher.Mono
import java.time.Duration


/**
 * @author 정원식 (wonsik.cheung)
 */
@SpringBootTest
class ProductRepositoryTest {

  @Autowired
  private lateinit var productRepository: ProductRepository

  @Test
  fun `test`() {
    BlockHound.install()
    productRepository.findById(1L)
      .delayElement(Duration.ofSeconds(5))
      .doOnSubscribe { println("doOnSubscribe") }
      .doOnNext { println(it) }
      .block()
  }
}