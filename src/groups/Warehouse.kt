package groups

import product.Product
import product.StockUnit
import review.LimitedReview
import review.PlainReview
import review.SmartReview
import kotlin.random.Random

/**
 * A representation of a warehouse that stores [Product]
 */
class Warehouse {
    /**
     * A List of the products in the warehouse
     */
    private val products: MutableList<Product> = mutableListOf()

    /**
     * A String containing each [Product] on its own line, as reported by [Product.showNice]
     */
    val listOfProducts: String
        get() {
            val description = StringBuilder()
            for (product in products) {
                description.append(product.showNice()).append('\n')
            }
            return description.toString()
        }

    /**
     * A String containing the description of each [Product], with linebreaks in between.
     * The description is as reported by [Product.description]
     */
    val productDescriptions: String
        get() {
            val description = StringBuilder()
            for (product in products) {
                description.append(product.description).append('\n')
            }
            return description.toString()
        }

    /**
     * Checks whether a [Product] with the given name is in this warehouse
     * @param productName The name of the product
     * @return True if a product has the given name, false otherwise
     */
    fun hasProduct(productName: String): Boolean {
        return products.any { it.productName == productName }
    }

    /**
     * Gets a [Product] in this warehouse with the given name
     * @param productName The name of the product to return
     * @return A Product with the given name or null, if no such product is in the warehouse
     */
    fun getProductByName(productName: String): Product? {
        return products.find { it.productName == productName }
    }

    /**
     * Adds a new Product to this warehouse with the given properties (and a random review of each type)
     * @param productName The name of the new [Product]
     * @param basePrice The price the warehouse paid for the new [Product]
     * @param productDescription The description of the new [Product]
     * @param chargeOnTop The difference between the sales and basePrice of the new [Product] in %, that is for a win margin of 2
     *                      Set this value to 100.0
     * @param initialStockUnits How many [StockUnit]s are created for this product. THIS IS NOT THE INITIAL AMOUNT OF PRODUCTS
     */
    fun fillWarehouse(productName: String, basePrice: Double, productDescription: String,
                      chargeOnTop: Double = 50.0, initialStockUnits: Int = 3) {
        val product = Product.create(productName, basePrice, basePrice * (1 + chargeOnTop / 100), productDescription)
        for (i in 0 until initialStockUnits) {
            product.addStock(StockUnit(Random.nextInt(0, 100), Random.nextInt(0, 20)))
        }
        product.addReview(PlainReview(Random.nextInt()))
        product.addReview(LimitedReview(Random.nextDouble(), String(Random.nextBytes(Random.nextInt(0, 255)))))
        product.addReview(SmartReview(Random.nextInt(0, 5)))
        products.add(product)
    }
}