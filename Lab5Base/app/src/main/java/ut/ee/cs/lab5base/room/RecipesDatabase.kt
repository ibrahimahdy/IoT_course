package ut.ee.cs.lab5base.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(RecipeEntity::class), version = 1)
abstract class RecipesDatabase : RoomDatabase()  {

    abstract fun getRecipesDao(): RecipesDao
}