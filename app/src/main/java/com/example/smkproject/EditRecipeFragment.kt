package com.example.smkproject


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.core.view.children
import com.example.smkproject.common.DBHelper
import com.example.smkproject.presenters.EditRecipePresenter
import com.example.smkproject.views.EditRecipeView
import kotlinx.android.synthetic.main.fragment_edit_recipe.*
import kotlinx.android.synthetic.main.ingredient_container.*


class EditRecipeFragment : Fragment(), EditRecipeView {
    private var dbHelper: DBHelper? = null
    private var presenter: EditRecipePresenter? = null

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
        initiationIngredients()
        setListeners()
    }

    override fun navigateToFragment() {
        activity?.onBackPressedDispatcher
    }

    var title: String = "Новый рецепт"
    var describ: String = ""
    var tags: String = ""


    private fun setListeners() {

        addRecipeButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                presenter?.saveRecipe(title, describ, tags)
                presenter?.onBack()
                Log.d("mLog", "Click")
            }
        })
        backButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                presenter?.onBack()
                Log.d("mLog", "Click")
            }
        })

        titleRecipeEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                title = titleRecipeEditText.text.toString()

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        describRecipeEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                describ = describRecipeEditText.text.toString()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
        tagsEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                tags = tagsEditText.text.toString()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

    }

    /*<-----------------------------------тут начинаем работать с ингредиентами------------------------------->*/

    var ingredientEditText: EditText? = null //ингредиент
    var amountEditText: EditText? = null //кол-во ингредиента
    var delIngrButton: Button? = null //кнопка для удаения ингредиента
    var countIngr: Int = 1 //считаем кол-во полей для ввода ингредиентов

    val indexButt = 0
    val indexTitle = 1
    val indexAmount = 2
    val indexSpinner = 3

    private fun initiationIngredients() {
        countIngr = listIngredients.childCount
        if (countIngr == 1) {
            var view = listIngredients.children.elementAt(0) as LinearLayout
            ingredientEditText = view.children.elementAt(indexTitle) as EditText
            amountEditText = view.children.elementAt(indexAmount) as EditText
            delIngrButton = view.children.elementAt(indexButt) as Button
            delIngrButton?.setOnClickListener { deleteIngredient(delIngrButton!!) }
            setIngredientChangedLiseners()
        }
    }

    override fun saveIngredient() {
        var count = listIngredients.childCount
        var i = 0
        do {
            var linearLayout = listIngredients.children.elementAt(i) as LinearLayout
            var titleET = linearLayout.children.elementAt(indexTitle) as EditText
            var amountET = linearLayout.children.elementAt(indexAmount) as EditText
            var unitSp = linearLayout.children.elementAt(indexSpinner) as Spinner

            if (titleET.text.isNotEmpty() && amountET.text.isNotEmpty()) {

                var title = titleET.text.toString()
                var amount = amountET.text.toString().toDouble()
                var unit = unitSp.selectedItem.toString()
                Log.d("mLog", title)
                presenter?.setIngredient(title, amount, unit)
            }
            i++
        } while (i < count)
    }

    //удаляем поля для ввода ингредиента
    private fun deleteIngredient(button: Button) {
        if (countIngr > 1) {
            var parent: ViewGroup = button.parent as ViewGroup
            var grandParent = parent.parent as ViewGroup
            var ingr = parent.children.elementAt(indexTitle) as EditText
            var amount = parent.children.elementAt(indexAmount) as EditText
            if (ingr.text.isNotEmpty() || amount.text.isNotEmpty()) {
                ingredientEditText =
                    (grandParent.children.last() as LinearLayout).children.elementAt(1) as EditText
                amountEditText =
                    (grandParent.children.last() as LinearLayout).children.elementAt(2) as EditText
                grandParent.removeView(parent)
                countIngr -= 1
            }
        }
    }

    //создаем новое пое для ингредиента
    fun newIngredientView() {
        if (ingredientEditText?.text!!.isNotEmpty() && amountIngredient.text.isNotEmpty()) {
            var view = LayoutInflater.from(context).inflate(R.layout.ingredient_container, null)
            countIngr += 1
            var button = (view as LinearLayout).children.elementAt(indexButt) as Button
            ingredientEditText = (view as LinearLayout).children.elementAt(indexTitle) as EditText
            amountEditText = (view as LinearLayout).children.elementAt(indexAmount) as EditText
            setIngredientChangedLiseners()
            button.isClickable = true
            button.setOnClickListener { deleteIngredient(button) }
            listIngredients.addView(view)
        }

    }

    private fun setIngredientChangedLiseners() {
        ingredientEditText!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = newIngredientView()
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        amountEditText!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = newIngredientView()
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }


}