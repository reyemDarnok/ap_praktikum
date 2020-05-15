import java.lang.NumberFormatException
import kotlin.random.Random

object App {
    private val warehouse: Warehouse = Warehouse()
    private val shoppingCart: ShoppingCart = ShoppingCart()
    @JvmStatic
    fun main(args: Array<String>) {

        for (i in 0..10){
            warehouse.fillWarehouse("Name $i", Random.nextDouble(0.0, 20.0),"Description $i")
        }
        print(warehouse.productDescriptions())

        mainloop@ while(true){
            println("H=Hinzufügen   K=Alles kaufen  I=Info Z=Einkaufs-Liste zeigen  L=Liste leeren  E=Exit" )
            when(readLine() ?: "No Input"){
                "E","e" -> {
                    println("Auf wiedersehen")
                    break@mainloop
                }
                "H","h" -> add()
                "I","i" -> info()
                "K","k" -> buy()
                "L","l" -> emptyBasket()
                "Z","z" -> showBasket()
                else -> println("Bitte einen validen Befehl eingeben (EHIKLZ)")
            }
        }
    }

    private fun showBasket() {
        print(shoppingCart.listOfAllProducts())
    }

    private fun emptyBasket() {
        shoppingCart.clear()
        println("Einkaufswagen geleert")
    }

    private fun buy() {
        println("Wirklich kaufen? Der Preis ist %.2f€ (Ny)".format(shoppingCart.totalPrice()))
        if(readLine() == "y"){
            println("Für %.2f€ Euro eingekauft".format(shoppingCart.buyEverything()))
        } else {
            println("Vorgang abgebrochen")
        }
    }

    private fun info() {
        print(warehouse.productDescriptions())
    }

    private fun add() {
        print("Produktname: ")
        val productName = readLine() ?: ""
        if(productName == ""){
            println("Bitte einen Namen angeben!")
            return
        }
        val product = warehouse.getProductByName(productName)
        if(product == null){
            println("Das Produkt ist leider nicht vorhanden")
            return
        }
        print("Menge: ")
        val quantityString = readLine() ?: ""
        if(quantityString == ""){
            println("Bitte eine Menge angeben!")
            return
        }
        val quantity: Int
        try {
            quantity = quantityString.toInt()
        } catch (e: NumberFormatException){
            println("$quantityString ist keine ganze Zahl")
            return
        }
        if(quantity <= 0){
            println("Die Menge muss positiv sein")
        }
        val index = getIndexByProductName(productName)
        if(index != -1){
            val newQuantity = shoppingCart.productAndQuantityList[index].second + quantity
            if(product.isPreferredQuantityAvailable(newQuantity)) {
                shoppingCart.productAndQuantityList[index] = Pair(product, newQuantity)

                if (quantity != 1) {
                    println("$quantity $productName wurden der Einkaufsliste hinzugefügt")
                } else {
                    println("1 $productName wurde der Einkaufsliste hinzugefügt")
                }
            } else {
                println("Es sind leider nur noch ${product.availableItems()} auf Lager")
            }
        } else {
            if(product.isPreferredQuantityAvailable(quantity)) {
                shoppingCart.productAndQuantityList.add(Pair(product, quantity))
                if (quantity != 1) {
                    println("$quantity $productName wurden der Einkaufsliste hinzugefügt")
                } else {
                    println("1 $productName wurde der Einkaufsliste hinzugefügt")
                }
            } else {
                println("Es sind leider nur noch ${product.availableItems()} auf Lager")
            }
        }
    }

    private fun getIndexByProductName(productName: String): Int{
        for(i in 0 until shoppingCart.productAndQuantityList.size){
            var (product, quantity) = shoppingCart.productAndQuantityList[i]
            if(product.productName == productName){
                return i
            }
        }
        return -1
    }

}