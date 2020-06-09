package groups

import product.InfiniteProduct
import product.Product

class MockCart(override val totalPrice: Double) : ShoppingCart() {
    var boughtTimes = 0
    override val productAndQuantityList: MutableList<Pair<Product, Int>> = mutableListOf(Pair(InfiniteProduct(), 1))

    override fun buyEverything(): Double {
        boughtTimes++
        return totalPrice
    }

    override fun equals(other: Any?): Boolean {
        if (other is MockCart) {
            return totalPrice == other.totalPrice
        }
        return false
    }

    override fun hashCode(): Int {
        return totalPrice.hashCode()
    }
}