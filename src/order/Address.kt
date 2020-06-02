package order

/**
 * Data class to represent an Address
 * @property firstName The first name of the addressee
 * @property lastName The last name of the addressee
 * @property street The street the addressee lives on
 * @property city The city the addressee lives on
 */
data class Address(
        val firstName: String,
        val lastName: String,
        val street: String,
        val city: String
)