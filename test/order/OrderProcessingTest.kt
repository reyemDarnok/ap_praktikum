package order

import groups.ShoppingCart
import order.OrderProcessing.OrderNode
import org.junit.Assert.*
import org.junit.Test
import product.InfiniteProduct

class OrderProcessingTest {

    private val emptyAddress = Address("", "", "", "")
    private val emptyOrder = Order(ShoppingCart(), emptyAddress)

    private fun shoppingCarts(): Triple<ShoppingCart, ShoppingCart, ShoppingCart> {
        val cart = ShoppingCart().apply {
            addProduct(InfiniteProduct("", 1.0, 2.0, ""), 5) // 10
            addProduct(InfiniteProduct("", 2.0, 2.5, ""), 5) // 12.5 => 22.5
        }

        val cart2 = ShoppingCart().apply {
            addProduct(InfiniteProduct("", 1.0, 3.0, ""), 5) // 15
        }

        val cart3 = ShoppingCart().apply {
            addProduct(InfiniteProduct("", 1.0, 3.5, ""), 5) // 17.5
        }

        return Triple(cart, cart2, cart3)
    }

    @Test
    fun `fresh created list should be empty`() {
        val list = OrderProcessing()

        assertTrue(list.isEmpty)
    }

    @Test
    fun `list is not empty if it has at least one item`() {
        val list = OrderProcessing()
        list.first = OrderNode(Order(ShoppingCart(), emptyAddress), null)

        assertFalse(list.isEmpty)
    }

    @Test
    fun `empty list is sorted`() {
        val list = OrderProcessing()

        assertTrue(list.isSorted())
    }

    @Test
    fun `sorted lists are recognized as such`() {
        val list = OrderProcessing()

        val cart = ShoppingCart().apply {
            addProduct(InfiniteProduct("", 1.0, 2.0, ""), 5) // 10
            addProduct(InfiniteProduct("", 2.0, 2.5, ""), 5) // 12.5
        }

        val cart2 = ShoppingCart().apply {
            addProduct(InfiniteProduct("", 1.0, 3.0, ""), 5) // 15
        }

        list.first = OrderNode(
                Order(cart, emptyAddress),
                OrderNode(
                        Order(cart2, emptyAddress),
                        null
                )
        )

        assertTrue(list.isSorted())
    }

    @Test
    fun `non sorted lists are recognized as such`() {
        val list = OrderProcessing()

        val cart = ShoppingCart().apply {
            addProduct(InfiniteProduct("", 1.0, 2.0, ""), 5) // 10
            addProduct(InfiniteProduct("", 2.0, 2.5, ""), 5) // 12.5
        }

        val cart2 = ShoppingCart().apply {
            addProduct(InfiniteProduct("", 1.0, 3.0, ""), 5) // 15
        }

        list.first = OrderNode(
                Order(cart2, emptyAddress),
                OrderNode(
                        Order(cart, emptyAddress),
                        null
                )
        )

        assertFalse(list.isSorted())
    }

    @Test
    fun `calculates total order volume properly`() {
        val list = OrderProcessing()

        val cart = ShoppingCart().apply {
            addProduct(InfiniteProduct("", 1.0, 2.0, ""), 5) // 10
            addProduct(InfiniteProduct("", 2.0, 2.5, ""), 5) // 12.5
        }

        val cart2 = ShoppingCart().apply {
            addProduct(InfiniteProduct("", 1.0, 3.0, ""), 5) // 15
        }

        list.first = OrderNode(
                Order(cart, emptyAddress),
                OrderNode(
                        Order(cart2, emptyAddress),
                        null
                )
        )

        assertEquals(37.5, list.totalVolume, 0.0)
    }

    @Test
    fun `size is zero if list is empty`() {
        val list = OrderProcessing()
        assertEquals(0, list.size)
    }

    @Test
    fun `size is one if list has one element`() {
        val list = OrderProcessing().apply {
            first = OrderNode(Order(ShoppingCart(), emptyAddress), null)
        }

        assertEquals(1, list.size)
    }

    @Test
    fun `size is n if list has n elements`() {
        val list = OrderProcessing().apply {
            first = OrderNode(
                    emptyOrder,
                    OrderNode(
                            emptyOrder,
                            OrderNode(
                                    emptyOrder,
                                    null
                            )
                    )
            )
        }

        assertEquals(3, list.size)
    }

    @Test
    fun `append adds order to the end of the list`() {
        val (cart, cart2, cart3) = shoppingCarts()

        val order1 = Order(cart, emptyAddress)
        val order2 = Order(cart2, emptyAddress)
        val order3 = Order(cart3, emptyAddress)

        val list = OrderProcessing().apply {
            append(order1)
            append(order2)
            append(order3)
        }

        val expected = OrderNode(
                order1,
                OrderNode(
                        order2,
                        OrderNode(
                                order3,
                                null
                        )
                )
        )

        assertEquals(expected, list.first)
    }

    @Test
    fun `insert orders sorted before smaller ones`() {
        val (cart, cart2, cart3) = shoppingCarts()

        val order1 = Order(cart, emptyAddress)
        val order2 = Order(cart2, emptyAddress)
        val order3 = Order(cart3, emptyAddress)

        val list = OrderProcessing().apply {
            insertBeforeSmallerVolumes(order1)
            insertBeforeSmallerVolumes(order2)
            insertBeforeSmallerVolumes(order3)
        }

        val expected = OrderNode(
                order1,
                OrderNode(
                        order3,
                        OrderNode(
                                order2,
                                null
                        )
                )
        )

        assertEquals(expected, list.first)
    }

