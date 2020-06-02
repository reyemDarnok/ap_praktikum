package review

import org.junit.Test
import kotlin.test.assertEquals

class SmartReviewTest {
    @Test
    fun `0 Test`() {
        assertEquals("Schlechtes Produkt", SmartReview(0).info())
    }

    @Test
    fun `1 Test`() {
        assertEquals("Mäßiges Produkt", SmartReview(1).info())
    }

    @Test
    fun `2 Test`() {
        assertEquals("Durchschnittliches Produkt", SmartReview(2).info())
    }

    @Test
    fun `3 Test`() {
        assertEquals("Brauchbares Produkt", SmartReview(3).info())
    }

    @Test
    fun `4 Test`() {
        assertEquals("Gutes Produkt", SmartReview(4).info())
    }

    @Test
    fun `5 Test`() {
        assertEquals("Exzellentes Produkt", SmartReview(5).info())
    }

    @Test
    fun `Other Test`() {
        assertEquals("Nicht sinnvoll bewertet", SmartReview(10).info())
    }
}