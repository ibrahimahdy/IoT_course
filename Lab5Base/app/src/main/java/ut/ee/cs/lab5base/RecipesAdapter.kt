package ut.ee.cs.lab5base


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.single_recipe.view.*
import ut.ee.cs.lab5base.activity.MainActivity
import ut.ee.cs.lab5base.room.RecipeEntity

class RecipesAdapter(var model: RecipeViewModel,  var activity: MainActivity) : BaseAdapter() {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(parent?.context)
            view = layoutInflater.inflate(R.layout.single_recipe, parent, false)
        } else{
            view = convertView
        }

        val recipe = getItem(position)

        view.recipeTitleTextview.text = recipe.title
        // TODO: add other fiels

        view.setOnClickListener {
//            activity.openDetails(recipe.id)
        }

        return view
    }

    override fun getItem(position: Int): RecipeEntity {
        return model.entityArray[position]
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode().toLong()
    }

    override fun getCount(): Int {
        return model.entityArray.size
    }
}