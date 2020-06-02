package product

import org.junit.Test
import kotlin.test.assertEquals

class StockUnitTest {
    @Test
    fun `reduce daysBeforeExpiration`() {
        val su = StockUnit(5, 10)
        su.daysBeforeExpiration = 3
        assertEquals(3, su.daysBeforeExpiration)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `increase DaysBeforeExpiration`() {
        val su = StockUnit(5, 3)
        su.daysBeforeExpiration = 10
    }
}