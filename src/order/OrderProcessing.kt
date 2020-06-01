package order

import product.Product

class OrderProcessing {
    /*** Basisstruktur für verkettete Liste ***/
    // Erstes Element der verketten Liste
    var first: OrderNode? = null

    // Ein Knoten der verketteten Liste
    data class OrderNode(val order: Order, var next: OrderNode?)

    /*** Eigenschaften ***/
    // ist die Liste leer?
    val isEmpty: Boolean = false // TODO

    // Sind die Items absteigend sortiert?
    fun isSorted(): Boolean = false // TODO

    // Berechnet den Gesamtwert aller Bestellungen
    val totalVolume: Double = 0.0 // TODO

    // Anzahl der Bestellungen
    val size: Int = 0 // TODO

    // ** Funktionen zum Einfügen **

    // Bestellung hinten anhängen
    fun append(order: Order) {
        // TODO
    }

    // Sortiert die Bestellung ein. Details siehe Aufgabentext
    fun insertBeforeSmallerVolumes(order: Order) {
        // TODO
    }

    // Sortiert nach Auftragsvolumen
    fun sortyByVolume() {
        // TODO
    }

    // Funktionen zum Verarbeiten der Liste

    // Verarbeitet die erste Bestellung und entfernt diese aus der Liste
    fun processFirst() {
        // TODO
    }

    // Vearbeitet die Bestellung mit dem höchsten Auftragsvolumen
    // und entfernt diese aus der Liste
    fun processHighest() {
        // TODO
    }

    // Verarbeitet alle Aufträge für die Stadt in einem Rutsch
    // und entfernt diese aus der Lite
    fun processAllFor(city: String) {
        // TODO
    }

    // Verarbeite alle Bestellungen. Die Liste ist danach leer.
    fun processAll() {
        // TODO
    }

    // ** Funktionen zum Analysieren**

    // Analysiert alle order mit der analyzer Funktion
    fun analyzeAll(analyzer: (Order) -> String): String = "" // TODO

    // Prüft, ob für ein Produkt einer der Bestellungen
    // die predicate Funktion erfüllt wird
    fun anyProduct(predicate: (Product) -> Boolean): Boolean = false // TODO

    // Erzeugt ein neues order.OrderProcessing Objekt, in dem nur noch
    // order.Order enthalten, für die die predicate Funktion true liefert
    fun filter(predicate: (Order) -> Boolean): OrderProcessing = this // TODO
}