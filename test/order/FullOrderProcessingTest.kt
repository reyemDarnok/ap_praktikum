package order

import groups.ShoppingCart
import order.OrderProcessing.OrderNode
import org.junit.Before
import org.junit.Test
import product.InfiniteProduct
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FullOrderProcessingTest {
    var list = OrderProcessing()
    var carts = mutableListOf<ShoppingCart>()

    @Before
    fun init() {
        list = OrderProcessing()
    }

    private fun cart(index: Int): ShoppingCart {
        if (index !in carts.indices) {
            for (i in carts.size..index) {
                carts.add(i,
                        ShoppingCart().apply {
                            addProduct(InfiniteProduct("", 0.1, index.toDouble(), ""), i)
                        }
                )
            }
        }
        return carts[index]
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

    @Test
    fun `size of empty list`() {
        assertEquals(0, list.size)
    }

    @Test
    fun `size of filled list`() {
        list.append(order(10))
        list.append(order(7))
        assertEquals(2, list.size)
    }

    @Test
    fun `append on empty list`() {
        list.append(order(1))
        assertEquals(OrderNode(order(1), null), list.first)
    }

    @Test
    fun `append twice on empty list`() {
        list.append(order(5))
        list.append(order(3))
        val expected = OrderNode(order(5),
                OrderNode(order(3), null))
        assertEquals(expected, list.first)
    }

    @Test
    fun `insert sorted on empty list`() {
        list.insertBeforeSmallerVolumes(order(5))
        val expected = OrderNode(order(5), null)
        assertEquals(expected, list.first)
    }

    @Test
    fun `insert sorted start of list`() {
        list.append(order(0))
        list.insertBeforeSmallerVolumes(order(10))
        val expected = OrderNode(order(10),
                OrderNode(order(0), null))
        assertEquals(expected, list.first)
    }

    @Test
    fun `insert sorted end of list`() {
        list.append(order(10))
        list.insertBeforeSmallerVolumes(order(0))
        val expected = OrderNode(order(10),
                OrderNode(order(0), null))
        assertEquals(expected, list.first)
    }

    @Test
    fun `insert sorted mid of list`() {
        list.append(order(10))
        list.append(order(0))
        list.insertBeforeSmallerVolumes(order(5))
        val expected = OrderNode(order(10),
                OrderNode(order(5),
                        OrderNode(order(0), null)))
        assertEquals(expected, list.first)
    }

    @Test
    fun `insert despite larger further back`() {
        list.append(order(0))
        list.append(order(10))
        list.insertBeforeSmallerVolumes(order(5))
        val expected = OrderNode(order(5),
                OrderNode(order(0),
                        OrderNode(order(10), null)))
        assertEquals(expected, list.first)
    }

    @Test
    fun `sort sorted list`() {
        list.append(order(10))
        list.append(order(5))
        list.append(order(0))
        list.sortByVolume()
        val expected = OrderNode(order(10),
                OrderNode(order(5),
                        OrderNode(order(0), null)))
        assertEquals(expected, list.first)
    }

    @Test
    fun `sort reversed list`() {
        list.append(order(0))
        list.append(order(5))
        list.append(order(10))
        list.sortByVolume()
        val expected = OrderNode(order(10),
                OrderNode(order(5),
                        OrderNode(order(0), null)))
        assertEquals(expected, list.first)
    }

    @Test
    fun `sort scrambled list`() {
        list.append(order(5))
        list.append(order(10))
        list.append(order(0))
        list.sortByVolume()
        val expected = OrderNode(order(10),
                OrderNode(order(5),
                        OrderNode(order(0), null)))
        assertEquals(expected, list.first)
    }

    @Test
    fun `sort list with identical entries`() {
        list.append(order(10))
        list.append(order(5))
        list.append(order(5))
        list.append(order(0))
        list.sortByVolume()
        val expected = OrderNode(order(10),
                OrderNode(order(5),
                        OrderNode(order(5),
                                OrderNode(order(0), null))))
        assertEquals(expected, list.first)
    }

    @Test
    fun `process first and only`() {
        list.append(order(5))
        list.processFirst()
        assertNull(list.first)
    }

    @Test
    fun `process first with following`() {
        list.append(order(5))
        list.append(order(3))
        list.processFirst()
        assertEquals(OrderNode(order(3), null), list.first)
    }
}