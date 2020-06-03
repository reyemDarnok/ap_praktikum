package product

import org.junit.After
import org.junit.Before
import org.junit.Test
import review.PlainReview
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProductTest {
    var product = Product.create("name", 5.0, 10.0, "desc")

    @Before
    fun init() {
        product = Product.create("name", 5.0, 10.0, "desc")
    }

    @After
    fun cleanup() {
        Product.apply {
            longestAmount = 0
            longestName = 0
            longestPrice = 0
        }
    }

    @Test
    fun factory() {
        assertEquals(5, Product.longestPrice)
        assertEquals(4, Product.longestName)
        assertEquals(0, Product.longestAmount)
    }

    @Test
    fun `profitPerItem works`() {
        assertEquals(5.0, product.profitPerItem)
    }

    @Test
    fun `valueOfAllItems works`() {
        product.apply {
            addStock(StockUnit(10, 5))
            assertEquals(50.0, valueOfAllItems)
        }
    }

    @Test
    fun `salesValueOfAllItems works`() {
        product.apply {
            addStock(StockUnit(10, 5))
            assertEquals(100.0, salesValueOfAllItems)
        }
    }

    @Test
    fun `availableItems works`() {
        product.apply {
            addStock(StockUnit(10, 5))
            assertEquals(10, availableItems)
        }
    }

    @Test
    fun `multi stock availableItems works`() {
        product.apply {
            addStock(StockUnit(10, 5))
            addStock(StockUnit(10, 2))
            assertEquals(20, availableItems)
        }
    }

    @Test
    fun `no stock availableItems works`() {
        assertEquals(0, product.availableItems)
    }

    @Test
    fun `toString formats properly`() {
        assertEquals("name 10.00 Euro. desc", product.toString())
    }

    @Test
    fun `describe makes description`() {
        assertEquals("name\n" +
                "\tPreis: 10.00€\n" +
                "\tdesc\n" +
                "\t0 auf Lager", product.describe())
    }

    @Test
    fun `addStock adds Stock`() {
        product.apply {
            addStock(StockUnit(10, 5))
            addStock(StockUnit(10, 5))
            assertEquals(20, availableItems)
            assertEquals(2, Product.longestAmount)
        }
    }

    @Test
    fun `addReview works`() {
        product.apply {
            addReview(PlainReview(5))
            assertEquals(1, reviews.size)
        }
    }

    @Test
    fun `cleanStock removes expired and empty`() {
        val toRemain = StockUnit(5, 5)
        product.apply {
            addStock(toRemain)
            addStock(StockUnit(10, -5))
            addStock(StockUnit(0, 10))
            addStock(StockUnit(-5, 10))
            addStock(StockUnit(0, 0))
            cleanStock()
            assertEquals(1, stockUnits.size)
            assertEquals(toRemain, stockUnits[0])
        }
    }

    @Test
    fun `takeItems from one stock`() {
        product.apply {
            addStock(StockUnit(10, 3))
            assertEquals(5, takeItems(5))
            assertEquals(5, availableItems)
        }
    }

    @Test
    fun `takeItems from multiple stocks`() {
        val toRemain = StockUnit(10, 100000)
        product.apply {
            addStock(toRemain)
            addStock(StockUnit(2, 2))
            assertEquals(5, takeItems(5))
            assertTrue(stockUnits.contains(toRemain))
            assertEquals(7, toRemain.quantity)
        }
    }
}