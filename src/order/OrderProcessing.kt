package order

import product.Product

class OrderProcessing : Iterable<Order> {
    /*** Basisstruktur für verkettete Liste ***/
    // Erstes Element der verketten Liste
    var first: OrderNode? = null

    // Ein Knoten der verketteten Liste
    data class OrderNode(val order: Order, var next: OrderNode?)

    /*** Eigenschaften ***/
    // ist die Liste leer?
    val isEmpty: Boolean
        get() = first == null

    // Sind die Items absteigend sortiert?
    fun isSorted(): Boolean {
        var previous: Order = first?.order ?: return true
        for (order in this) {
            if (order > previous) {
                return false
            }
            previous = order
        }
        return true
    }

    // Berechnet den Gesamtwert aller Bestellungen
    val totalVolume: Double
        get() {
            var sum = 0.0
            for (order in this) {
                sum += order.shoppingCart.totalPrice
            }
            return sum
        }

    // Anzahl der Bestellungen
    val size: Int
        get() {
            var num = 0
            for (order in this) {
                num += 1
            }
            return num
        }

    // ** Funktionen zum Einfügen **

    // Bestellung hinten anhängen
    fun append(order: Order) {
        var last: OrderNode? = first
        while (last?.next != null) {
            last = last.next
        }
        if (last != null) {
            last.next = OrderNode(order, null)
        } else {
            first = OrderNode(order, null)
        }
    }

    // Sortiert die Bestellung ein. Details siehe Aufgabentext
    fun insertBeforeSmallerVolumes(order: Order) {
        if (first == null) {
            first = OrderNode(order, null)
        } else {
            var last: OrderNode? = first
            var previous: OrderNode? = null
            while (last != null && last.order > order) {
                previous = last
                last = last.next
            }
            if (previous != null) {
                previous.next = OrderNode(order, last)
            } else {
                first = OrderNode(order, last)
            }
        }
    }

    // Sortiert nach Auftragsvolumen
    /**
     * Uses Mergesort
     */
    fun sortByVolume() {
        first = mergesort(first)
    }

    private fun mergesort(head: OrderNode?): OrderNode? {
        if (head?.next == null) {
            return head
        }

        val middle = getMiddle(head)
        val nextOfMiddle = middle?.next
        middle?.next = null

        val left = mergesort(head)
        val right = mergesort(nextOfMiddle)
        return sortedMerge(left, right)
    }

    private fun sortedMerge(left: OrderNode?, right: OrderNode?): OrderNode? {
        if (left == null) {
            return right
        }
        if (right == null) {
            return left
        }
        return if (left.order >= right.order) {
            left.next = sortedMerge(left.next, right)
            left
        } else {
            right.next = sortedMerge(left, right.next)
            right
        }
    }

    private fun getMiddle(head: OrderNode?): OrderNode? {
        if (head == null) {
            return head
        }
        var slow = head
        var fast = head

        while (fast?.next != null && fast.next?.next != null) {
            slow = slow?.next
            fast = fast.next?.next
        }

        return slow
    }

    // Funktionen zum Verarbeiten der Liste

    // Verarbeitet die erste Bestellung und entfernt diese aus der Liste
    fun processFirst() {
        first?.order?.shoppingCart?.buyEverything()
        first = first?.next
    }

    // Vearbeitet die Bestellung mit dem höchsten Auftragsvolumen
    // und entfernt diese aus der Liste
    fun processHighest() {
        if (isEmpty) {
            return
        }
        var beforeHighest: OrderNode? = null
        var highest: OrderNode = first!!
        var previous: OrderNode
        var current: OrderNode = first!!
        while (current.next != null) {
            previous = current
            current = current.next!!
            if (highest.order < current.order) {
                beforeHighest = previous
                highest = current
            }
        }
        highest.order.shoppingCart.buyEverything()
        if (beforeHighest != null) {
            beforeHighest.next = highest.next
        } else {
            first = highest.next
        }

    }

    // Verarbeitet alle Aufträge für die Stadt in einem Rutsch
    // und entfernt diese aus der Lite
    fun processAllFor(city: String) {
        if (isEmpty) {
            return
        }
        while (first?.order?.address?.city == city) {
            first?.order?.shoppingCart?.buyEverything()
            first = first?.next
        }
        var previous: OrderNode? = first
        if (first?.next != null) {
            var current: OrderNode? = first?.next
            while (current != null) {
                if (current.order.address.city == city) {
                    current.order.shoppingCart.buyEverything()
                    previous?.next = current.next
                } else {
                    previous = current
                }
                current = current.next
            }
        }
    }

    // Verarbeite alle Bestellungen. Die Liste ist danach leer.
    fun processAll() {
        for (order in this) {
            order.shoppingCart.buyEverything()
        }
        first = null
    }

    // ** Funktionen zum Analysieren**

    // Analysiert alle order mit der analyzer Funktion
    fun analyzeAll(analyzer: (Order) -> String): String {
        val out = StringBuilder()
        for (order in this) {
            out.append(analyzer.invoke(order))
        }
        return out.toString()
    }

    // Prüft, ob für ein Produkt einer der Bestellungen
    // die predicate Funktion erfüllt wird
    fun anyProduct(predicate: (Product) -> Boolean): Boolean {
        for (order in this) {
            for (pair in order.shoppingCart.productAndQuantityList) {
                if (predicate.invoke(pair.first)) {
                    return true
                }
            }
        }
        return false
    }

    // Erzeugt ein neues order.OrderProcessing Objekt, in dem nur noch
    // order.Order enthalten, für die die predicate Funktion true liefert
    fun filter(predicate: (Order) -> Boolean): OrderProcessing {
        val out = OrderProcessing()
        for (order in this) {
            if (predicate.invoke(order)) {
                out.append(order)
            }
        }
        return out
    }

    /**
     * Returns an iterator over the elements of this object.
     */
    override fun iterator(): Iterator<Order> {
        return OrderProcessingIterator(this)
    }

    class OrderProcessingIterator(orderProcessing: OrderProcessing) : Iterator<Order> {
        var current = orderProcessing.first

        /**
         * Returns `true` if the iteration has more elements.
         */
        override fun hasNext(): Boolean {
            return current != null
        }

        /**
         * Returns the next element in the iteration.
         */
        override fun next(): Order {
            val out = current ?: throw NoSuchElementException()
            current = current!!.next
            return out.order
        }
    }
    }