package product

/**
 * The different types of discounts for [DiscountProduct]
 * @property typeName: The name of the Discount
 * @property discountFactor: How much is still left of the price after application. For a discount of 20%
 *                              this value would be 1 - 20% = 0.8
 */
enum class DiscountType(val typeName: String, val discountFactor: Double) {
    SummerSale("Sommerschlussverkauf", 0.8),
    MHD("Kurzes MHD", 0.9),
    SellEverything("Alles muss raus", 0.5),
    NoRebate("Kein Rabatt", 1.0)
}