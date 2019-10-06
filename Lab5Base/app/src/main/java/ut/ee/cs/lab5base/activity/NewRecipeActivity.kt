package ut.ee.cs.lab5base.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_new_recipe.*
import ut.ee.cs.lab5base.R

class NewRecipeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_recipe)


        saveButton.setOnClickListener {
            // Fetch the values from UI,
            // Store them in DB

            finish()
        }
    }
}
