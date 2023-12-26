package at.fhj.hofer_cart

import java.util.UUID

enum class FoodCategory {
    FRUITS,
    VEGETABLES,
    MEAT,
    BAKERY,
    BEVERAGES,
    SNACKS,
    FROZEN
}

data class GroceryItem(
    val id: String = UUID.randomUUID().toString(),
    val amount: Int,
    val unit: String,
    val name: String,
    val category: FoodCategory
) {
    override fun toString(): String {
        return "$name $category"
    }
}

