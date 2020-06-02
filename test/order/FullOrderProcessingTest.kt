package order

import groups.ShoppingCart
import org.junit.Before
import org.junit.Test
import product.InfiniteProduct
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FullOrderProcessingTest {
    var list = OrderProcessing()

    @Before
    fun init() {
        list = OrderProcessing()
    }

    private fun cart(index: Int): ShoppingCart {
        return ShoppingCart().apply {
            addProduct(InfiniteProduct("", 0.1, index.toDouble(), ""), index)
        }
    }

    private fun order(index: Int): Order {
        val address = Address("", "", "", "")
        return Order(cart(index), address)
    }

    @Test
    fun `check empty true`() {
        assertTrue(list.isEmpty)
    }

    @Test
    fun `check empty false`() {
        list.append(order(0))
        assertFalse(list.isEmpty)
    }

    @Test
    fun `is empty list sorted`() {
        assertTrue(list.isSorted())
    }

    @Test
    fun `is list with one element sorted`() {
        list.append(order(0))
        assertTrue(list.isSorted())
    }

    @Test
    fun `is long sorted list actually sorted`() {
        for (index in 0 downTo 10) {
            list.append(order(index))
        }
        assertTrue(list.isSorted())
    }

    @Test
    fun `is long unsorted list actually unsorted`() {
        for (index in 0..10) {
            list.append(order(index))
        }
        assertFalse(list.isSorted())
    }

    @Test
    fun `total volume of empty list`() {
        assertEquals(0.0, list.totalVolume)
    }

    @Test
    fun `total volume of list`() {
        list.append(order(3))
        list.append(order(4))
        assertEquals(25.0, list.totalVolume)
    }
}