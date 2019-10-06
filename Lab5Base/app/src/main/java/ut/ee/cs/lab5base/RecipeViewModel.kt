package ut.ee.cs.lab5base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ut.ee.cs.lab5base.room.RecipeEntity

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    var entityArray : Array<RecipeEntity> = arrayOf(RecipeEntity("Pasta Bolognese"), RecipeEntity("Hawaii Pizza"), RecipeEntity("Swedish meatballs"))

    fun refresh(){
        // Reload dataset from DB, put it in in-memory list

    }
}