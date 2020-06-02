package review

import kotlin.math.round

/**
 * A [Review] that only accepts points between 0 and 5
 * @property comment The comment of this review
 */
class LimitedReview(uncheckedPoints: Double, private val comment: String) : Review {
    /**
     * The points this review has. Guaranteed to be between 0 and 5, inclusive
     */
    private val points: Double = when {
        uncheckedPoints < 0 -> 0.0
        uncheckedPoints > 5 -> 5.0
        else -> uncheckedPoints
    }

    /**
     * @return the amount of points this review has rounded to the next [Int]
     */
    override fun stars(): Int {
        return round(points).toInt()
    }

    /**
     * @return The comment of this review
     */
    override fun info(): String {
        return comment
    }
}