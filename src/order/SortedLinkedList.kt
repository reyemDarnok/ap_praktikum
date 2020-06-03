package order

class SortedLinkedList<Type : Comparable<Type>> : MutableList<Type> {

    data class Node<Type>(val content: Type, var previous: Node<Type>? = null, var next: Node<Type>? = null)

    private var first: Node<Type>? = null

    override val size: Int
        get() {
            var i = 0
            for (content in this) {
                i++
            }
            return i
        }

    override fun contains(element: Type): Boolean {
        for (content in this) {
            if (content == element) {
                return true
            }
        }
        return false
    }

    override fun containsAll(elements: Collection<Type>): Boolean {
        val found = BooleanArray(elements.size) { false }
        for (content in this) {
            for (index in elements.indices) {
                if (content == elements.elementAt(index)) {
                    found[index] = true
                }
            }
        }
        return found.all { it }
    }

    /**
     * Returns the element at the specified index in the list.
     */
    override fun get(index: Int): Type {
        iterator().apply {
            for (i in 1 until index) {
                next()
            }
            return next()
        }
    }

    /**
     * Returns the index of the first occurrence of the specified element in the list, or -1 if the specified
     * element is not contained in the list.
     */
    override fun indexOf(element: Type): Int {
        var index = 0
        iterator().apply {
            while (hasNext()) {
                if (next() == element) {
                    return index
                }
                index++
            }
        }
        return -1
    }

    override fun isEmpty(): Boolean {
        return first == null
    }

    override fun iterator(): MutableIterator<Type> {
        return SortedLinkedListIterator(this)
    }

    class SortedLinkedListIterator<Type : Comparable<Type>>(private val parent: SortedLinkedList<Type>) : MutableListIterator<Type> {
        private var previous: Node<Type>? = null
        private var nextIndex = 0
        private val previousIndex: Int
            get() = nextIndex - 2
        private var next = parent.first
        private var lastNodeReturned: Node<Type>? = null


        /**
         * Returns `true` if the iteration has more elements.
         */
        override fun hasNext(): Boolean {
            return next != null
        }

        /**
         * Returns the next element in the iteration.
         */
        override fun next(): Type {
            next?.let {
                previous = it
                next = it.next
                nextIndex++
                lastNodeReturned = it
                return it.content
            } ?: throw NoSuchElementException("No Element left in the list to retrieve")
        }

        /**
         * Removes from the underlying collection the last element returned by this iterator.
         */
        override fun remove() {
            lastNodeReturned?.let {
                previous = it.previous
                next = it.next
                previous?.let {
                    it.next = next
                } ?: let {
                    parent.first = next
                }
                next?.let { it.previous = previous }
            } ?: throw IllegalStateException("No Element previously requested")
        }

        /**
         * Returns `true` if there are elements in the iteration before the current element.
         */
        override fun hasPrevious(): Boolean {
            return previous != null
        }

        /**
         * Returns the index of the element that would be returned by a subsequent call to [next].
         */
        override fun nextIndex(): Int {
            next?.let { return nextIndex } ?: throw NoSuchElementException("No next Element exists to get the index of")

        }

        /**
         * Returns the previous element in the iteration and moves the cursor position backwards.
         */
        override fun previous(): Type {
            previous?.let {
                next = it
                previous = it.previous
                nextIndex--
                return it.content
            } ?: throw NoSuchElementException("No previous Element left in the list to retrieve")
        }

        /**
         * Returns the index of the element that would be returned by a subsequent call to [previous].
         */
        override fun previousIndex(): Int {
            previous?.let {
                return previousIndex
            } ?: throw NoSuchElementException("No previous Element left in the list to retrieve the index of")
        }

        /**
         * Adds the specified element [element] into the underlying collection immediately before the element that would be
         * returned by [next], if any, and after the element that would be returned by [previous], if any.
         * (If the collection contains no elements, the new element becomes the sole element in the collection.)
         * The new element is inserted before the implicit cursor: a subsequent call to [next] would be unaffected,
         * and a subsequent call to [previous] would return the new element. (This call increases by one the value \
         * that would be returned by a call to [nextIndex] or [previousIndex].)
         */
        override fun add(element: Type) {
            next?.let { if (element > it.content) throw java.lang.IllegalArgumentException("element is larger than next element") }
            previous?.let {
                if (element < it.content) throw IllegalArgumentException("element is smaller than previous element")
                it.next = Node(element, it, next)
                previous = it.next
                next?.previous = it
            } ?: let {
                parent.first = Node(element, null, next)
            }
            nextIndex++
        }

