package review

/**
 * A Review that attaches a generic comment ot an arbitrary point rating
 * @property points The points of this review
 */
class SmartReview(private val points: Int) : Review {
    /**
     * @return The points of this review
     */
    override fun stars(): Int {
        return points
    }

    /**
     * @return A generic comment based on the point value. Values 0-5 get a comment each and everything else gets a catchall comment
     */
    override fun info(): String {
        return when (stars()) {
            0 -> "Schlechtes Produkt"
            1 -> "Mäßiges Produkt"
            2 -> "Durchschnittliches Produkt"
            3 -> "Brauchbares Produkt"
            4 -> "Gutes Produkt"
            5 -> "Exzellentes Produkt"
            else -> "Nicht sinnvoll bewertet"
        }
    }
}