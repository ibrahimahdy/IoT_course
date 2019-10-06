package ut.ee.cs.lab5base

import android.content.Context
import androidx.room.Room
import ut.ee.cs.lab5base.room.RecipesDatabase

object LocalDbClient {

    var recipeDb : RecipesDatabase? = null
    fun getDatabase(context: Context) : RecipesDatabase? {

        if (recipeDb == null){

            recipeDb = Room.databaseBuilder(
                context.applicationContext, RecipesDatabase::class.java, "myRecipes")
                .fallbackToDestructiveMigration() // each time schema changes, data is lost!
                .allowMainThreadQueries() // if possible, use background thread instead
                .build()

        }
        return recipeDb
    }

}