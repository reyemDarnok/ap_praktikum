class ShoppingCart {
    val productAndQuantityList: ArrayList<Pair<Product, Int>> = ArrayList()

    val allProductsAvailable: Boolean
        get() {
            for ((product, quantity) in productAndQuantityList) {
                if (!product.isPreferredQuantityAvailable(quantity)) {
                    return false
                }
            }
            return true
        }

    val totalPrice: Double
        get() {
            var price = 0.0
            for ((product, quantity) in productAndQuantityList) {
                price += product.salesPrice * quantity
            }
            return price
        }

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
        var price = 0.0
        for ((product, quantity) in productAndQuantityList) {
            price += product.takeItems(quantity) * product.salesPrice
        }
        clear()
        return price
    }
}