    @Test
    fun `sort orders by volume (desc)`() {
        val (cart, cart2, cart3) = shoppingCarts()

        val order1 = Order(cart, emptyAddress)
        val order2 = Order(cart2, emptyAddress)
        val order3 = Order(cart3, emptyAddress)

        val list = OrderProcessing().apply {
            append(order1)
            append(order2)
            append(order3)
        }

        val before = OrderNode(
                order1,
                OrderNode(
                        order2,
                        OrderNode(
                                order3,
                                null
                        )
                )
        )

        val after = OrderNode(
                order1,
                OrderNode(
                        order3,
                        OrderNode(
                                order2,
                                null
                        )
                )
        )

        assertEquals(before, list.first)
        list.sortyByVolume()
        assertEquals(after, list.first)
    }

    @Test
    fun `remove the first order after calling processFirst`() {
        val (cart, cart2, cart3) = shoppingCarts()

        val order1 = Order(cart, emptyAddress)
        val order2 = Order(cart2, emptyAddress)
        val order3 = Order(cart3, emptyAddress)

        val list = OrderProcessing().apply {
            append(order1)
            append(order2)
            append(order3)
        }

        val before = OrderNode(
                order1,
                OrderNode(
                        order2,
                        OrderNode(
                                order3,
                                null
                        )
                )
        )

        val after = OrderNode(
                order2,
                OrderNode(
                        order3,
                        null
                )
        )

        assertEquals(before, list.first)
        list.processFirst()
        assertEquals(after, list.first)
    }

    @Test
    fun `remove the order with highest volume after calling processHighest`() {
        val (cart, cart2, cart3) = shoppingCarts()

        val order1 = Order(cart, emptyAddress)
        val order2 = Order(cart2, emptyAddress)
        val order3 = Order(cart3, emptyAddress)

        val list = OrderProcessing().apply {
            append(order2)
            append(order1)
            append(order3)
        }

        val before = OrderNode(
                order2,
                OrderNode(
                        order1,
                        OrderNode(
                                order3,
                                null
                        )
                )
        )

        val after = OrderNode(
                order2,
                OrderNode(
                        order3,
                        null
                )
        )

        assertEquals(before, list.first)
        list.processHighest()
        assertEquals(after, list.first)
    }

    @Test
    fun `remove all orders which are in given city after calling processAllFor`() {
        val (cart, cart2, cart3) = shoppingCarts()

        val gummersbach = Address("", "", "", "Gummersbach")
        val cologne = Address("", "", "", "Köln")

        val order1 = Order(cart, gummersbach)
        val order2 = Order(cart2, cologne)
        val order3 = Order(cart3, gummersbach)

        val list = OrderProcessing().apply {
            append(order1)
            append(order2)
            append(order3)
        }

        val before = OrderNode(
                order1,
                OrderNode(
                        order2,
                        OrderNode(
                                order3,
                                null
                        )
                )
        )

        val after = OrderNode(
                order2,
                null
        )

        assertEquals(before, list.first)
        list.processAllFor(gummersbach.city)
        assertEquals(after, list.first)
    }

    @Test
    fun `remove all orders after calling processAll`() {
        val (cart, cart2, cart3) = shoppingCarts()

        val order1 = Order(cart, emptyAddress)
        val order2 = Order(cart2, emptyAddress)
        val order3 = Order(cart3, emptyAddress)

        val list = OrderProcessing().apply {
            append(order1)
            append(order2)
            append(order3)
        }

        val before = OrderNode(
                order1,
                OrderNode(
                        order2,
                        OrderNode(
                                order3,
                                null
                        )
                )
        )

        assertEquals(before, list.first)
        list.processAll()
        assertNull(list.first)
    }


    @Test
    fun `analyze all orders by concatenating individual strings`() {
        val list = OrderProcessing().apply {
            append(Order(ShoppingCart(), Address("", "", "", "GM")))
            append(Order(ShoppingCart(), Address("", "", "", "K")))
            append(Order(ShoppingCart(), Address("", "", "", "HH")))
        }

        assertEquals("GMKHH", list.analyzeAll { it.address.city })
    }

    @Test
    fun `return empty string while analyzing if orders are empty`() {
        val list = OrderProcessing()
        assertEquals("", list.analyzeAll { it.address.city })
    }

    @Test
    fun `checks if product is in list`() {
        val (cart, cart2, cart3) = shoppingCarts()

        val order1 = Order(cart, emptyAddress)
        val order2 = Order(cart2, emptyAddress)
        val order3 = Order(cart3, emptyAddress)

        val list = OrderProcessing().apply {
            append(order1)
            append(order2)
            append(order3)
        }

        assertTrue(list.anyProduct { it.salesPrice == 3.0 })
        assertTrue(list.anyProduct { it.basePrice == 1.0 })
        assertFalse(list.anyProduct { it.basePrice > 100.0 })
        assertFalse(list.anyProduct { it.salesPrice > 100.0 })
    }

    @Test
    fun `filters list by order predicate`() {
        val (cart, cart2, cart3) = shoppingCarts()

        val order1 = Order(cart, emptyAddress)
        val order2 = Order(cart2, emptyAddress)
        val order3 = Order(cart3, emptyAddress)

        val list = OrderProcessing().apply {
            append(order1)
            append(order2)
            append(order3)
        }

        val expected = OrderProcessing().apply {
            append(order1)
            append(order3)
        }

        assertEquals(expected.first, list.filter { it.shoppingCart.totalPrice > 16.0 }.first)
        assertNull(list.filter { it.shoppingCart.totalPrice > 30.0 }.first)
    }
}