package ut.ee.cs.lab5base.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_recipe_details.*
import ut.ee.cs.lab5base.R

class RecipeDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)


        buttonBack.setOnClickListener {
            finish()
        }
    }
}
