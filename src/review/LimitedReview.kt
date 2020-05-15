package review

import kotlin.math.round

class LimitedReview(uncheckedPoints: Double, private val comment: String) : Review {
    private val points: Double = when {
        uncheckedPoints < 0 -> 0.0
        uncheckedPoints > 5 -> 5.0
        else -> uncheckedPoints
    }

    override fun stars(): Int {
        return round(points).toInt()
    }

    override fun info(): String {
        return comment
    }
}