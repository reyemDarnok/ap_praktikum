package product

/**
 * Represents a group of items of the same product
 * @property quantity How many Products are there
 */
class StockUnit(var quantity: Int, daysBeforeExpiration: Int) {

    /**
     * Whether this StockUnit has expired
     */
    val isExpired: Boolean
        get() = daysBeforeExpiration < 0


    /**
     * How many days need to pass until this StockUnit expires
     */
    var daysBeforeExpiration: Int = daysBeforeExpiration
        /**
         * Checks for linearity of time
         * @param numOfDays The new value of [daysBeforeExpiration]. Needs to be smaller than the current value
         * @throws IllegalArgumentException if you attempt to go backwards in time,
         * i.e. try to set [daysBeforeExpiration] to a larger value
         */
        set(numOfDays) {
            if (numOfDays > field) {
                throw IllegalArgumentException("Time is linear! Number of days before expiration can only ever get smaller")
            } else {
                field = numOfDays
            }
        }

    /**
     * Whether this StockUnit expires in the next 5 days
     */
    val isExpiringSoon: Boolean
        get() = daysBeforeExpiration < 5

}