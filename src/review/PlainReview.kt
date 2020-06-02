package review

/**
 * A simplistic review with no restrictions to points
 * @property points The amount of points this review has. May be any [Int]
 */
class PlainReview(private val points: Int) : Review {
    /**
     * @return The number of points this review has
     */
    override fun stars(): Int {
        return points
    }

    /**
     * @return A short string saying how many points this review has
     */
    override fun info(): String {
        return if (points != 1 && points != -1) {
            "Produkt mit %d Sternen.".format(points)
        } else {
            "Produkt mit %d Stern.".format(points)
        }
    }


}