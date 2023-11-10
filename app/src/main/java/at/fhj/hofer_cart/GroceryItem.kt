package at.fhj.hofer_cart

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
    val amount: Int,
    val unit: String,
    val name: String,
    val category: FoodCategory
) {
    override fun toString(): String {
        return "$name $category"
    }
}

