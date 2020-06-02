package review

import org.junit.Test
import kotlin.test.assertEquals

class PlainReviewTest {
    @Test
    fun `echo stars`() {
        assertEquals(5, PlainReview(5).stars())
    }

    @Test
    fun `info plural`() {
        assertEquals("Produkt mit 2 Sternen.", PlainReview(2).info())
    }

    @Test
    fun `info -1`() {
        assertEquals("Produkt mit -1 Stern.", PlainReview(-1).info())
    }

    @Test
    fun `info +1`() {
        assertEquals("Produkt mit 1 Stern.", PlainReview(1).info())
    }

    @Test
    fun `info -plural`() {
        assertEquals("Produkt mit -2 Sternen.", PlainReview(-2).info())
    }
}