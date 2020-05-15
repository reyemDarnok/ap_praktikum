open class Product(val productName: String, val basePrice: Double, open val salesPrice: Double, val description: String) {
    val reviews: ArrayList<Review> = ArrayList()
    private var stockUnits: ArrayList<StockUnit> = ArrayList()

    val profitPerItem: Double
        get() = salesPrice - basePrice


    val valueOfAllItems: Double
        get() = availableItems * basePrice


    val salesValueOfAllItems: Double
        get() =  availableItems * salesPrice

    val availableItems: Int
        get() {
            var sum = 0
            for (unit: StockUnit in stockUnits) {
                sum += unit.quantity
            }
            return sum
        }


    override fun toString(): String {
        return productName + " Preis: %.2f€ (".format(salesPrice) + description + ") (${availableItems} auf Lager)"
    }

    fun addStock(items: StockUnit){
        stockUnits.add(items)
    }

    fun addReview(review: Review){
        reviews.add(review)
    }

    fun cleanStock(){
        var offset = 0
        for(i in 0 until stockUnits.size){
            val correctedIndex = i - offset
            if(stockUnits[correctedIndex].isExpired || stockUnits[correctedIndex].quantity==0){
                stockUnits.removeAt(i - offset)
                offset++
            }
        }
    }

    fun isPreferredQuantityAvailable(preferredQuantity: Int): Boolean{
        return availableItems >= preferredQuantity
    }

    fun takeItems(preferredQuantity: Int): Int{
        var remainingQuantity = preferredQuantity
        stockUnits = ArrayList(stockUnits.sortedWith(compareBy {it.daysBeforeExpiration}))
        for(unit in stockUnits){
            if(unit.quantity > remainingQuantity){
                unit.quantity -= remainingQuantity
                return preferredQuantity
            } else {
                remainingQuantity -= unit.quantity
                unit.quantity = 0
            }
        }
        return preferredQuantity - remainingQuantity
    }

}