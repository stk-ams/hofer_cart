package at.fhj.hofer_cart


import androidx.room.*

@Dao
interface GroceryItemDao {
    @Query("SELECT * FROM GroceryItem")
    fun getAll(): List<GroceryItem>

    @Insert
    fun insert(groceryItem: GroceryItem)

    @Query("DELETE FROM GroceryItem WHERE id = :id")
    fun deleteByLocalQuestionId(id: String)
}