package review

import org.junit.Test
import kotlin.test.assertTrue

class ReviewTest {
    @Test
    fun smaller() {
        assertTrue(PlainReview(-5) < PlainReview(10))
    }

    @Test
    fun larger() {
        assertTrue(PlainReview(5) > LimitedReview(3.0, ""))
    }
}