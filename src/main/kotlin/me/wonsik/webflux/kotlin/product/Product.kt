package me.wonsik.webflux.kotlin.product

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table


/**
 * @author 정원식 (wonsik.cheung)
 */
@Table
data class Product (
  @Id val id: Long? = null,
  @Column val name: String,
  @Column val price: Int
) {

  override fun toString(): String {
    return "Product(id=$id, name='$name', price=$price)"
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Product

    if (id != other.id) return false
    if (name != other.name) return false
    if (price != other.price) return false

    return true
  }

  override fun hashCode(): Int {
    var result = id?.hashCode() ?: 0
    result = 31 * result + name.hashCode()
    result = 31 * result + price
    return result
  }
}