package product

import org.junit.Test
import kotlin.test.assertEquals

class DiscountProductTest {
    @Test
    fun `salesPrice is adjusted`() {
        val discountProduct = DiscountProduct.create("", 3.0, 10.0, "", DiscountType.SellEverything)
        assertEquals(5.0, discountProduct.salesPrice)
    }
}