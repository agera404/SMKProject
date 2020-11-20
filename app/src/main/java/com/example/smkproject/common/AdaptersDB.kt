package com.example.smkproject.common

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.smkproject.models.Recipe
import com.example.smkproject.models.Tag

class DBHelper internal constructor(context: Context?) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME, null,
        DATABASE_VERSION
    ) {


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_RECIPES)
        db.execSQL(CREATE_TABLE_TAGS)
        db.execSQL(CREATE_TABLE_RECIPESTAGS)
        db.execSQL(CREATE_TABLE_INGREDIENTS)
        db.execSQL(CREATE_TABLE_RECIPEINGREDIENT)

    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("drop table if exists ${TABLE_RECIPES}")
        db.execSQL("drop table if exists ${TABLE_TAGS}")
        db.execSQL("drop table if exists ${TABLE_RECIPESTAGS}")
        db.execSQL("drop table if exists ${TABLE_INGREDIENTS}")
        db.execSQL("drop table if exists ${TABLE_RECIPEINGREDIENT}")
        onCreate(db)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "RecipesDB"
        const val TABLE_RECIPES = "recipes"
        const val TABLE_TAGS = "tags"
        const val TABLE_RECIPESTAGS = "recipes_tags"
        const val TABLE_INGREDIENTS = "ingredients"
        const val TABLE_RECIPEINGREDIENT = "recipes_ingredients"

        const val CREATE_TABLE_RECIPES = "create table " + TABLE_RECIPES + "(" +
                RecipesDB.KEY_ID + " integer primary key," +
                RecipesDB.KEY_TITLE + " text," +
                RecipesDB.KEY_DESCRIB + " text," +
                RecipesDB.KEY_DATETIME + " text, " +
                RecipesDB.KEY_INGREDIENT + " text, " +
                RecipesDB.KEY_TAGS + " text" + ")"
        const val CREATE_TABLE_TAGS = "create table " + TABLE_TAGS + "(" +
                TagsAdapterDB.KEY_ID + " integer primary key," +
                TagsAdapterDB.KEY_TAG + " text, " +
                TagsAdapterDB.KEY_COUNT + " int" +
                ")"
        const val CREATE_TABLE_RECIPESTAGS = "create table " + TABLE_RECIPESTAGS +
                "(" +
                RecipeTagDB.KEY_ID + " integer primary key, " +
                RecipeTagDB.KEY_IDRECIPE + " int, " +
                RecipeTagDB.KEY_IDTAG + " int, " +
                " FOREIGN KEY (" + RecipeTagDB.KEY_IDRECIPE + ") REFERENCES " + TABLE_RECIPES + "(" + RecipesDB.KEY_ID + "), " +
                "FOREIGN KEY (" + RecipeTagDB.KEY_IDTAG + ") REFERENCES " + TABLE_TAGS + "(" + TagsAdapterDB.KEY_ID + ")" +
                ")"
        const val CREATE_TABLE_INGREDIENTS = "create table " + TABLE_INGREDIENTS + "(" +
                IngredientsDB.KEY_ID + " integer primary key, " +
                IngredientsDB.KEY_INGREDIENT + " text, " +
                IngredientsDB.KEY_COUNT + " int" + ")"
        const val CREATE_TABLE_RECIPEINGREDIENT = "create table " + TABLE_RECIPEINGREDIENT + "(" +
                RecipeIngredientDB.KEY_ID + " integer primary key, " +
                RecipeIngredientDB.KEY_IDINGR + " int, " +
                RecipeIngredientDB.KEY_IDRECIPE + " int, " +
                RecipeIngredientDB.KEY_AMOUNT + " real, "  +
                RecipeIngredientDB.KEY_UNIT + " text, "+
                " FOREIGN KEY (" + RecipeIngredientDB.KEY_IDRECIPE + ") REFERENCES " + TABLE_RECIPES + "(" + RecipesDB.KEY_ID + "), " +
                " FOREIGN KEY (" + RecipeIngredientDB.KEY_IDINGR + ") REFERENCES " + TABLE_INGREDIENTS + "(" + IngredientsDB.KEY_ID + ")" +
                ")"

    }
}

class RecipesDB(_dbHelper: DBHelper?) {
    companion object {
        const val KEY_ID = "_id"
        const val KEY_TITLE = "title"
        const val KEY_DESCRIB = "describ"
        const val KEY_INGREDIENT = "ingredient"
        const val KEY_DATETIME = "dateTime"
        const val KEY_TAGS = "tags"

    }

