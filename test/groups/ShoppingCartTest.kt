package groups

import org.junit.Before
import org.junit.Test
import product.InfiniteProduct
import product.Product
import product.StockUnit
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ShoppingCartTest {
    var cart: ShoppingCart = ShoppingCart()
    var products: MutableList<Product> = mutableListOf()

    @Before
    fun init() {
        cart = ShoppingCart()
        products.clear()
        products.add(InfiniteProduct("1", 2.0, 4.0, ""))
        products.add(InfiniteProduct("2", 3.0, 4.0, ""))
        products.add(InfiniteProduct("3", 2.0, 4.0, ""))
        cart.addProduct(products[0], 5)
        cart.addProduct(products[1], 10)
        cart.addProduct(products[2], 5)
    }

    @Test
    fun `available Products reported as such`() {
        assertTrue { cart.allProductsAvailable }
    }

    @Test
    fun `totalPrice is accurate`() {
        assertEquals(4.0 * 5 + 4 * 10 + 4 * 5, cart.totalPrice)
    }

    @Test
    fun `listOfAllProducts is correct`() {
        assertEquals("5\t1\t20.00€\n" +
                "10\t2\t40.00€\n" +
                "5\t3\t20.00€\n", cart.listOfAllProducts)
    }

    @Test
    fun `clear clears list`() {
        cart.clear()
        assertTrue { cart.productAndQuantityList.isEmpty() }
    }

    @Test
    fun `addProduct works`() {
        assertEquals(3, cart.productAndQuantityList.size)
        assertEquals(Pair(products[0], 5), cart.productAndQuantityList[0])
        assertEquals(Pair(products[1], 10), cart.productAndQuantityList[1])
        assertEquals(Pair(products[2], 5), cart.productAndQuantityList[2])
    }

    @Test(expected = InsufficientProductsException::class)
    fun `addProduct checks product amounts`() {
        cart.addProduct(Product("", 0.2, 0.3, ""), 5)
    }

    @Test
    fun `addProduct stacks multiple requests of the same Product`() {
        cart.addProduct(products[0], 20)
        val pair = cart.productAndQuantityList.first { it.first == products[0] }
        assertEquals(25, pair.second)
        assertEquals(3, cart.productAndQuantityList.size)
    }

    @Test
    fun `buyEverything buys everything`() {
        assertEquals(80.0, cart.buyEverything())
        assertTrue(cart.productAndQuantityList.isEmpty())
    }

    @Test(expected = InsufficientProductsException::class)
    fun `addProduct checks amounts while adding`() {
        val product = Product("", 0.2, 0.3, "")
        product.addStock(StockUnit(10, 5))
        cart.addProduct(product, 10)
        cart.addProduct(product, 10)
    }
}