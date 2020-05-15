import kotlin.random.Random

class Warehouse {
    private val products: ArrayList<Product> = ArrayList()

    val productDescriptions: String
        get() {
            var description = ""
            for (product in products) {
                description += product.toString() + "\n"
            }
            return description
        }

    fun hasProduct(productName: String): Boolean {
        for (product: Product in products) {
            if (product.productName == productName) {
                return true
            }
        }
        return false
    }

    fun getProductByName(productName: String): Product? {
        for (product: Product in products) {
            if (product.productName == productName) {
                return product
            }
        }
        return null
    }

    fun fillWarehouse(productName: String, basePrice: Double, productDescription: String,
                      chargeOnTop: Double = 50.0, initialStockUnits: Int = 3) {
        val product = Product(productName, basePrice, basePrice * (1 + chargeOnTop / 100), productDescription)
        val discountProduct = DiscountProduct(productName + "Discounted", basePrice, basePrice * (1 + chargeOnTop / 100), productDescription, DiscountType.SummerSale)
        for (i in 0..initialStockUnits) {
            product.addStock(StockUnit(Random.nextInt(0, 20), Random.nextInt(0, 20)))
            discountProduct.addStock(StockUnit(Random.nextInt(0, 20), Random.nextInt(0, 20)))
        }
        product.addReview(PlainReview(Random.nextInt()))
        product.addReview(LimitedReview(Random.nextDouble(), String(Random.nextBytes(Random.nextInt(0, 255)))))
        product.addReview(SmartReview(Random.nextInt(0, 5)))
        products.add(discountProduct)
        products.add(product)
    }
}