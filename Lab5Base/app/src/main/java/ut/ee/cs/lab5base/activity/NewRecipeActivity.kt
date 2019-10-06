package ut.ee.cs.lab5base.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_new_recipe.*
import ut.ee.cs.lab5base.LocalDbClient
import ut.ee.cs.lab5base.R
import ut.ee.cs.lab5base.room.RecipeEntity
import ut.ee.cs.lab5base.room.RecipesDatabase

class NewRecipeActivity : AppCompatActivity() {

    var db : RecipesDatabase
    init {
        db = LocalDbClient.getDatabase(this)!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_recipe)


        saveButton.setOnClickListener {

            var RecipeTitle = findViewById<EditText>(R.id.titleEditText)
            var RecipeContent = findViewById<EditText>(R.id.contentEditText)
            var RecipeAuthor = findViewById<EditText>(R.id.authorEditText)
            var RecipePrepTime = findViewById<EditText>(R.id.prepTimeEditText)

            var myRecipe = RecipeEntity(0, RecipeTitle.text.toString(), RecipeContent.text.toString(), RecipeAuthor.text.toString(), java.lang.Double.parseDouble(RecipePrepTime.text.toString()))
            db.getRecipesDao().insertRecipes(myRecipe)
            finish()
        }
    }
}
