package com.example.smkproject


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.view.children
import com.example.smkproject.common.DBHelper
import com.example.smkproject.presenters.EditRecipePresenter
import com.example.smkproject.views.EditRecipeView
import kotlinx.android.synthetic.main.fragment_edit_recipe.*
import kotlinx.android.synthetic.main.ingredient_container.*


class EditRecipeFragment : Fragment(),EditRecipeView {
    private var dbHelper: DBHelper?= null
    private var presenter: EditRecipePresenter? = null

    var ingredientEditText: EditText? = null
    var amountEditText: EditText? = null
    var delIngrButton:Button? = null

    var title:String = "Новый рецепт"
    var describ: String = ""
    var tags: String = ""
    var countIngr:Int = 1



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_recipe, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dbHelper = DBHelper(context)
        presenter = EditRecipePresenter(this, dbHelper!!)
        countIngr = listIngredients.childCount
        if (countIngr == 1){
            var view = listIngredients.children.elementAt(0) as LinearLayout
            ingredientEditText = view.children.elementAt(1) as EditText
            amountEditText = view.children.elementAt(2) as EditText
            delIngrButton = view.children.elementAt(0) as Button
            delIngrButton?.setOnClickListener { deliteIngredient(delIngrButton!!) }

        }
        setListeners()
        textChangedLiseners()

    }


    fun setListeners(){



        addRecipeButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                presenter?.saveRecipe(title,describ,tags)
                presenter?.onBack()
            }
        })
        //слушаем события titleRecipeEditText
        titleRecipeEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                title = titleRecipeEditText.text.toString()

            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }
        })
        //слушаем события describRecipeEditText
        describRecipeEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

                describ = describRecipeEditText.text.toString()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }
        })
        //слушаем события describRecipeEditText
        tagsEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                tags = tagsEditText.text.toString()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }
        })
        ingredient.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }
        })
        amountIngredient.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }
        })
        //spinnerIngredient <- нужно следить за ним
    }
    private fun deliteIngredient(button: Button){
        if(countIngr>2){
            var parent: ViewGroup = button.parent as ViewGroup
            var grandParent = parent.parent as ViewGroup
            var ingr = parent.children.elementAt(1) as EditText
            var amount = parent.children.elementAt(2) as EditText
            if(!ingr.text.isEmpty() && !amount.text.isEmpty()){

                ingredientEditText = (grandParent.children.last() as LinearLayout).children.elementAt(1) as EditText
                amountEditText = (grandParent.children.last() as LinearLayout).children.elementAt(2) as EditText
                grandParent.removeView(parent)
                countIngr-=1
            }
        }
    }
    fun newIngredientView(){
        if(ingredientEditText?.text!!.length>0 && amountIngredient.text.length>0){
            var view = LayoutInflater.from(context).inflate(R.layout.ingredient_container,null)
            countIngr+=1
            var button = (view as LinearLayout).children.elementAt(0) as Button
            ingredientEditText = (view as LinearLayout).children.elementAt(1) as EditText
            amountEditText = (view as LinearLayout).children.elementAt(2) as EditText
            textChangedLiseners()
            button.isClickable = true
            button.setOnClickListener{deliteIngredient(button)}

            listIngredients.addView(view)
        }

    }
    fun textChangedLiseners(){
        ingredientEditText!!.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) = newIngredientView()
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int){}
        })

        amountEditText!!.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) = newIngredientView()
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int){}
        })
    }
    override fun navigateToFragment(){
        activity?.onBackPressedDispatcher
    }

    override fun saveRecipe(){
        if(describ.length<=0) presenter?.onBack()
        if(describ.length>0){
            if(title.length<=0) title="Новый рецепт"
            presenter?.saveRecipe(title, describ, tags)
            presenter?.onBack()
        }

    }

}