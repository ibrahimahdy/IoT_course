package ut.ee.cs.lab5base.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import ut.ee.cs.lab5base.R
import ut.ee.cs.lab5base.RecipeViewModel
import ut.ee.cs.lab5base.RecipesAdapter

class MainActivity : AppCompatActivity() {



    private lateinit var model: RecipeViewModel
    lateinit var recipesAdapter: RecipesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // link to ViewModel

        model = ViewModelProviders.of(this).get(RecipeViewModel::class.java)
        recipesAdapter = RecipesAdapter(model, this)
        recipeListView.adapter = recipesAdapter


        addButton.setOnClickListener {
            startActivity(Intent(this, NewRecipeActivity::class.java))
        }
    }


    override fun onResume(){
        super.onResume()
        model.refresh()
        recipesAdapter.notifyDataSetChanged()
    }

    fun openDetails(recipeId: Int) {
        // launch details activity and pass recipe ID

        val i = Intent(this, RecipeDetailsActivity::class.java)
        i.putExtra("recipeId", recipeId)
        startActivity(i)

    }
}
