class SmartReview(private val points: Int) : Review {
    override fun stars(): Int {
        return points
    }

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