class StockUnit(var quantity: Int,daysBeforeExpiration: Int){

    val isExpired: Boolean
        get() =  daysBeforeExpiration < 0


    var daysBeforeExpiration: Int = daysBeforeExpiration
        set(numOfDays) {
            if (numOfDays > field) {
                throw IllegalArgumentException("Time is linear! Number of days before expiration can only ever get smaller")
            } else {
                field = numOfDays
            }
        }

    val isExpiringSoon: Boolean
        get() = daysBeforeExpiration < 5

}