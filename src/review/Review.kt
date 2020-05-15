package review

interface Review : Comparable<Review> {
    fun stars(): Int
    fun info(): String
    override fun compareTo(other: Review): Int {
        return this.stars() - other.stars()
    }
}