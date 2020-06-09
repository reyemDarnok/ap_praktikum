package order

import groups.MockCart
import groups.ShoppingCart
import order.OrderProcessing.OrderNode
import org.junit.Before
import org.junit.Test
import product.InfiniteProduct
import kotlin.test.*

class FullOrderProcessingTest {
    var list = OrderProcessing()
    var carts = mutableListOf<ShoppingCart>()

    @Before
    fun init() {
        list = OrderProcessing()
        carts.clear()
    }

    private fun cart(index: Int): ShoppingCart {
        carts.apply {
            if (index !in indices) {
                for (i in size..index) {
                    add(i,
                            MockCart(i.toDouble())
                    )
                }
            }
        }
        return carts[index]
    }

    private fun order(index: Int, city: String = ""): Order {
        val address = Address("", "", "", city)
        return Order(cart(index), address)
    }

    @Test
    fun `check empty true`() {
        assertTrue(list.isEmpty)
    }

    @Test
    fun `check empty false`() {
        list.apply {
            append(order(0))
            assertFalse(isEmpty)
        }
    }

    @Test
    fun `is empty list sorted`() {
        assertTrue(list.isSorted())
    }

    @Test
    fun `is list with one element sorted`() {
        list.apply {
            append(order(0))
            assertTrue(isSorted())
        }
    }

    @Test
    fun `is long sorted list actually sorted`() {
        list.apply {
            for (index in 0 downTo 10) {
                append(order(index))
            }
            assertTrue(isSorted())
        }
    }

    @Test
    fun `is long unsorted list actually unsorted`() {
        list.apply {
            for (index in 0..10) {
                append(order(index))
            }
            assertFalse(isSorted())
        }
    }

    @Test
    fun `total volume of empty list`() {
        assertEquals(0.0, list.totalVolume)
    }

    @Test
    fun `total volume of list`() {
        list.apply {
            append(order(3))
            append(order(4))
            assertEquals(7.0, totalVolume)
        }
    }

    @Test
    fun `size of empty list`() {
        assertEquals(0, list.size)
    }

    @Test
    fun `size of filled list`() {
        list.apply {
            append(order(10))
            append(order(7))
            assertEquals(2, size)
        }
    }

    @Test
    fun `append on empty list`() {
        list.apply {
            append(order(1))
            assertEquals(OrderNode(order(1), null), first)
        }
    }

    @Test
    fun `append twice on empty list`() {
        val expected = OrderNode(order(5),
                OrderNode(order(3), null))
        list.apply {
            append(order(5))
            append(order(3))
            assertEquals(expected, first)
        }
    }

    @Test
    fun `insert sorted on empty list`() {
        val expected = OrderNode(order(5), null)
        list.apply {
            insertBeforeSmallerVolumes(order(5))
            assertEquals(expected, first)
        }
    }

    @Test
    fun `insert sorted start of list`() {
        val expected = OrderNode(order(10),
                OrderNode(order(0), null))
        list.apply {
            append(order(0))
            insertBeforeSmallerVolumes(order(10))
            assertEquals(expected, first)
        }

    }

    @Test
    fun `insert sorted end of list`() {
        val expected = OrderNode(order(10),
                OrderNode(order(0), null))
        list.apply {
            append(order(10))
            insertBeforeSmallerVolumes(order(0))
            assertEquals(expected, first)
        }
    }

    @Test
    fun `insert sorted mid of list`() {
        val expected = OrderNode(order(10),
                OrderNode(order(5),
                        OrderNode(order(0), null)))
        list.apply {
            append(order(10))
            append(order(0))
            insertBeforeSmallerVolumes(order(5))
            assertEquals(expected, first)
        }
    }

    @Test
    fun `insert despite larger further back`() {
        val expected = OrderNode(order(5),
                OrderNode(order(0),
                        OrderNode(order(10), null)))
        list.apply {
            append(order(0))
            append(order(10))
            insertBeforeSmallerVolumes(order(5))
            assertEquals(expected, first)
        }
    }

    @Test
    fun `sort sorted list`() {
        val expected = OrderNode(order(10),
                OrderNode(order(5),
                        OrderNode(order(0), null)))
        list.apply {
            append(order(10))
            append(order(5))
            append(order(0))
            sortByVolume()
            assertEquals(expected, first)
        }

    }

    @Test
    fun `sort reversed list`() {
        val expected = OrderNode(order(10),
                OrderNode(order(5),
                        OrderNode(order(0), null)))
        list.apply {
            append(order(0))
            append(order(5))
            append(order(10))
            sortByVolume()
            assertEquals(expected, first)
        }
    }

