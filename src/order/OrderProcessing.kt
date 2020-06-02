package order

import product.Product

/**
 * Represents a linked list of orders (Does not extend MutableList<Order> because of a clash between isEmpty() demanded
 * by MutableList<Order> and isEmpty demanded by task)
 */
class OrderProcessing : MutableIterable<Order> {
    /*** Basic structure for linked list ***/


    /**
     * The head of the linked list
     */
    var first: OrderNode? = null

    /**
     * One node of the list. Standard linkedList node
     */
    data class OrderNode(val order: Order, var next: OrderNode?)

    /*** Properties ***/

    /**
     * Checks whether the list is empty
     */
    val isEmpty: Boolean
        get() = first == null


    /**
     * Checks if this list is sorted
     */
    fun isSorted(): Boolean {
        var previous: Order = first?.order ?: return true //empty lists are sorted
        for (order in this) {
            if (order > previous) {
                return false
            }
            previous = order
        }
        return true
    }

    /**
     * The total volume of the order, that is, the sum of all prices of all orders
     */
    val totalVolume: Double
        get() {
            //return sumByDouble { it.shoppingCart.totalPrice }
            var sum = 0.0
            for (order in this) {
                sum += order.shoppingCart.totalPrice
            }
            return sum
        }

    /**
     * The number of orders in this list
     */
    val size: Int
        get() {
            var num = 0
            for (order in this) {
                num += 1
            }
            return num
        }

    /**
     * Functions for insertions
     */

    /**
     * Append order to last node of list
     * @param order The order to append
     */
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

    /**
     * Inserts the new order where it should be based on its volume.
     * Note that this does not actually make this list sorted.
     * @param order The new order
     */
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

    /**
     * Sorts the list in O(n log n)
     */
    fun sortByVolume() {
        first = mergesort(first)
    }

    /**
     * Divides the list in the middle. Calls itself recursively and sorts in
     * O(n log n)
     * @param head The start of the list to be sorted
     */
    private fun mergesort(head: OrderNode?): OrderNode? {
        if (head?.next == null) {
            return head
        }

        val middle = getMiddle(head)
        val nextOfMiddle = middle?.next
        middle?.next = null

        val left = mergesort(head)
        val right = mergesort(nextOfMiddle)
        return left?.let {
            if (right != null) {
                sortedMerge(it, right)
            } else {
                null
            }
        } ?: right
    }

    /**
     * Merges two sorted lists. The order of the arguments does not matter
     * @param left The first list
     * @param right The second list
     * @return The head of the new merged list
     */
    private fun sortedMerge(left: OrderNode, right: OrderNode): OrderNode? {
        return if (left.order >= right.order) {
            left.next = left.next?.let { sortedMerge(it, right) } ?: right
            left
        } else {
            right.next = right.next?.let { sortedMerge(left, it) } ?: left
            right
        }
    }

    /**
     * Returns the middle of the given list
     * @param head The head of the list
     * @return The middle of the list, rounded down
     */
    private fun getMiddle(head: OrderNode): OrderNode? {
        var slow = head
        var fast = head

        while (fast.next != null && fast.next?.next != null) {
            slow = slow.next
            fast = fast.next?.next
        }

        return slow
    }

    /**
     * Functions to process the list
     */

    /**
     * Processes the first element of the list (call buyEverything() and remove the node)
     */
    fun processFirst() {
        first?.order?.shoppingCart?.buyEverything()
        first = first?.next
    }

    /**
     * Process the highest Element of the list (call buyEvertything() and remove the node)
     */
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

    /**
     * Processes all orders headed to a specific city
     * @param city The name of the city for which the orders should be processed
     */
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

    /**
     * Process all elements in the list. The list will be empty afterwards
     */
    fun processAll() {
        for (order in this) {
            order.shoppingCart.buyEverything()
        }
        first = null
    }

    /**
     * Functions to analyze the list
     */

    /**
     * Adds the results of [analyzer] for every element in this list together
     * @param analyzer Function to be called on every element in this list
     * @return The concatenated results of [analyzer]. There is no separator between results
     */
    fun analyzeAll(analyzer: (Order) -> String): String {
        val out = StringBuilder()
        for (order in this) {
            out.append(analyzer.invoke(order))
        }
        return out.toString()
    }

    /**
     * Checks whether the given function is true for any element of the list
     * @param predicate The function to check with
     * @return true, if [predicate] returns true for any element of the list, false otherwise
     */
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
    override fun iterator(): MutableIterator<Order> {
        return OrderProcessingIterator(this)
    }

    class OrderProcessingIterator(private val parent: OrderProcessing) : MutableIterator<Order> {
        private var beforeReturned: OrderNode? = null
        private var lastReturned: OrderNode? = null
        private var current = parent.first

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
            if (lastReturned != null) {
                beforeReturned = lastReturned
            }
            lastReturned = current
            current = current!!.next
            return out.order
        }

        /**
         * Removes from the underlying collection the last element returned by this iterator.
         * @throws IllegalStateException if no element has been requested beforehand
         */
        override fun remove() {
            if (lastReturned != null) {
                if (beforeReturned != null) {
                    beforeReturned!!.next = current
                } else {
                    parent.first = current
                }
                lastReturned = null
            } else {
                throw IllegalStateException("Can't remove the latest element before any have been requested")
            }
        }
    }
}