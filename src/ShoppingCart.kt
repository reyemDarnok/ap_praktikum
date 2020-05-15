class ShoppingCart {
    val productAndQuantityList: ArrayList<Pair<Product, Int>> = ArrayList()

    fun allProductsAvailable(): Boolean{
        for((product, quantity) in productAndQuantityList){
            if(!product.isPreferredQuantityAvailable(quantity)){
                return false
            }
        }
         return true
    }

    fun totalPrice(): Double{
        var price = 0.0
        for((product, quantity) in productAndQuantityList){
            price += product.getSalesPrice() * quantity
        }
        return price
    }

    fun listOfAllProducts(): String{
        var string: StringBuilder = StringBuilder()
        for (pair: Pair<Product, Int> in productAndQuantityList){
            var (product, quantity) = pair
            string.append(quantity).append('\t')
                .append(product.productName).append('\t')
                .append("%.2f€\n".format(product.getSalesPrice() * quantity))
        }
        return string.toString()
    }

    fun clear(){
        productAndQuantityList.clear()
    }

    fun buyEverything(): Double{
        var price = 0.0
        for((product, quantity) in productAndQuantityList){
            price += product.takeItems(quantity) * product.getSalesPrice()
        }
        clear()
        return price
    }
}