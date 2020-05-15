package product

import review.Review

open class Product(val productName: String, val basePrice: Double, open val salesPrice: Double, val description: String) {
    val reviews: MutableList<Review> = mutableListOf()
    private var stockUnits: MutableList<StockUnit> = mutableListOf()

    val profitPerItem: Double
        get() = salesPrice - basePrice


    val valueOfAllItems: Double
        get() = availableItems * basePrice


    val salesValueOfAllItems: Double
        get() = availableItems * salesPrice

    val availableItems: Int
        get() = stockUnits.sumBy { it.quantity }


    override fun toString(): String {
        return productName + " %.2f€ ".format(salesPrice) + "x $availableItems"
    }

    fun describe(): String {
        return productName + "\n\tPreis: %.2f€\n\t".format(salesPrice) + description + "\n\t$availableItems auf Lager"
    }

    fun addStock(items: StockUnit) {
        stockUnits.add(items)
    }

    fun addReview(review: Review) {
        reviews.add(review)
    }

    fun cleanStock() {
        stockUnits = stockUnits.filterNot { it.isExpired || it.quantity == 0 } as MutableList<StockUnit>
    }

    fun isPreferredQuantityAvailable(preferredQuantity: Int): Boolean{
        return availableItems >= preferredQuantity
    }

    fun takeItems(preferredQuantity: Int): Int{
        var remainingQuantity = preferredQuantity
        stockUnits.sortBy { it.daysBeforeExpiration }
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