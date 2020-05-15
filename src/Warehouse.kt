import kotlin.random.Random

class Warehouse {
    private val products: MutableList<Product> = mutableListOf()

    val listOfProducts: String
        get() {
            val description = StringBuilder()
            for (product in products) {
                description.append(product.toString()).append('\n')
            }
            return description.toString()
        }

    val productDescriptions: String
        get() {
            val description = StringBuilder()
            for (product in products) {
                description.append(product.description).append('\n')
            }
            return description.toString()
        }

    fun hasProduct(productName: String): Boolean {
        return products.any { it.productName == productName }
    }

    fun getProductByName(productName: String): Product? {
        return products.find { it.productName == productName }
    }

    fun fillWarehouse(productName: String, basePrice: Double, productDescription: String,
                      chargeOnTop: Double = 50.0, initialStockUnits: Int = 3) {
        val product = Product(productName, basePrice, basePrice * (1 + chargeOnTop / 100), productDescription)
        for (i in 0 until initialStockUnits) {
            product.addStock(StockUnit(Random.nextInt(0, 20), Random.nextInt(0, 20)))
        }
        product.addReview(PlainReview(Random.nextInt()))
        product.addReview(LimitedReview(Random.nextDouble(), String(Random.nextBytes(Random.nextInt(0, 255)))))
        product.addReview(SmartReview(Random.nextInt(0, 5)))
        products.add(product)
    }
}