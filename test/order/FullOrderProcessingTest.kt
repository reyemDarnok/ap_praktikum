package order

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

    @Test
    fun `process highest on empty list`() {
        list.processHighest()
        assertNull(list.first)
    }

    @Test
    fun `process highest and only`() {
        list.append(order(10))
        list.processHighest()
        assertNull(list.first)
    }

    @Test
    fun `process highest and first`() {
        list.append(order(10))
        list.append(order(0))
        list.append(order(5))
        list.processHighest()
        val expected = OrderNode(order(0),
                OrderNode(order(5), null))
        assertEquals(expected, list.first)
    }

    @Test
    fun `process highest and last`() {
        list.append(order(2))
        list.append(order(1))
        list.append(order(5))
        list.processHighest()
        val expected = OrderNode(order(2),
                OrderNode(order(1), null))
        assertEquals(expected, list.first)
    }

    @Test
    fun `process highest and middle`() {
        list.append(order(4))
        list.append(order(5))
        list.append(order(2))
        list.processHighest()
        val expected = OrderNode(order(4),
                OrderNode(order(2), null))
        assertEquals(expected, list.first)
    }

    @Test
    fun `process all for city no targets`() {
        list.append(order(4))
        list.append(order(2))
        list.processAllFor("some city")
        val expected = OrderNode(order(4),
                OrderNode(order(2), null))
        assertEquals(expected, list.first)
    }

    @Test
    fun `process all for city some targets`() {
        list.append(order(2, "target"))
        list.append(order(4))
        list.append(order(3, "target"))
        list.append(order(5))
        list.processAllFor("target")
        val expected = OrderNode(order(4),
                OrderNode(order(5), null))
        assertEquals(expected, list.first)
    }

    @Test
    fun `process all for city all targets`() {
        list.append(order(2))
        list.append(order(1))
        list.processAllFor("")
        assertTrue(list.isEmpty)
    }

    @Test
    fun `process all for city empty list`() {
        list.processAllFor("")
        assertTrue(list.isEmpty)
    }

    @Test
    fun `process all on empty list`() {
        list.processAll()
        assertTrue(list.isEmpty)
    }

    @Test
    fun `process all on filled list`() {
        list.append(order(5))
        list.append(order(3))
        list.processAll()
        assertTrue(list.isEmpty)
    }

    @Test
    fun `analyze all on empty list`() {
        assertEquals("", list.analyzeAll { it.toString() })
    }

    @Test
    fun `analyze all on filled list`() {
        list.append(order(2))
        list.append(order(3))
        val expected = "4|9|"
        assertEquals(expected, list.analyzeAll { "%.0f".format(it.shoppingCart.totalPrice) + "|" })
    }

    @Test
    fun `any on empty list`() {
        assertFalse(list.anyProduct { true })
    }

    @Test
    fun `any with false result`() {
        list.append(order(3))
        list.append(order(2))
        assertFalse(list.anyProduct { it.productName == "not there" })
    }

    @Test
    fun `any with all matches`() {
        list.append(order(3))
        list.append(order(2))
        assertTrue(list.anyProduct { true })
    }

    @Test
    fun `any with some matches`() {
        order(4).shoppingCart.addProduct(
                InfiniteProduct("name", 0.0, 0.0, ""), 5)
        list.append(order(2))
        list.append(order(4))
        list.append(order(3))
        assertTrue(list.anyProduct { it.productName == "name" })
    }

    @Test
    fun `filter on empty list`() {
        assertNull(list.filter { true }.first)
    }

    @Test
    fun `filter with false result`() {
        list.append(order(3))
        list.append(order(2))
        assertNull(list.filter { false }.first)
    }

    @Test
    fun `filter with all matches`() {
        list.append(order(3))
        list.append(order(2))
        val expected = OrderNode(order(3),
                OrderNode(order(2), null))
        assertEquals(expected, list.filter { true }.first)
    }

    @Test
    fun `filter with one match`() {
        order(4).shoppingCart.addProduct(
                InfiniteProduct("name", 0.0, 0.0, ""), 5)
        list.append(order(2))
        list.append(order(4))
        list.append(order(3))
        val expected = OrderNode(order(4), null)
        assertEquals(expected, list.filter { it.shoppingCart.productAndQuantityList.any { it.first.productName == "name" } }.first)
    }

    @Test
    fun `filter is not identity preserving`() {
        list.append(order(5))
        list.append(order(3))
        assertNotSame(list, list.filter { true })
    }

    /**
     * List tests start here
     */

    @Test
    fun `removing first and only via iterator`() {
        list.append(order(5))
        val iterator = list.iterator()
        while (iterator.hasNext()) {
            iterator.next()
            iterator.remove()
            break
        }
        assertNull(list.first)
    }

    @Test(expected = IllegalStateException::class)
    fun `removing without accessing causes exception`() {
        list.iterator().remove()
    }

    @Test(expected = java.lang.IllegalStateException::class)
    fun `removing twice without accessing in between is not allowed`() {
        list.append(order(1))
        list.append(order(2))
        list.append(order(3))
        list.append(order(4))
        val iterator = list.iterator()
        iterator.next()
        iterator.next()
        iterator.next()
        iterator.remove()
        iterator.remove()
    }

    @Test
    fun `removing first with following`() {
        list.append(order(2))
        list.append(order(3))
        val iterator = list.iterator()
        iterator.next()
        iterator.remove()
        assertEquals(OrderNode(order(3), null), list.first)
    }

    @Test
    fun `removing from end`() {
        list.append(order(2))
        list.append(order(3))
        val iterator = list.iterator()
        iterator.next()
        iterator.next()
        iterator.remove()
        assertEquals(OrderNode(order(2), null), list.first)
    }

    @Test
    fun `removing twice`() {
        list.append(order(1))
        list.append(order(2))
        list.append(order(3))
        list.append(order(4))
        val iterator = list.iterator()
        iterator.next()
        iterator.next()
        iterator.remove()
        iterator.next()
        iterator.remove()
        val expected = OrderNode(order(1),
                OrderNode(order(4), null))
        assertEquals(expected, list.first)
    }

    @Test
    fun `contains on empty list`() {
        assertFalse(list.contains(order(0)))
    }

    @Test
    fun `contains on list with match`() {
        list.append(order(1))
        assertTrue(list.contains(order(1)))
    }

    @Test
    fun `contains on list without match`() {
        list.append(order(0))
        assertFalse(list.contains(order(1)))
    }

    @Test
    fun `containsAll on empty list`() {
        val searchFor = listOf(order(0), order(1))
        assertFalse(list.containsAll(searchFor))
    }

    @Test
    fun `containsAll no match`() {
        val searchFor = listOf(order(0), order(1))
        list.append(order(2))
        assertFalse(list.containsAll(searchFor))
    }

    @Test
    fun `containsAll all in list matches but requested is longer`() {
        val searchFor = listOf(order(0), order(1))
        list.append(order(0))
        assertFalse(list.containsAll(searchFor))
    }

    @Test
    fun `containsAll full list match`() {
        val searchFor = listOf(order(0), order(1))
        list.append(order(0))
        list.append(order(1))
        assertTrue(list.containsAll(searchFor))
    }

    @Test
    fun `containsAll matches on part of list`() {
        val searchFor = listOf(order(0), order(1))
        list.append(order(0))
        list.append(order(1))
        list.append(order(2))
        assertTrue(list.containsAll(searchFor))
    }

    @Test
    fun `containsAll with duplicates in searching collection`() {
        val searchFor = listOf(order(0), order(0))
        list.append(order(0))
        list.append(order(1))
        assertTrue(list.containsAll(searchFor))
    }

    @Test
    fun `add on empty list`() {
        list.append(order(1))
        assertEquals(OrderNode(order(1), null), list.first)
    }

    @Test
    fun `add twice on empty list`() {
        list.add(order(5))
        list.add(order(3))
        val expected = OrderNode(order(5),
                OrderNode(order(3), null))
        assertEquals(expected, list.first)
    }

    @Test
    fun `addAll with one Element on empty List`() {
        val toAdd = listOf(order(0))
        list.addAll(toAdd)
        assertEquals(OrderNode(order(0), null), list.first)
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
        list.append(order(0))
        list.append(order(1))
        val toAdd = listOf(order(2))
        list.addAll(toAdd)
        val expected = OrderNode(order(0),
                OrderNode(order(1),
                        OrderNode(order(2), null)))
        assertEquals(expected, list.first)
    }

    @Test
    fun `addAll with multiple Elements on filled list`() {
        list.append(order(0))
        list.append(order(1))
        val toAdd = listOf(order(2), order(3))
        list.addAll(toAdd)
        val expected = OrderNode(order(0),
                OrderNode(order(1),
                        OrderNode(order(2),
                                OrderNode(order(3), null))))
        assertEquals(expected, list.first)
    }

    @Test
    fun `clear on empty list`() {
        list.clear()
        assertNull(list.first)
    }

    @Test
    fun `clear on filled list`() {
        list.append(order(0))
        list.clear()
        assertNull(list.first)
    }

    @Test
    fun `remove on empty list`() {
        assertFalse(list.remove(order(0)))
        assertNull(list.first)
    }

    @Test
    fun `remove on list without target`() {
        list.append(order(0))
        list.append(order(1))
        assertFalse(list.remove(order(2)))
        val expected = OrderNode(order(0),
                OrderNode(order(1), null))
        assertEquals(expected, list.first)
    }

    @Test
    fun `remove on list with target`() {
        list.append(order(0))
        list.append(order(1))
        assertTrue(list.remove(order(1)))
        val expected = OrderNode(order(0), null)
        assertEquals(expected, list.first)
    }

    @Test
    fun `retainAll on empty list`() {
        val toRetain = listOf(order(0), order(1))
        assertFalse(list.retainAll(toRetain))
        assertNull(list.first)
    }

    @Test
    fun `retainAll everything matches`() {
        list.append(order(0))
        list.append(order(1))
        val toRetain = listOf(order(0), order(1))
        assertFalse(list.retainAll(toRetain))
        val expected = OrderNode(order(0),
                OrderNode(order(1), null))
        assertEquals(expected, list.first)
    }

    @Test
    fun `retainAll toRetain longer than list everything matches`() {
        list.append(order(0))
        list.append(order(1))
        val toRetain = listOf(order(0), order(1), order(2))
        assertFalse(list.retainAll(toRetain))
        val expected = OrderNode(order(0),
                OrderNode(order(1), null))
        assertEquals(expected, list.first)
    }

    @Test
    fun `retainAll with duplicates in list`() {
        list.append(order(0))
        list.append(order(0))
        val toRetain = listOf(order(0))
        assertFalse(list.retainAll(toRetain))
        val expected = OrderNode(order(0),
                OrderNode(order(0), null))
        assertEquals(expected, list.first)
    }

    @Test
    fun `retainAll in list but list longer`() {
        list.append(order(0))
        list.append(order(1))
        val toRetain = listOf(order(0))
        assertTrue(list.retainAll(toRetain))
        val expected = OrderNode(order(0), null)
        assertEquals(expected, list.first)
    }

    @Test
    fun `retainAll matches partially`() {
        list.append(order(0))
        list.append(order(1))
        val toRetain = listOf(order(1), order(2))
        assertTrue(list.retainAll(toRetain))
        val expected = OrderNode(order(1), null)
        assertEquals(expected, list.first)
    }

    @Test
    fun `retainAll does not match`() {
        list.append(order(0))
        list.append(order(1))
        val toRetain = listOf(order(2))
        assertTrue(list.retainAll(toRetain))
        assertNull(list.first)
    }

    @Test
    fun `removeAll on empty list`() {
        val toRemove = listOf(order(0), order(1))
        assertFalse(list.removeAll(toRemove))
        assertNull(list.first)
    }

    @Test
    fun `removeAll everything matches`() {
        list.append(order(0))
        list.append(order(1))
        val toRemove = listOf(order(0), order(1))
        assertTrue(list.removeAll(toRemove))
        assertNull(list.first)
    }

    @Test
    fun `removeAll toRemove longer than list everything matches`() {
        list.append(order(0))
        list.append(order(1))
        val toRemove = listOf(order(0), order(1), order(2))
        assertTrue(list.removeAll(toRemove))
        assertNull(list.first)
    }

    @Test
    fun `removeAll with duplicates in list`() {
        list.append(order(0))
        list.append(order(0))
        val toRemove = listOf(order(0))
        assertTrue(list.removeAll(toRemove))
        assertNull(list.first)
    }

    @Test
    fun `removeAll in list but list longer`() {
        list.append(order(0))
        list.append(order(1))
        val toRemove = listOf(order(0))
        assertTrue(list.removeAll(toRemove))
        val expected = OrderNode(order(1), null)
        assertEquals(expected, list.first)
    }

    @Test
    fun `removeAll matches partially`() {
        list.append(order(0))
        list.append(order(1))
        val toRemove = listOf(order(1), order(2))
        assertTrue(list.removeAll(toRemove))
        val expected = OrderNode(order(0), null)
        assertEquals(expected, list.first)
    }

    @Test
    fun `removeAll does not match`() {
        list.append(order(0))
        list.append(order(1))
        val toRetain = listOf(order(2))
        assertFalse(list.removeAll(toRetain))
        val expected = OrderNode(order(0),
                OrderNode(order(1), null))
        assertEquals(expected, list.first)
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