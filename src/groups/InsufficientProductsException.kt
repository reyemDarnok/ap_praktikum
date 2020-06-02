package groups

/**
 * To be thrown if less products are available than requested
 * @property availableItems The amount of items still available
 */
class InsufficientProductsException(val availableItems: Int) : Exception()