package groups

import product.Product

/**
 * Represents a shopping cart to shop at
 */
class ShoppingCart {
    /**
     * Contains all Products with their quantities
     */
    val productAndQuantityList: ArrayList<Pair<Product, Int>> = ArrayList()

    /**
     * Check if everything can be bought
     * true if every item in productAndQuantityList is available in its listed quantity, false otherwise
     */
    val allProductsAvailable: Boolean
        get() = !productAndQuantityList.any { !it.first.isPreferredQuantityAvailable(it.second) }


    /**
     * The total price to buy everything in the cart
     */
    val totalPrice: Double
        get() = productAndQuantityList.sumByDouble { it.first.salesPrice * it.second }

    /**
     * A String containing all Products in a line each of the format
     * <quantity>   <name>  <price>
     */
    val listOfAllProducts: String
        get() {
            val string: StringBuilder = StringBuilder()
            for (pair: Pair<Product, Int> in productAndQuantityList) {
                val (product, quantity) = pair
                string.append(quantity).append('\t')
                        .append(product.productName).append('\t')
                        .append("%.2f€\n".format(product.salesPrice * quantity))
            }
            return string.toString()
        }

    /**
     * Removes everything from the shopping cart
     */
    fun clear() {
        productAndQuantityList.clear()
    }


    /**
     * Adds something to the cart. Either increases an existing listing or creates a new,
     * depending on if there is already a listing for the product
     * @param product The Product to add
     * @param quantity The amount of product to add
     * @throws InsufficientProductsException if there are not enough products available
     */
    fun addProduct(product: Product, quantity: Int) {
        productAndQuantityList.find { it.first == product }
                ?.let {
                    val newQuantity = it.second + quantity
                    if (product.isPreferredQuantityAvailable(newQuantity)) {
                        productAndQuantityList.remove(it)
                        productAndQuantityList.add(Pair(product, newQuantity))
                    } else {
                        throw InsufficientProductsException(product.availableItems)
                    }
                } ?: let {
            if (product.isPreferredQuantityAvailable(quantity)) {
                productAndQuantityList.add(Pair(product, quantity))
            } else {
                throw InsufficientProductsException(product.availableItems)
            }
        }
    }

    /**
     * Buys everything to the extent it is available.
     * @return The price for everything bought. THIS CAN DEVIATE FROM totalPrice IF SOME ITEMS ARE UNAVAILABLE
     */
    fun buyEverything(): Double {
        val price = productAndQuantityList.sumByDouble { it.first.takeItems(it.second) * it.first.salesPrice }
        clear()
        return price
    }
}