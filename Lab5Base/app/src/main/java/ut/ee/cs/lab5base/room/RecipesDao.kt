package ut.ee.cs.lab5base.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipesDao {

    @Query("SELECT title FROM recipe")
    fun loadRecipeTitles(): Array<String>


    @Query("SELECT * FROM recipe")
    fun loadAllRecipe(): Array<RecipeEntity>

    @Query("SELECT * FROM recipe where id = :id")
    fun loadRecipeByID(id: Int): Array<RecipeEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipes(vararg recipes: RecipeEntity)

}