    @Test
    fun `sort scrambled list`() {
        val expected = OrderNode(order(10),
                OrderNode(order(5),
                        OrderNode(order(0), null)))
        list.apply {
            append(order(5))
            append(order(10))
            append(order(0))
            sortByVolume()
            assertEquals(expected, first)
        }

    }

    @Test
    fun `sort list with identical entries`() {
        val expected = OrderNode(order(10),
                OrderNode(order(5),
                        OrderNode(order(5),
                                OrderNode(order(0), null))))

        list.apply {
            append(order(10))
            append(order(5))
            append(order(5))
            append(order(0))
            sortByVolume()
            assertEquals(expected, first)
        }
    }

    @Test
    fun `process first and only`() {
        list.apply {
            append(order(5))
            processFirst()
            assertEquals(1, (order(5).shoppingCart as MockCart).boughtTimes)
            assertNull(first)
        }
    }

    @Test
    fun `process first with following`() {
        list.apply {
            append(order(5))
            append(order(3))
            processFirst()
            assertEquals(1, (order(5).shoppingCart as MockCart).boughtTimes)
            assertEquals(0, (order(3).shoppingCart as MockCart).boughtTimes)
            assertEquals(OrderNode(order(3), null), first)
        }
    }

    @Test
    fun `process highest on empty list`() {
        list.apply {
            processHighest()
            assertNull(first)
        }
    }

    @Test
    fun `process highest and only`() {
        list.apply {
            append(order(10))
            processHighest()
            assertEquals(1, (order(10).shoppingCart as MockCart).boughtTimes)
            assertNull(first)
        }
    }

    @Test
    fun `process highest and first`() {
        val expected = OrderNode(order(0),
                OrderNode(order(5), null))
        list.apply {
            append(order(10))
            append(order(0))
            append(order(5))
            processHighest()
            assertEquals(1, (order(10).shoppingCart as MockCart).boughtTimes)
            assertEquals(0, (order(5).shoppingCart as MockCart).boughtTimes)
            assertEquals(0, (order(0).shoppingCart as MockCart).boughtTimes)
            assertEquals(expected, first)
        }
    }

    @Test
    fun `process highest and last`() {
        val expected = OrderNode(order(2),
                OrderNode(order(1), null))
        list.apply {
            append(order(2))
            append(order(1))
            append(order(5))
            processHighest()
            assertEquals(1, (order(5).shoppingCart as MockCart).boughtTimes)
            assertEquals(0, (order(2).shoppingCart as MockCart).boughtTimes)
            assertEquals(0, (order(1).shoppingCart as MockCart).boughtTimes)
            assertEquals(expected, first)
        }
    }

    @Test
    fun `process highest and middle`() {
        val expected = OrderNode(order(4),
                OrderNode(order(2), null))
        list.apply {
            append(order(4))
            append(order(5))
            append(order(2))
            processHighest()
            assertEquals(1, (order(5).shoppingCart as MockCart).boughtTimes)
            assertEquals(0, (order(4).shoppingCart as MockCart).boughtTimes)
            assertEquals(0, (order(2).shoppingCart as MockCart).boughtTimes)
            assertEquals(expected, first)
        }
    }

    @Test
    fun `process all for city no targets`() {
        val expected = OrderNode(order(4),
                OrderNode(order(2), null))
        list.apply {
            append(order(4))
            append(order(2))
            processAllFor("some city")
            assertEquals(0, (order(4).shoppingCart as MockCart).boughtTimes)
            assertEquals(0, (order(2).shoppingCart as MockCart).boughtTimes)
            assertEquals(expected, first)
        }

    }

    @Test
    fun `process all for city some targets`() {
        val expected = OrderNode(order(4),
                OrderNode(order(5), null))
        list.apply {
            append(order(2, "target"))
            append(order(4))
            append(order(3, "target"))
            append(order(5))
            processAllFor("target")
            assertEquals(1, (order(2).shoppingCart as MockCart).boughtTimes)
            assertEquals(1, (order(3).shoppingCart as MockCart).boughtTimes)
            assertEquals(0, (order(4).shoppingCart as MockCart).boughtTimes)
            assertEquals(0, (order(5).shoppingCart as MockCart).boughtTimes)
            assertEquals(expected, first)
        }
    }

    @Test
    fun `process all for city all targets`() {
        list.apply {
            append(order(2))
            append(order(1))
            processAllFor("")
            assertTrue(isEmpty)
            assertEquals(1, (order(2).shoppingCart as MockCart).boughtTimes)
            assertEquals(1, (order(1).shoppingCart as MockCart).boughtTimes)
        }
    }

