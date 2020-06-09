package product

class InfiniteProduct(productName: String = "", basePrice: Double = 0.0, salesPrice: Double = 1.0, description: String = "") : Product(productName, basePrice, salesPrice, description) {
    override val availableItems: Int
        get() {
            return Int.MAX_VALUE
        }

    override fun takeItems(preferredQuantity: Int): Int {
        return preferredQuantity
    }
}