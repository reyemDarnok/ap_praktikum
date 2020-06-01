package order

import groups.ShoppingCart

data class Order(
        val shoppingCart: ShoppingCart,
        val address: Address
) : Comparable<Order> {
    /**
     * Compares this object with the specified object for order. Returns zero if this object is equal
     * to the specified [other] object, a negative number if it's less than [other], or a positive number
     * if it's greater than [other].
     */
    override fun compareTo(other: Order): Int {
        val difference = shoppingCart.totalPrice - other.shoppingCart.totalPrice
        //avoid rounding to zero in cases of differences smaller than 1
        return if (difference > 0 && difference < 1) {
            1
        } else if (difference < 0 && difference > -1) {
            -1
        } else {
            difference.toInt()
        }
    }
}