    @Test
    fun `process all for city empty list`() {
        list.apply {
            processAllFor("")
            assertTrue(isEmpty)
        }
    }

    @Test
    fun `process all on empty list`() {
        list.apply {
            processAll()
            assertTrue(isEmpty)
        }
    }

    @Test
    fun `process all on filled list`() {
        list.apply {
            append(order(5))
            append(order(3))
            processAll()
            assertEquals(1, (order(5).shoppingCart as MockCart).boughtTimes)
            assertEquals(1, (order(3).shoppingCart as MockCart).boughtTimes)
            assertTrue(isEmpty)
        }
    }

    @Test
    fun `analyze all on empty list`() {
        assertEquals("", list.analyzeAll { it.toString() })
    }

    @Test
    fun `analyze all on filled list`() {
        val expected = "2|3|"
        list.apply {
            append(order(2))
            append(order(3))
            assertEquals(expected, analyzeAll { "%.0f".format(it.shoppingCart.totalPrice) + "|" })
        }
    }

    @Test
    fun `any on empty list`() {
        assertFalse(list.anyProduct { true })
    }

    @Test
    fun `any with false result`() {
        list.apply {
            append(order(3))
            append(order(2))
            assertFalse(anyProduct { it.productName == "not there" })
        }
    }

    @Test
    fun `any with all matches`() {
        list.apply {
            append(order(3))
            append(order(2))
            assertTrue(anyProduct { true })
        }
    }

    @Test
    fun `any with some matches`() {
        order(4).shoppingCart.addProduct(
                InfiniteProduct("name", 0.0, 0.0, ""), 5)
        list.apply {
            append(order(2))
            append(order(4))
            append(order(3))
            assertTrue(anyProduct { it.productName == "name" })
        }
    }

    @Test
    fun `filter on empty list`() {
        assertNull(list.filter { true }.first)
    }

    @Test
    fun `filter with false result`() {
        list.apply {
            append(order(3))
            append(order(2))
            assertNull(filter { false }.first)
        }
    }

    @Test
    fun `filter with all matches`() {
        val expected = OrderNode(order(3),
                OrderNode(order(2), null))
        list.apply {
            append(order(3))
            append(order(2))

            assertEquals(expected, filter { true }.first)
        }
    }

    @Test
    fun `filter with one match`() {
        order(4).shoppingCart.addProduct(
                InfiniteProduct("name", 0.0, 0.0, ""), 5)
        val expected = OrderNode(order(4), null)
        list.apply {
            append(order(2))
            append(order(4))
            append(order(3))
            assertEquals(expected, filter { it.shoppingCart.productAndQuantityList.any { pair -> pair.first.productName == "name" } }.first)
        }
    }

    @Test
    fun `filter is not identity preserving`() {
        list.apply {
            append(order(5))
            append(order(3))
            assertNotSame(this, filter { true })
        }
    }

    /**
     * List tests start here
     */

    @Test
    fun `removing first and only via iterator`() {
        list.apply {
            append(order(5))
            iterator().apply {
                next()
                remove()
            }
            assertNull(first)
        }
    }

    @Test(expected = IllegalStateException::class)
    fun `removing without accessing causes exception`() {
        list.iterator().remove()
    }

    @Test(expected = java.lang.IllegalStateException::class)
    fun `removing twice without accessing in between is not allowed`() {
        list.apply {
            append(order(1))
            append(order(2))
            append(order(3))
            append(order(4))
            iterator().apply {
                next()
                next()
                next()
                remove()
                remove()
            }
        }
    }

    @Test
    fun `removing first with following`() {
        list.apply {
            append(order(2))
            append(order(3))
            iterator().apply {
                next()
                remove()
            }
            assertEquals(OrderNode(order(3), null), first)
        }
    }

    @Test
    fun `removing from end`() {
        list.apply {
            append(order(2))
            append(order(3))
            iterator().apply {
                next()
                next()
                remove()
            }
            assertEquals(OrderNode(order(2), null), first)
        }
    }

    @Test
    fun `removing twice`() {
        val expected = OrderNode(order(1),
                OrderNode(order(4), null))
        list.apply {
            append(order(1))
            append(order(2))
            append(order(3))
            append(order(4))
            iterator().apply {
                next()
                next()
                remove()
                next()
                remove()
            }
            assertEquals(expected, first)
        }

    }

    @Test
    fun `contains on empty list`() {
        assertFalse(list.contains(order(0)))
    }

    @Test
    fun `contains on list with match`() {
        list.apply {
            append(order(1))
            assertTrue(contains(order(1)))
        }
    }

