package product

class DiscountProduct(productName: String, basePrice: Double, salesPrice: Double, description: String, val discount: DiscountType)
    : Product(productName, basePrice, salesPrice, description) {

    init {
        testPriceLength(super.salesPrice * discount.discountFactor)
    }

    override val salesPrice: Double
        get() = super.salesPrice * discount.discountFactor
}