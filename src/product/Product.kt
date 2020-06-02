package product

import review.Review
import kotlin.math.log10

/**
 * Represents a Product
 * @property productName The name of this Product
 * @property basePrice The price this product has been bought for
 * @property salesPrice The price this product will sell for
 * @property description A description of this product.
 */
open class Product(val productName: String, val basePrice: Double, open val salesPrice: Double, val description: String) {
    /**
     * Tracks the longest names, prices and amounts to align the output nicely without iterating through all products
     * every time
     */
    companion object Maxima {
        /**
         * The length of the longest name of any product
         */
        var longestName: Int = 0

        /**
         * The length of the longest price of any product. 15.3€ would be 5 (decimal places are padded to 2)
         */
        var longestPrice: Int = 0

        /**
         * The length of the longest Amount. 115 would be 3, 17 would be 2
         */
        var longestAmount: Int = 0

        /**
         * Checks if [name] is longer than the current longest and updates [longestName] if that is the case
         * @param name The name to test
         */
        fun testNameLength(name: String) {
            if (name.length > longestName) {
                longestName = name.length
            }
        }

        /**
         * Checks if [price] is longer than the current longest and updates [longestPrice] if that is the case
         * @param price The price to test
         */
        fun testPriceLength(price: Double) {
            val priceLength = log10(price).toInt() + 4
            if (priceLength > longestPrice) {
                longestPrice = priceLength
            }
        }

        /**
         * Checks if [amount] is longer than the current longest and updates [longestAmount] if that is the case
         * @param amount The amount to test
         */
        fun testAmountLength(amount: Int) {
            val amountLength = log10(amount.toDouble()).toInt() + 1
            if (amountLength > longestAmount) {
                longestAmount = amountLength
            }
        }

        /**
         * Tests all attributes of [product] if they are longer than the current Maxima and updates accordingly
         * @param product The [Product] to check
         */
        fun testAll(product: Product) {
            testAmountLength(product.availableItems)
            testNameLength(product.productName)
            testPriceLength(product.salesPrice)
        }
    }

    /**
     * A list of all [Review]s for this product
     */
    val reviews: MutableList<Review> = mutableListOf()
    var stockUnits: MutableList<StockUnit> = mutableListOf()

    init {
        // this leakage is fine, testAll does not cause it to persist and only checks already initialised attributes
        testAll(this)
    }


    /**
     * The difference between sales and basePrice
     */
    val profitPerItem: Double
        get() = salesPrice - basePrice


    /**
     * How much buying all items of this product costed
     */
    val valueOfAllItems: Double
        get() = availableItems * basePrice


    /**
     * How much selling all items of this product will bring
     */
    val salesValueOfAllItems: Double
        get() = availableItems * salesPrice

    /**
     * How many items are available of this product
     */
    open val availableItems: Int
        get() = stockUnits.sumBy { it.quantity }


    /**
     * Prints
     * <[productName]> <[salesPrice]> x <[availableItems]>
     * padded to align with the other products
     */
    override fun toString(): String {
        val nameLength: Int = productName.length
        val priceLength = log10(salesPrice).toInt() + 4
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

    /**
     * Prints a full rundown of the product in a human readable format
     */
    fun describe(): String {
        return productName + "\n\tPreis: %.2f€\n\t".format(salesPrice) + description + "\n\t$availableItems auf Lager"
    }

    /**
     * Adds Items to this product
     * @param items The [StockUnit] to add to this product
     */
    fun addStock(items: StockUnit) {
        stockUnits.add(items)
        testAmountLength(availableItems)
    }

    /**
     * Adds a review to this product
     * @param review The review to add
     */
    fun addReview(review: Review) {
        reviews.add(review)
    }

    /**
     * Filters out any [StockUnit] that either is expired of is empty
     */
    fun cleanStock() {
        stockUnits = stockUnits.filterNot { it.isExpired || it.quantity <= 0 } as MutableList<StockUnit>
    }

    /**
     * Checks whether a given quantity of items is available
     * @param preferredQuantity The amount to check
     * @return Whether [preferredQuantity] <= [availableItems]
     */
    fun isPreferredQuantityAvailable(preferredQuantity: Int): Boolean {
        return availableItems >= preferredQuantity
    }

    /**
     * Attempts to take [preferredQuantity] items of this product
     * @param preferredQuantity The requested Amount of items
     * @return The amount of items actually removed. Differs from [preferredQuantity] if that quantity is not available,
     *          in which case it is equal to the amount actually removed
     */
    open fun takeItems(preferredQuantity: Int): Int {
        var remainingQuantity = preferredQuantity
        stockUnits.sortBy { it.daysBeforeExpiration }
        for (unit in stockUnits) {
            if (unit.quantity > remainingQuantity) {
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