    @Test
    fun `contains on list without match`() {
        list.apply {
            append(order(0))
            assertFalse(contains(order(1)))
        }
    }

    @Test
    fun `containsAll on empty list`() {
        val searchFor = listOf(order(0), order(1))
        assertFalse(list.containsAll(searchFor))
    }

    @Test
    fun `containsAll no match`() {
        val searchFor = listOf(order(0), order(1))
        list.apply {
            append(order(2))
            assertFalse(containsAll(searchFor))
        }
    }

    @Test
    fun `containsAll all in list matches but requested is longer`() {
        val searchFor = listOf(order(0), order(1))
        list.apply {
            append(order(0))
            assertFalse(containsAll(searchFor))
        }
    }

    @Test
    fun `containsAll full list match`() {
        val searchFor = listOf(order(0), order(1))
        list.apply {
            append(order(0))
            append(order(1))
            assertTrue(containsAll(searchFor))
        }
    }

    @Test
    fun `containsAll matches on part of list`() {
        val searchFor = listOf(order(0), order(1))
        list.apply {
            append(order(0))
            append(order(1))
            append(order(2))
            assertTrue(containsAll(searchFor))
        }
    }

    @Test
    fun `containsAll with duplicates in searching collection`() {
        val searchFor = listOf(order(0), order(0))
        list.apply {
            append(order(0))
            append(order(1))
            assertTrue(containsAll(searchFor))
        }
    }

    @Test
    fun `add on empty list`() {
        list.apply {
            append(order(1))
            assertEquals(OrderNode(order(1), null), first)
        }
    }

    @Test
    fun `add twice on empty list`() {
        val expected = OrderNode(order(5),
                OrderNode(order(3), null))
        list.apply {
            add(order(5))
            add(order(3))
            assertEquals(expected, first)
        }

    }

    @Test
    fun `addAll with one Element on empty List`() {
        val toAdd = listOf(order(0))
        list.apply {
            addAll(toAdd)
            assertEquals(OrderNode(order(0), null), first)
        }
    }

    @Test
    fun `addAll with multiple Elements on empty list`() {
        val toAdd = listOf(order(0), order(1))
        list.addAll(toAdd)
        val expected = OrderNode(order(0),
                OrderNode(order(1), null))
        assertEquals(expected, list.first)
    }

    @Test
    fun `addAll with one Element on filled list`() {
        val toAdd = listOf(order(2))
        val expected = OrderNode(order(0),
                OrderNode(order(1),
                        OrderNode(order(2), null)))
        list.apply {
            append(order(0))
            append(order(1))
            addAll(toAdd)
            assertEquals(expected, first)
        }
    }

    @Test
    fun `addAll with multiple Elements on filled list`() {
        val toAdd = listOf(order(2), order(3))
        val expected = OrderNode(order(0),
                OrderNode(order(1),
                        OrderNode(order(2),
                                OrderNode(order(3), null))))
        list.apply {
            append(order(0))
            append(order(1))
            addAll(toAdd)
            assertEquals(expected, first)
        }
    }

    @Test
    fun `clear on empty list`() {
        list.apply {
            clear()
            assertNull(first)
        }
    }

    @Test
    fun `clear on filled list`() {
        list.apply {
            append(order(0))
            clear()
            assertNull(first)
        }
    }

    @Test
    fun `remove on empty list`() {
        list.apply {
            assertFalse(remove(order(0)))
            assertNull(first)
        }
    }

    @Test
    fun `remove on list without target`() {
        val expected = OrderNode(order(0),
                OrderNode(order(1), null))
        list.apply {
            append(order(0))
            append(order(1))
            assertFalse(remove(order(2)))
            assertEquals(expected, first)
        }
    }

    @Test
    fun `remove on list with target`() {
        val expected = OrderNode(order(0), null)
        list.apply {
            append(order(0))
            append(order(1))
            assertTrue(remove(order(1)))
            assertEquals(expected, first)
        }

    }

    @Test
    fun `retainAll on empty list`() {
        val toRetain = listOf(order(0), order(1))
        list.apply {
            assertFalse(list.retainAll(toRetain))
            assertNull(list.first)
        }
    }

    @Test
    fun `retainAll everything matches`() {
        val toRetain = listOf(order(0), order(1))
        val expected = OrderNode(order(0),
                OrderNode(order(1), null))
        list.apply {
            append(order(0))
            append(order(1))
            assertFalse(retainAll(toRetain))
            assertEquals(expected, first)
        }
    }