        /**
         * Replaces the last element returned by [next] or [previous] with the specified element [element].
         */
        override fun set(element: Type) {
            lastNodeReturned?.apply {
                next?.let { if (element > it.content) throw java.lang.IllegalArgumentException("element is larger than next element") }
                previous?.let {
                    if (element < it.content) throw IllegalArgumentException("element is smaller than previous element")
                    it.next = Node(element, it, next)
                    next?.previous = it.next
                }
            } ?: throw IllegalStateException("Cannot change last returned element if no element had been returned")
        }

    }

    /**
     * Returns the index of the last occurrence of the specified element in the list, or -1 if the specified
     * element is not contained in the list.
     */
    override fun lastIndexOf(element: Type): Int {
        var index = 0
        var lastIndex = -1
        iterator().apply {
            while (hasNext()) {
                if (next() == element) {
                    lastIndex = index
                }
                index++
            }
        }
        return lastIndex
    }

    /**
     * Adds the specified element to the end of this list.
     *
     * @return `true` because the list is always modified as the result of this operation.
     */
    override fun add(element: Type): Boolean {
        listIterator().apply {
            @Suppress("ControlFlowWithEmptyBody")
            while (hasNext() && next() < element) {
            }
            if (hasPrevious()) {
                previous()
            }
            add(element)
        }
        return true
    }

    /**
     * Inserts an element into the list at the specified [index].
     */
    override fun add(index: Int, element: Type) {
        listIterator().apply {
            for (i in 1 until index) next()
            add(element)
        }
    }

    /**
     * Inserts all of the elements of the specified collection [elements] into this list at the specified [index].
     *
     * @return `true` if the list was changed as the result of the operation.
     */
    override fun addAll(index: Int, elements: Collection<Type>): Boolean {
        listIterator().apply {
            for (i in 1 until index) next()
            for (element in elements) add(element)
        }
        return elements.isEmpty()
    }

    /**
     * Adds all of the elements of the specified collection to the end of this list.
     *
     * The elements are appended in the order they appear in the [elements] collection.
     *
     * @return `true` if the list was changed as the result of the operation.
     */
    override fun addAll(elements: Collection<Type>): Boolean {
        for (element in elements) add(element)
        return elements.isEmpty()
    }

    override fun clear() {
        first = null
    }

    override fun listIterator(): MutableListIterator<Type> {
        return SortedLinkedListIterator(this)
    }

    override fun listIterator(index: Int): MutableListIterator<Type> {
        return SortedLinkedListIterator(this).apply {
            for (i in 1 until index) next()
        }
    }

    override fun remove(element: Type): Boolean {
        listIterator().apply {
            while (hasNext()) {
                if (next() == element) {
                    remove()
                    return true
                }
            }
        }
        return false
    }

    override fun removeAll(elements: Collection<Type>): Boolean {
        var modified = false
        listIterator().apply {
            while (hasNext()) {
                if (elements.contains(next())) {
                    remove()
                    modified = true
                }
            }
        }
        return modified
    }

    /**
     * Removes an element at the specified [index] from the list.
     *
     * @return the element that has been removed.
     */
    override fun removeAt(index: Int): Type {
        listIterator().apply {
            for (i in 0 until index) next()
            val toReturn = next()
            remove()
            return toReturn
        }
    }

    override fun retainAll(elements: Collection<Type>): Boolean {
        var modified = false
        listIterator().apply {
            while (hasNext()) {
                if (!elements.contains(next())) {
                    modified = true
                    remove()
                }
            }
        }
        return modified
    }

    /**
     * Replaces the element at the specified position in this list with the specified element.
     *
     * @return the element previously at the specified position.
     */
    override fun set(index: Int, element: Type): Type {
        listIterator().apply {
            for (i in 0 until index) {
                next()
            }
            val toReturn = next()
            set(element)
            return toReturn
        }
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<Type> {
        var current = first
        val subList = SortedLinkedList<Type>()
        for (index in 0 until fromIndex) {
            current = current?.next ?: throw NoSuchElementException("fromIndex has to be in range for this list")
        }
        for (index in fromIndex..toIndex) {
            subList.add(current?.content ?: throw NoSuchElementException("toIndex has to be in range for this list"))
            current = current.next
        }
        return subList
    }
}