    private var dbHelper: DBHelper? = _dbHelper

    fun insert(recipe: Recipe): Long {
        val db = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, recipe.title);
        contentValues.put(KEY_DESCRIB, recipe.describ);
        contentValues.put(KEY_DATETIME, recipe.dateTime);
        contentValues.put(KEY_TAGS, recipe.tags)
        contentValues.put(KEY_INGREDIENT, recipe.stringIngredient)
        //Вставляем запись в БД и возвращаем ее ID. Если запись не вставиллась, возвращает -1
        return db.insert(DBHelper.TABLE_RECIPES, null, contentValues);
    }

    fun saveRecipe(recipe: Recipe) {

        var idRecipe = insert(recipe)
        var idTag: Long = -1
        for (_tag in recipe.tags.split(",").toTypedArray()) {
            var tag = _tag.replace(" ", "")
            idTag = TagsAdapterDB(dbHelper).getIdTag(tag)
            TagsAdapterDB(dbHelper).updateCount(idTag, true)
            RecipeTagDB(dbHelper).insert(idRecipe, idTag)
        }
        var idIngr: Long = -1
        for (i in recipe.ingredients) {
            idIngr = IngredientsDB(dbHelper).getId(i.ingredient)
            IngredientsDB(dbHelper).updateCount(idIngr, true)
            RecipeIngredientDB(dbHelper).insert(idRecipe, idIngr, i.amount, i.unit)
        }
    }

    //Возвращает все рецепты из БД
    fun loadRecipes(): ArrayList<Recipe> {
        var recipes: ArrayList<Recipe> = ArrayList()

        val database = dbHelper!!.writableDatabase
        val cursor: Cursor =
            database.query(DBHelper.TABLE_RECIPES, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(KEY_ID)
            val titleIndex = cursor.getColumnIndex(KEY_TITLE)
            val describIndex = cursor.getColumnIndex(KEY_DESCRIB)
            val dateTimeIndex = cursor.getColumnIndex(KEY_DATETIME)
            val tagsIndex = cursor.getColumnIndex(KEY_TAGS)
            val ingredientIndex = cursor.getColumnIndex(KEY_INGREDIENT)
            do {
                recipes.add(
                    Recipe(
                        cursor.getLong(idIndex),
                        cursor.getString(titleIndex).toString(),
                        cursor.getString(describIndex).toString(),
                        cursor.getString(dateTimeIndex).toString(),
                        cursor.getString(tagsIndex).toString(),
                        cursor.getString(ingredientIndex).toString()
                    )
                )

            } while (cursor.moveToNext())
        } else Log.d("mLog", "нечего выгружать из бд")

        cursor.close()

        return recipes
    }

    fun loadRecipes(_cursor: Cursor): ArrayList<Recipe> {
        var recipes: ArrayList<Recipe> = ArrayList()

        val database = dbHelper!!.writableDatabase
        val cursor: Cursor = _cursor
        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(KEY_ID)
            val titleIndex = cursor.getColumnIndex(KEY_TITLE)
            val describIndex = cursor.getColumnIndex(KEY_DESCRIB)
            val dateTimeIndex = cursor.getColumnIndex(KEY_DATETIME)
            val tagsIndex = cursor.getColumnIndex(KEY_TAGS)
            val ingredientIndex = cursor.getColumnIndex(KEY_INGREDIENT)
            do {
                recipes.add(
                    Recipe(
                        cursor.getLong(idIndex),
                        cursor.getString(titleIndex).toString(),
                        cursor.getString(describIndex).toString(),
                        cursor.getString(dateTimeIndex).toString(),
                        cursor.getString(tagsIndex).toString(),
                        cursor.getString(ingredientIndex).toString()
                    )
                )

            } while (cursor.moveToNext())
        } else Log.d("mLog", "нечего выгружать из бд")

        cursor.close()

        return recipes
    }

    //Возвращает все рецепты из БД в лог
    fun loadRecipesInLog() {
        val database = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        val cursor: Cursor =
            database.query(DBHelper.TABLE_RECIPES, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(KEY_ID)
            val titleIndex = cursor.getColumnIndex(KEY_TITLE)
            val describIndex = cursor.getColumnIndex(KEY_DESCRIB)
            val dateTimeIndex = cursor.getColumnIndex(KEY_DATETIME)
            val tagsIndex = cursor.getColumnIndex(KEY_TAGS)
            do {
                Log.d(
                    "mLog", "ID = " + cursor.getInt(idIndex) +
                            ", title = " + cursor.getString(titleIndex) +
                            ", descrip = " + cursor.getString(describIndex) +
                            ", dateTime = " + cursor.getString(dateTimeIndex) +
                            ", tags = " + cursor.getString(tagsIndex)
                )
            } while (cursor.moveToNext())
        } else Log.d("mLog", "0 rows")

        cursor.close()
    }

}

