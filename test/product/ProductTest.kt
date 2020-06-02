package product

import org.junit.Before
import org.junit.Test
import review.PlainReview
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProductTest {
    var product = Product.create("name", 5.0, 10.0, "desc")

    @Before
    fun init() {
        Product.longestAmount = 0
        Product.longestPrice = 0
        Product.longestName = 0
        product = Product.create("name", 5.0, 10.0, "desc")
    }

    @Test
    fun Maxima() {
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
        product.addStock(StockUnit(10, 5))
        assertEquals(50.0, product.valueOfAllItems)
    }

    @Test
    fun `salesValueOfAllItems works`() {
        product.addStock(StockUnit(10, 5))
        assertEquals(100.0, product.salesValueOfAllItems)
    }

    @Test
    fun `availableItems works`() {
        product.addStock(StockUnit(10, 5))
        assertEquals(10, product.availableItems)
    }

    @Test
    fun `multi stock availableItems works`() {
        product.addStock(StockUnit(10, 5))
        product.addStock(StockUnit(10, 2))
        assertEquals(20, product.availableItems)
    }

    @Test
    fun `no stock availableItems works`() {
        assertEquals(0, product.availableItems)
    }

    @Test
    fun `showNice formats properly`() {
        Product.longestName = 10
        Product.longestPrice = 10
        Product.longestAmount = 5
        assertEquals("      name\t     10.00€ \tx\t0", product.showNice())
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
        product.addStock(StockUnit(10, 5))
        assertEquals(10, product.availableItems)
        product.addStock(StockUnit(10, 5))
        assertEquals(20, product.availableItems)
        assertEquals(2, Product.longestAmount)
    }

    @Test
    fun `addReview works`() {
        product.addReview(PlainReview(5))
        assertEquals(1, product.reviews.size)
    }

    @Test
    fun `cleanStock removes expired and empty`() {
        val toRemain = StockUnit(5, 5)
        product.addStock(toRemain)
        product.addStock(StockUnit(10, -5))
        product.addStock(StockUnit(0, 10))
        product.addStock(StockUnit(-5, 10))
        product.addStock(StockUnit(0, 0))
        product.cleanStock()
        assertEquals(1, product.stockUnits.size)
        assertEquals(toRemain, product.stockUnits[0])
    }

    @Test
    fun `takeItems from one stock`() {
        product.addStock(StockUnit(10, 3))
        assertEquals(5, product.takeItems(5))
        assertEquals(5, product.availableItems)
    }

    @Test
    fun `takeItems from multiple stocks`() {
        val toRemain = StockUnit(10, 100000)
        product.addStock(toRemain)
        product.addStock(StockUnit(2, 2))
        assertEquals(5, product.takeItems(5))
        assertTrue(product.stockUnits.contains(toRemain))
        assertEquals(7, toRemain.quantity)
    }

}