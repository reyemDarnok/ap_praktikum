package review

class PlainReview(private val points: Int) : Review {
    override fun stars(): Int {
        return points
    }

    override fun info(): String {
        return if (points != 1 && points != -1) {
            "Produkt mit %d Sternen.".format(points)
        } else {
            "Produkt mit %d Stern.".format(points)
        }
    }


}