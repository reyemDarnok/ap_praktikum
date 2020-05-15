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

    fun buyEverything(): Double {
        val price = productAndQuantityList.sumByDouble { it.first.takeItems(it.second) * it.first.salesPrice }
        clear()
        return price
    }
}