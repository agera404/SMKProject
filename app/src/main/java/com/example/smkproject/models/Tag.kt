package com.example.smkproject.models

class Tag(_id: Long, _tag: String, _count: Int) {
    var id: Long
    var tag: String
    var count: Int
    var recipes: ArrayList<Recipe> = arrayListOf()
    init {
        id = _id
        tag = _tag.capitalize()
        count = _count
    }
    fun setRecipe(recipe: Recipe){
        var tags = recipe.tags?.split(' ')?.toTypedArray()
        for (t in tags!!){
            var temp = t.replace(" ", "")
            if (this.tag.equals(temp)) recipes.add(recipe)
        }
    }
}