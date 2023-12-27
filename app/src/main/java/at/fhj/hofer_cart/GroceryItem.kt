package at.fhj.hofer_cart

import androidx.compose.ui.platform.LocalContext
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.UUID

enum class FoodCategory(val stringId: Int) {
    FRUITS(R.string.fruits),
    VEGETABLES(R.string.vegetables),
    MEAT(R.string.meat),
    BAKERY(R.string.bakery),
    BEVERAGES(R.string.beverages),
    SNACKS(R.string.snacks),
    FROZEN(R.string.frozen),
    OTHER(R.string.other)
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