class TagsAdapterDB(_dbHelper: DBHelper?) {
    companion object {
        const val KEY_ID = "_id"
        const val KEY_TAG = "tag"
        const val KEY_COUNT = "count"
    }

    private var dbHelper: DBHelper? = _dbHelper

    fun updateCount(idTag: Long, boolean: Boolean) {
        var db = dbHelper!!.writableDatabase
        var cursor = db.query(
            DBHelper.TABLE_TAGS,
            null,
            "$KEY_ID = ?",
            arrayOf(idTag.toString()),
            null,
            null,
            null,
            null
        )
        cursor.moveToFirst()
        var count = cursor.getInt(cursor.getColumnIndex(KEY_COUNT))
        if (true) count += 1
        else {
            if (count < 0) count = 0
            else count -= 1
        }
        var contentValues = ContentValues()
        contentValues.put(KEY_COUNT, count)
        db.update(DBHelper.TABLE_TAGS, contentValues, "$KEY_ID = ?", arrayOf(idTag.toString()))
    }

    fun loadTags(): ArrayList<Tag> {
        var tags: ArrayList<Tag> = ArrayList()

        val database = dbHelper!!.writableDatabase
        val cursor: Cursor =
            database.query(DBHelper.TABLE_TAGS, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(KEY_ID)
            val tagIndex = cursor.getColumnIndex(KEY_TAG)
            val countIndex = cursor.getColumnIndex(KEY_COUNT)

            do {

                var tag = Tag(
                    cursor.getLong(idIndex),
                    cursor.getString(tagIndex),
                    cursor.getInt(countIndex)
                )
                var recipes = RecipeTagDB(dbHelper).loadRecipeForTag(tag.id)
                tag.recipes = recipes
                tags.add(tag)

            } while (cursor.moveToNext())
        } else Log.d("mLog", "нечего выгружать из бд")
        cursor.close()
        return tags
    }

    fun getIdTag(tag: String): Long {
        //тут нужна проверка, если id не вернется
        var id = findTag(tag)
        if (id >= 0) {
            return id
        } else {
            id = insertTag(tag)
            return id
        }
    }

    fun findTag(tag: String): Long {
        var idTag: Long = -1
        val database = dbHelper!!.writableDatabase
        var cursor = database.query(
            DBHelper.TABLE_TAGS, null,
            "$KEY_TAG = ?"
            , arrayOf(tag), null, null, null
        );


        if (cursor.moveToNext())
            idTag = cursor.getLong(cursor.getColumnIndex(KEY_ID))
        return idTag
    }

    fun insertTag(tag: String): Long {
        val database = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TAG, tag)
        contentValues.put(KEY_COUNT, 0)
        return database.insert(DBHelper.TABLE_TAGS, null, contentValues)
    }


}

class RecipeTagDB(_dbHelper: DBHelper?) {
    companion object {
        const val KEY_ID = "_id"
        const val KEY_IDTAG = "id_tags"
        const val KEY_IDRECIPE = "id_recipe"
    }

    private var dbHelper: DBHelper? = _dbHelper
    fun check(idRecipe: Long, idTag: Long): Boolean{
        val database = dbHelper!!.writableDatabase
        var cursor = database.rawQuery("SELECT * FROM ${DBHelper.TABLE_RECIPESTAGS}" +
                " WHERE ($KEY_IDRECIPE = $idRecipe) AND ($KEY_IDTAG = $idTag)",null)
        if (cursor.moveToFirst() && cursor.count>0) return true
        return false
    }
    fun insert(idRecipe: Long, idTag: Long): Long {
        if(!check(idRecipe,idTag)){
            val database = dbHelper!!.writableDatabase
            val contentValues = ContentValues()
            contentValues.put(KEY_IDRECIPE, idRecipe)
            contentValues.put(KEY_IDTAG, idTag)
            return database.insert(DBHelper.TABLE_RECIPESTAGS, null, contentValues);
        }
        return -1
    }

