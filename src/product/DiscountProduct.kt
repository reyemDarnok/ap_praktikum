package product

/**
 * Represents a discounted Product
 * @property salesPrice The price this product is sold at BEFORE the discount is applied.
 * @property discount The [DiscountType] this product is affected by. This modification applies on top of salesPrice
 */
class DiscountProduct private constructor(productName: String, basePrice: Double, salesPrice: Double, description: String, val discount: DiscountType)
    : Product(productName, basePrice, salesPrice, description) {

    companion object Factory {
        fun create(productName: String, basePrice: Double, salesPrice: Double, description: String, discount: DiscountType): DiscountProduct {
            testPriceLength(salesPrice * discount.discountFactor)
            testNameLength(productName)
            return DiscountProduct(productName, basePrice, salesPrice, description, discount)
        }
    }

    override val salesPrice: Double
        get() = super.salesPrice * discount.discountFactor
}