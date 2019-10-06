package ut.ee.cs.lab5base.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_recipe_details.*
import ut.ee.cs.lab5base.LocalDbClient
import ut.ee.cs.lab5base.R
import ut.ee.cs.lab5base.room.RecipeEntity
import ut.ee.cs.lab5base.room.RecipesDatabase

class RecipeDetailsActivity : AppCompatActivity() {

    var db : RecipesDatabase
    init {
        db = LocalDbClient.getDatabase(this)!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)

        var RecipeTitle = findViewById<TextView>(R.id.title)
        var RecipeContent = findViewById<TextView>(R.id.content)
        var RecipeAuthor = findViewById<TextView>(R.id.author)
        var RecipePrepTime = findViewById<TextView>(R.id.time)

        val theId:Int = intent.getIntExtra("recipeId",0)

        var entityArray : Array<RecipeEntity> = db.getRecipesDao().loadRecipeByID(theId)

        RecipeTitle.setText(entityArray[0].title)
        RecipeContent.setText(entityArray[0].content)
        RecipePrepTime.setText(entityArray[0].prepTime.toString())
        RecipeAuthor.setText(entityArray[0].author)


        buttonBack.setOnClickListener {
            finish()
        }
    }
}
