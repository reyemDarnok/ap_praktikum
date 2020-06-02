package product

/**
 * Represents a discounted Product
 * @property salesPrice The price this product is sold at BEFORE the discount is applied.
 * @property discount The [DiscountType] this product is affected by. This modification applies on top of salesPrice
 */
class DiscountProduct(productName: String, basePrice: Double, salesPrice: Double, description: String, val discount: DiscountType)
    : Product(productName, basePrice, salesPrice, description) {

    private val discountActual: DiscountType? = discount

    init {
        testAll(this)
    }

    override val salesPrice: Double
        get() {
            return if (discountActual == null) {
                0.0
            } else {
                super.salesPrice * discountActual.discountFactor
            }
        }
}