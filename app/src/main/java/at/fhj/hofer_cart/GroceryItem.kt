package at.fhj.hofer_cart

import java.io.Serializable
import java.util.UUID

enum class FoodCategory {
    FRUITS,
    VEGETABLES,
    MEAT,
    BAKERY,
    BEVERAGES,
    SNACKS,
    FROZEN,
    OTHER
}

data class GroceryItem(
    val id: String = UUID.randomUUID().toString(),
    val amount: Int,
    val unit: String,
    val name: String,
    val category: FoodCategory
) : Serializable{
    override fun toString(): String {
        return "$name $category"
    }
}

