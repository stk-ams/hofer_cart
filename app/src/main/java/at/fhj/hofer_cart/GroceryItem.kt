package at.fhj.hofer_cart

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
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
@Entity
data class GroceryItem(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "amount") val amount: Int,
    @ColumnInfo(name = "unit") val unit: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "category") val category: FoodCategory
) : Serializable{
    override fun toString(): String {
        return "$amount $unit $name"
    }
}

