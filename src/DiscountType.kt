enum class DiscountType(val typeName: String, val discountFactor: Double) {
    SummerSale("Sommerschluss", 0.2),
    MHD("Kurzes MHD", 0.1),
    SellEverything("Alles muss raus", 0.5),
    NoRebate("Kein Rabatt", 0.0)
}