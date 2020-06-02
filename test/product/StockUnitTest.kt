package product

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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

    @Test
    fun `is not expiring soon`() {
        val su = StockUnit(5, 10)
        assertFalse(su.isExpiringSoon)
    }

    @Test
    fun `is expiring soon`() {
        val su = StockUnit(5, 2)
        assertTrue(su.isExpiringSoon)
    }
}