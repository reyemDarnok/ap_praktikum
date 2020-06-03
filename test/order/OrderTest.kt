package order

import groups.ShoppingCart
import org.junit.Test
import product.InfiniteProduct
import kotlin.test.assertTrue

class OrderTest {
    @Test
    fun `slightly larger`() {
        val address = Address("", "", "", "")
        val smallCart = ShoppingCart().apply {
            addProduct(InfiniteProduct("", 0.1, 0.1, ""), 1)
        }
        val largeCart = ShoppingCart().apply {
            addProduct(InfiniteProduct("", 0.1, 0.2, ""), 1)
        }
        val smaller = Order(smallCart, address)
        val larger = Order(largeCart, address)
        assertTrue(smaller < larger)
    }

    @Test
    fun `slightly smaller`() {
        val address = Address("", "", "", "")
        val smallCart = ShoppingCart().apply {
            addProduct(InfiniteProduct("", 0.1, 0.1, ""), 1)
        }
        val largeCart = ShoppingCart().apply {
            addProduct(InfiniteProduct("", 0.1, 0.2, ""), 1)
        }
        val smaller = Order(smallCart, address)
        val larger = Order(largeCart, address)
        assertTrue(larger > smaller)
    }

    @Test
    fun `significant difference`() {
        val address = Address("", "", "", "")
        val smallCart = ShoppingCart().apply {
            addProduct(InfiniteProduct("", 0.1, 0.1, ""), 1)
        }
        val largeCart = ShoppingCart().apply {
            addProduct(InfiniteProduct("", 0.1, 2000.0, ""), 10000)
        }
        val smaller = Order(smallCart, address)
        val larger = Order(largeCart, address)
        assertTrue(larger > smaller)
    }
}