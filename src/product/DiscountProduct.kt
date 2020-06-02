package product

/**
 * Represents a discounted Product
 * @property salesPrice The price this product is sold at BEFORE the discount is applied.
 * @property discount The [DiscountType] this product is affected by. This modification applies on top of salesPrice
 */
class DiscountProduct(productName: String, basePrice: Double, salesPrice: Double, description: String, val discount: DiscountType)
    : Product(productName, basePrice, salesPrice, description) {

    init {
        testPriceLength(super.salesPrice * discount.discountFactor)
    }

    override val salesPrice: Double
        get() = super.salesPrice * discount.discountFactor
}