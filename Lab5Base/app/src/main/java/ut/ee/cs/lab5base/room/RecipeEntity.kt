package ut.ee.cs.lab5base.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe")
data class RecipeEntity(@PrimaryKey(autoGenerate = true) var id: Int, var title: String?, var content: String?, var author: String?, var prepTime: Double?) {


}