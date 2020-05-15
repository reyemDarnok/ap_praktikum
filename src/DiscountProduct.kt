class DiscountProduct(productName: String, basePrice: Double, salesPrice: Double, description: String, val discount: DiscountType)
    : Product(productName, basePrice, salesPrice, description) {

    override val salesPrice: Double
        get() = super.salesPrice * (1 - discount.discountFactor)
}