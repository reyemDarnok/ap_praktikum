package review

/**
 * A template for Reviews. Compares via the return value of the stars() method
 */
interface Review : Comparable<Review> {
    fun stars(): Int
    fun info(): String
    override fun compareTo(other: Review): Int {
        return this.stars() - other.stars()
    }
}