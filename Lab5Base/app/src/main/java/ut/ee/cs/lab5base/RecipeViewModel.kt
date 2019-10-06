package ut.ee.cs.lab5base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ut.ee.cs.lab5base.room.RecipeEntity
import ut.ee.cs.lab5base.room.RecipesDatabase

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    var entityArray : Array<RecipeEntity> = arrayOf()
    var db : RecipesDatabase


    init {
        db = LocalDbClient.getDatabase(application.applicationContext)!!
    }

    fun refresh(){

        entityArray = db.getRecipesDao().loadAllRecipe()

    }
}