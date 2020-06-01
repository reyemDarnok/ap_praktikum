package order

import groups.ShoppingCart

data class Order(
        val shoppingCart: ShoppingCart,
        val address: Address
)