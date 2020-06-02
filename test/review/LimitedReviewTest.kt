package review

import org.junit.Test
import kotlin.test.assertEquals

class LimitedReviewTest {
    @Test
    fun `no correction`() {
        val review = LimitedReview(3.0, "")
        assertEquals(3, review.stars())
    }

    @Test
    fun `rounding correct`() {
        val review = LimitedReview(2.8, "")
        assertEquals(3, review.stars())
    }

    @Test
    fun `correction up`() {
        val review = LimitedReview(-2.0, "")
        assertEquals(0, review.stars())
    }

    @Test
    fun `correction down`() {
        val review = LimitedReview(10.0, "")
        assertEquals(5, review.stars())
    }
}