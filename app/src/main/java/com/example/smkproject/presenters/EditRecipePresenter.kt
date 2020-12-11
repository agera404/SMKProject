package com.example.smkproject.presenters

import android.util.Log
import androidx.lifecycle.Lifecycle
import com.example.smkproject.common.MainRepository
import com.example.smkproject.models.Ingredient
import com.example.smkproject.models.Recipe
import com.example.smkproject.models.Tag
import com.example.smkproject.views.EditRecipeView
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Error
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EditRecipePresenter(var view: EditRecipeView): BasePresenter() {
    var recipe: Recipe? = null

    init {
        if (MainRepository.selectedRecipe != null) {
            recipe = MainRepository.selectedRecipe
        }else{
            recipe = Recipe(title =  "",describe = "",dateTime = "",tags = "", ingredients ="")
        }
    }

    fun isRecipeNotNull(): Boolean {
        if (recipe != null) {
            return true
        }
        return false
    }
    private var errorCode: String?=null
    fun isSaveError(): Boolean{
        recipe?.let {
            if (it.title.isEmpty() || it.title.isBlank()){
                errorCode = ErrorSaveCode.ERROR_TITLE
                return true
            }
            if (it.describe.isEmpty() || it.describe.isBlank()){
                errorCode = ErrorSaveCode.ERROR_DESCRIBE
                return true
            }
            if (it.ingredients.isEmpty() || it.ingredients.isBlank()){
                errorCode = ErrorSaveCode.ERROR_INGR
                return true
            }
            if (it.tags.isEmpty() || it.ingredients.isBlank()){
                errorCode = ErrorSaveCode.ERROR_TAGS
                return true
            }
        }
        return false
    }
    fun saveRecipe(): String?{
        recipe?.ingredients = MainRepository.selectedRecipe?.ingredients ?: return ErrorSaveCode.ERROR_INGR
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")
        val date = dateFormat.format(currentDate)
        recipe?.dateTime = date
        recipe?.tags = recipe?.tags?.trim(' ', ',','\n') ?: ""

        if (!isSaveError()){
            launch {
                MainRepository.saveRecipe(recipe!!)
            }
            return null
        }else{
            Log.d("mLog", "Error save code: $errorCode")
            val code = errorCode
            errorCode = null
            return code
        }

    }

    fun getTags(): List<String>{
        var tags: List<Tag>? = null
        runBlocking {
            tags = MainRepository.loadTags()
        }
        var listTags: ArrayList<String> = arrayListOf()
        for (tag in tags!!){
            listTags.add(tag.tag)
        }
        return listTags
    }
}
object ErrorSaveCode{
    val ERROR_TITLE: String = "001"
    val ERROR_DESCRIBE: String = "002"
    val ERROR_INGR: String = "003"
    val ERROR_DATETIME: String = "004"
    val ERROR_TAGS: String = "005"
}