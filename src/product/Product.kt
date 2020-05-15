package product

import review.Review
import kotlin.math.log10

open class Product(val productName: String, val basePrice: Double, open val salesPrice: Double, val description: String) {
    companion object Maxima {
        var longestName: Int = 0
        var longestPrice: Int = 0
        var longestAmount: Int = 0

        fun testNameLength(name: String) {
            if (name.length > longestName) {
                longestName = name.length
            }
        }

        fun testPriceLength(price: Double) {
            val priceLength = log10(price).toInt() + 3
            if (priceLength > longestPrice) {
                longestPrice = priceLength
            }
        }

        fun testAmountLength(amount: Int) {
            val amountLength = log10(amount.toDouble()).toInt() + 1
            if (amountLength > longestAmount) {
                longestAmount = amountLength
            }
        }

        fun testAll(product: Product) {
            testAmountLength(product.availableItems)
            testNameLength(product.productName)
            testPriceLength(product.salesPrice)
        }
    }

    val reviews: MutableList<Review> = mutableListOf()
    private var stockUnits: MutableList<StockUnit> = mutableListOf()

    init {
        testAll(this)
    }


    val profitPerItem: Double
        get() = salesPrice - basePrice


    val valueOfAllItems: Double
        get() = availableItems * basePrice


    val salesValueOfAllItems: Double
        get() = availableItems * salesPrice

    val availableItems: Int
        get() = stockUnits.sumBy { it.quantity }


    override fun toString(): String {
        val nameLength: Int = productName.length
        val priceLength = log10(salesPrice).toInt() + 3
        val amountLength = log10(availableItems.toDouble()).toInt() + 1
        val string = StringBuilder()
        for (i in 0 until (longestName - nameLength)) {
            string.append(' ')
        }
        string.append(productName).append('\t')
        for (i in 0 until (longestPrice - priceLength)) {
            string.append(' ')
        }
        string.append("%.2f€ ".format(salesPrice)).append("\tx\t")
        for (i in 0 until (longestAmount - amountLength)) {
            string.append(' ')
        }
        string.append(availableItems)
        return string.toString()
    }

    fun describe(): String {
        return productName + "\n\tPreis: %.2f€\n\t".format(salesPrice) + description + "\n\t$availableItems auf Lager"
    }

    fun addStock(items: StockUnit) {
        stockUnits.add(items)
        testAmountLength(availableItems)
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