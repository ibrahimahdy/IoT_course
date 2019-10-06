package ut.ee.cs.lab5base.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import ut.ee.cs.lab5base.LocalDbClient
import ut.ee.cs.lab5base.R
import ut.ee.cs.lab5base.RecipeViewModel
import ut.ee.cs.lab5base.RecipesAdapter
import ut.ee.cs.lab5base.room.RecipeEntity
import ut.ee.cs.lab5base.room.RecipesDatabase

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
        val i = Intent(this, RecipeDetailsActivity::class.java)
        i.putExtra("recipeId", recipeId)
        startActivity(i)

    }
}
