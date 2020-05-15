class StockUnit(var quantity: Int,daysBeforeExpiration: Int){

    val isExpired: Boolean
        get() =  daysBeforeExpiration < 0


    var daysBeforeExpiration: Int = daysBeforeExpiration
        private set

    val isExpiringSoon: Boolean
        get() = daysBeforeExpiration < 5


    fun daysPassed(numOfDays: Int){
        if(numOfDays < 0){
            throw IllegalArgumentException("Time is linear! Negative values for daysPassed are disallowed")
        } else {
            daysBeforeExpiration-=numOfDays
        }
    }

}