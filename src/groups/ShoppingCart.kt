package groups

import product.Product

class ShoppingCart {
    val productAndQuantityList: ArrayList<Pair<Product, Int>> = ArrayList()

    val allProductsAvailable: Boolean
        get() = !productAndQuantityList.any { !it.first.isPreferredQuantityAvailable(it.second) }


    val totalPrice: Double
        get() = productAndQuantityList.sumByDouble { it.first.salesPrice * it.second }

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

    fun clear() {
        productAndQuantityList.clear()
    }


    /**
     * @throws InsufficientProductsException if there are not enough products available
     */
    fun addProduct(product: Product, quantity: Int) {
        val index = productAndQuantityList.indexOfFirst { it.first == product }
        if (index != -1) {
            val newQuantity = productAndQuantityList[index].second + quantity
            if (product.isPreferredQuantityAvailable(newQuantity)) {
                productAndQuantityList[index] = Pair(product, newQuantity)
            } else {
                throw InsufficientProductsException(product.availableItems)
            }
        } else {
            if (product.isPreferredQuantityAvailable(quantity)) {
                productAndQuantityList.add(Pair(product, quantity))
            } else {
                throw InsufficientProductsException(product.availableItems)
            }
        }
    }

    fun buyEverything(): Double {
        val price = productAndQuantityList.sumByDouble { it.first.takeItems(it.second) * it.first.salesPrice }
        clear()
        return price
    }
}