enum class DiscountType(val typeName: String, val discountFactor: Double) {
    SummerSale("Sommerschlussverkauf", 0.8),
    MHD("Kurzes MHD", 0.9),
    SellEverything("Alles muss raus", 0.5),
    NoRebate("Kein Rabatt", 1.0)
}