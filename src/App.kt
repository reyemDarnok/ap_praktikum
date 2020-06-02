import groups.InsufficientProductsException
import groups.ShoppingCart
import groups.Warehouse
import product.Product
import product.ProductNames
import review.Review
import kotlin.math.log10
import kotlin.random.Random

/**
 * The main UI object
 */
object App {
    /**
     * The [Warehouse] in which the products are stored
     */
    private val warehouse: Warehouse = Warehouse()

    /**
     * The [ShoppingCart] for this buying session
     */
    private val shoppingCart: ShoppingCart = ShoppingCart()

    /**
     * The main method for the JVM
     * @param args Completely ignored, but syntactically demanded
     */
    @JvmStatic
    fun main(args: Array<String>) {
        //init the warehouse
        val names = mutableListOf(*ProductNames.values())
        for (i in 0 until 10) {
            val rand = Random.nextInt(names.size)
            warehouse.fillWarehouse(names[rand].name, Random.nextDouble(0.0, 20.0), names[rand].description)
            names.remove(names[rand])
        }

        //UI mainloop
        mainloop@ while (true) {
            warehouse.forEach { showNice(it) }
            println("H=Hinzufügen   K=Alles kaufen  I=Info Z=Einkaufs-Liste zeigen  L=Liste leeren  E=Exit")
            when (readLine() ?: "No Input") {
                "E", "e" -> {
                    println("Auf wiedersehen")
                    break@mainloop
                }
                "H", "h" -> add()
                "I", "i" -> info()
                "K", "k" -> buy()
                "L", "l" -> emptyBasket()
                "Z", "z" -> showBasket()
                else -> println("Bitte einen validen Befehl eingeben (EHIKLZ)")
            }
            println()
        }
    }

    /**
     * Prints the contents of [shoppingCart] to stdout
     */
    private fun showBasket() {
        print(shoppingCart.listOfAllProducts)
    }

    /**
     * Removes everything from [shoppingCart] and informs the user of this via stdout
     */
    private fun emptyBasket() {
        shoppingCart.clear()
        println("Einkaufswagen geleert")
    }

    /**
     * Asks the user whether he really wants to buy, then either buys or not, depending on answer.
     * Communicates via stdin / stdout
     */
    private fun buy() {
        println("Wirklich kaufen? Der Preis ist %.2f€ (Ny)".format(shoppingCart.totalPrice))
        if (readLine() == "y") {
            println("Für %.2f€ Euro eingekauft".format(shoppingCart.buyEverything()))
        } else {
            println("Vorgang abgebrochen")
        }
    }

    /**
     * Prints a long view of a requested Product to stdout, including review average and best and worst reviews
     */
    private fun info() {
        print("Produktname:")
        val productName = readLine() ?: ""
        val product = warehouse.getProductByName(productName)
        if (product == null) {
            println("Das Produkt ist leider nicht vorhanden")
            return
        }
        println(product.describe())
        if (product.reviews.isNotEmpty()) {
            val worstReview: Review = product.reviews.min() ?: product.reviews[0]
            val bestReview: Review = product.reviews.max() ?: product.reviews[0]
            val sumOfRatings = product.reviews.sumBy { it.stars() }
            println("Durchschnittliche Bewertung: %.1f".format(sumOfRatings.toDouble() / product.reviews.size))
            println("Beste Bewertung:")
            println('\t' + bestReview.info())
            println("Schlechteste Bewertung:")
            println('\t' + worstReview.info())
        }
    }

    /**
     * Adds a requested Product to the [shoppingCart] in the requested amount. Does some error checking
     */
    private fun add() {
        print("Produktname: ")
        val productName = readLine() ?: ""
        val product = warehouse.getProductByName(productName)
        if (product == null) {
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
        } catch (e: NumberFormatException) {
            println("$quantityString ist keine ganze Zahl")
            return
        }
        if (quantity <= 0) {
            println("Die Menge muss positiv sein")
        }
        try {
            shoppingCart.addProduct(product, quantity)
            if (quantity != 1) {
                println("$quantity $productName wurden der Einkaufsliste hinzugefügt")
            } else {
                println("1 $productName wurde der Einkaufsliste hinzugefügt")
            }
        } catch (e: InsufficientProductsException) {
            println("Es sind leider nur noch ${e.availableItems} auf Lager")
        }
    }

    /**
     * Prints
     * <productName> <salesPrice> x <availableItems>
     *  of [product] padded to align with the other products
     *  @param product The product to show
     */
    private fun showNice(product: Product) {
        val nameLength: Int = product.productName.length
        val priceLength = log10(product.salesPrice).toInt() + 4
        val amountLength = log10(product.availableItems.toDouble()).toInt() + 1
        val string = StringBuilder()
        for (i in 0 until (Product.longestName - nameLength)) {
            string.append(' ')
        }
        string.append(product.productName).append('\t')
        for (i in 0 until (Product.longestPrice - priceLength)) {
            string.append(' ')
        }
        string.append("%.2f€ ".format(product.salesPrice)).append("\tx\t")
        for (i in 0 until (Product.longestAmount - amountLength)) {
            string.append(' ')
        }
        string.append(product.availableItems)
        println(string)
    }

}