    fun loadRecipeForTag(idTag: Long): ArrayList<Recipe> {
        val database = dbHelper!!.writableDatabase
        var cursor =
            database.rawQuery(
                "SELECT * FROM ${DBHelper.TABLE_RECIPESTAGS} LEFT JOIN ${DBHelper.TABLE_RECIPES} ON $KEY_IDRECIPE = ${DBHelper.TABLE_RECIPES}.${RecipesDB.KEY_ID} WHERE $KEY_IDTAG = $idTag",
                null
            )
        return RecipesDB(dbHelper).loadRecipes(cursor)

    }
}

class IngredientsDB(_dbHelper: DBHelper?) {
    companion object {
        const val KEY_ID = "_id"
        const val KEY_INGREDIENT = "ingredient"
        const val KEY_COUNT = "count"
    }

    private var dbHelper: DBHelper? = _dbHelper

    fun getId(ingredient: String): Long {
        //тут нужна проверка, если id не вернется
        var id = findIngr(ingredient)
        if (id >= 0) {
            return id
        } else {
            id = insert(ingredient)
            return id
        }
    }

    fun findIngr(ingredient: String): Long {
        var id: Long = -1
        val database = dbHelper!!.writableDatabase
        var cursor = database.query(
            DBHelper.TABLE_INGREDIENTS, null,
            "${IngredientsDB.KEY_INGREDIENT} = ?"
            , arrayOf(ingredient), null, null, null
        );


        if (cursor.moveToNext())
            id = cursor.getLong(cursor.getColumnIndex(IngredientsDB.KEY_ID))
        return id
    }

    fun insert(ingredient: String): Long {
        val database = dbHelper!!.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(IngredientsDB.KEY_INGREDIENT, ingredient)
        contentValues.put(IngredientsDB.KEY_COUNT, 0)
        return database.insert(DBHelper.TABLE_INGREDIENTS, null, contentValues)
    }

    fun updateCount(id: Long, boolean: Boolean) {
        var db = dbHelper!!.writableDatabase
        var cursor = db.query(
            DBHelper.TABLE_INGREDIENTS,
            null,
            "${TagsAdapterDB.KEY_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
            null
        )
        cursor.moveToFirst()
        var count = cursor.getInt(cursor.getColumnIndex(TagsAdapterDB.KEY_COUNT))
        if (true) count += 1
        else {
            if (count < 0) count = 0
            else count -= 1
        }
        var contentValues = ContentValues()
        contentValues.put(TagsAdapterDB.KEY_COUNT, count)
        db.update(
            DBHelper.TABLE_INGREDIENTS,
            contentValues,
            "${TagsAdapterDB.KEY_ID} = ?",
            arrayOf(id.toString())
        )
    }

}

class RecipeIngredientDB(_dbHelper: DBHelper?) {
    companion object {
        const val KEY_ID = "_id"
        const val KEY_IDINGR = "idingredient"
        const val KEY_IDRECIPE = "idrecipe"
        const val KEY_AMOUNT = "amount_ingredient"
        const val KEY_UNIT = "unit_ingredient"
    }

    private var dbHelper: DBHelper? = _dbHelper

    fun check(idRecipe: Long, idIngr: Long): Boolean{
        val database = dbHelper!!.writableDatabase
        var cursor = database.rawQuery("SELECT * FROM ${DBHelper.TABLE_RECIPEINGREDIENT}" +
                " WHERE (${KEY_IDRECIPE} = $idRecipe) AND (${KEY_IDINGR} = $idIngr)",null)
        if (cursor.moveToFirst() && cursor.count>0) return true
        return false
    }


    fun insert(idRecipe: Long, idIngr: Long, amount: Double, unit: String): Long {
        if (!check(idRecipe, idIngr)){
            val database = dbHelper!!.writableDatabase
            val contentValues = ContentValues()
            contentValues.put(KEY_IDRECIPE, idRecipe)
            contentValues.put(KEY_IDINGR, idIngr)
            contentValues.put(KEY_AMOUNT, amount)
            contentValues.put(KEY_UNIT, unit)

            return database.insert(DBHelper.TABLE_RECIPEINGREDIENT, null, contentValues);
        }
       return -1
    }
}