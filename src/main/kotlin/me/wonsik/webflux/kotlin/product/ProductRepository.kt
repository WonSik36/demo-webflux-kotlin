package me.wonsik.webflux.kotlin.product

import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Mono


/**
 * @author 정원식 (wonsik.cheung)
 */
interface ProductRepository : R2dbcRepository<Product, Long> {
    fun findByName(name: String): Mono<Product>
}