    @Test
    fun `retainAll toRetain longer than list everything matches`() {
        val toRetain = listOf(order(0), order(1), order(2))
        val expected = OrderNode(order(0),
                OrderNode(order(1), null))
        list.apply {
            append(order(0))
            append(order(1))
            assertFalse(retainAll(toRetain))
            assertEquals(expected, first)
        }
    }

    @Test
    fun `retainAll with duplicates in list`() {
        val toRetain = listOf(order(0))
        val expected = OrderNode(order(0),
                OrderNode(order(0), null))
        list.apply {
            append(order(0))
            append(order(0))
            assertFalse(retainAll(toRetain))
            assertEquals(expected, first)
        }
    }

    @Test
    fun `retainAll in list but list longer`() {
        val toRetain = listOf(order(0))
        val expected = OrderNode(order(0), null)
        list.apply {
            append(order(0))
            append(order(1))
            assertTrue(retainAll(toRetain))
            assertEquals(expected, first)
        }
    }

    @Test
    fun `retainAll matches partially`() {
        val toRetain = listOf(order(1), order(2))
        val expected = OrderNode(order(1), null)
        list.apply {
            append(order(0))
            append(order(1))
            assertTrue(retainAll(toRetain))
            assertEquals(expected, first)
        }
    }

    @Test
    fun `retainAll does not match`() {
        val toRetain = listOf(order(2))
        list.apply {
            append(order(0))
            append(order(1))
            assertTrue(retainAll(toRetain))
            assertNull(first)
        }
    }

    @Test
    fun `removeAll on empty list`() {
        val toRemove = listOf(order(0), order(1))
        list.apply {
            assertFalse(removeAll(toRemove))
            assertNull(first)
        }
    }

    @Test
    fun `removeAll everything matches`() {
        val toRemove = listOf(order(0), order(1))
        list.apply {
            append(order(0))
            append(order(1))
            assertTrue(removeAll(toRemove))
            assertNull(first)
        }
    }

    @Test
    fun `removeAll toRemove longer than list everything matches`() {
        val toRemove = listOf(order(0), order(1), order(2))
        list.apply {
            append(order(0))
            append(order(1))
            assertTrue(removeAll(toRemove))
            assertNull(first)
        }
    }

    @Test
    fun `removeAll with duplicates in list`() {
        val toRemove = listOf(order(0))
        list.apply {
            append(order(0))
            append(order(0))
            assertTrue(removeAll(toRemove))
            assertNull(first)
        }
    }

    @Test
    fun `removeAll in list but list longer`() {
        val toRemove = listOf(order(0))
        val expected = OrderNode(order(1), null)
        list.apply {
            append(order(0))
            append(order(1))
            assertTrue(removeAll(toRemove))
            assertEquals(expected, first)
        }
    }

    @Test
    fun `removeAll matches partially`() {
        val toRemove = listOf(order(1), order(2))
        val expected = OrderNode(order(0), null)
        list.apply {
            append(order(0))
            append(order(1))
            assertTrue(removeAll(toRemove))
            assertEquals(expected, first)
        }
    }

    @Test
    fun `removeAll does not match`() {
        val toRetain = listOf(order(2))
        val expected = OrderNode(order(0),
                OrderNode(order(1), null))
        list.apply {
            append(order(0))
            append(order(1))
            assertFalse(removeAll(toRetain))
            assertEquals(expected, first)
        }

    }

    @Test
    fun `equals wrong class`() {
        assertFalse(list.equals("String"))
    }

    @Test
    fun `equals is null safe`() {
        assertNotEquals(list, null as OrderProcessing?)
    }

    @Test
    fun `equals identity test`() {
        assertEquals(list, list)
    }

    @Test
    fun `equals true on empty lists`() {
        assertEquals(list, OrderProcessing())
    }

    @Test
    fun `equals true with same orders in list`() {
        list.append(order(0))
        val other = OrderProcessing().apply { append(order(0)) }
        assertEquals(list, other)
    }

    @Test
    fun `equals false with different orders in list`() {
        list.append(order(0))
        val other = OrderProcessing().apply { append(order(1)) }
        assertNotEquals(list, other)
    }

    @Test
    fun `equals false with different ordering of list`() {
        list.apply {
            append(order(0))
            append(order(1))
        }
        val other = OrderProcessing().apply {
            append(order(1))
            append(order(0))
        }
        assertNotEquals(list, other)
    }

    @Test
    fun `hashcode of empty list`() {
        assertEquals(list.hashCode(), 0)
    }

    @Test
    fun `hashcode of filled list`() {
        list.apply {
            append(order(0))
            append(order(1))
        }
        val expected = OrderNode(order(0),
                OrderNode(order(1), null))
        assertEquals(list.hashCode(), expected.hashCode())
    }

}