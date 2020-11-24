package com.example.smkproject

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.smkproject.common.MainRepository
import com.example.smkproject.models.Ingredient
import com.example.smkproject.presenters.EditIngredientsPresenter
import com.example.smkproject.views.EditIngredientsView
import kotlinx.android.synthetic.main.fragment_edit_ingredients.*


class EditIngredientsFragment : Fragment(), EditIngredientsView {

    var presenter: EditIngredientsPresenter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_ingredients, container, false)
    }


    var ingredientET: EditText? = null //ингредиент
    var amountET: EditText? = null //кол-во ингредиента
    var unitSpinner: Spinner? = null
    var delIngrButton: Button? = null //кнопка для удаения ингредиента
    var countIngr: Int = 1 //считаем кол-во полей для ввода ингредиентов

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter = EditIngredientsPresenter(this)

        ingredientET = (listIngredients.children.elementAt(0) as LinearLayout).children.elementAt(indexTitle) as EditText
        ingredientET?.addTextChangedListener(textWatcher)

        amountET = (listIngredients.children.elementAt(0) as LinearLayout).children.elementAt(indexAmount) as EditText
        amountET?.addTextChangedListener(textWatcher)

        unitSpinner = (listIngredients.children.elementAt(0) as LinearLayout).children.elementAt(indexSpinner) as Spinner

        delIngrButton = (listIngredients.children.elementAt(0) as LinearLayout).children.elementAt(indexButt) as Button
        delIngrButton?.setOnClickListener(this.clickListener)

        addNewIngredientButton.setOnClickListener(clickListener)
        saveIngredientsButton.setOnClickListener(clickListener)

        if (presenter!!.ingredients.count()>0){
            presenter!!.setIngredients()
        }
    }

    var clickListener = View.OnClickListener { v ->
        when(v){
            delIngrButton->{

            }
            addNewIngredientButton->{
                newIngredientView()
            }
            saveIngredientsButton->{
                for (element in listIngredients.children){
                    saveIngredient(element)
                }
                MainRepository.selectedRecipe!!.ingredients = presenter!!.ingredients
                MainRepository.onEditRecipe?.invoke()
            }
        }
    }
    private var textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if(ingredientET?.getText().hashCode() == s.hashCode()){

            }
            if(amountET?.getText().hashCode() == s.hashCode()){

            }
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }



    //индексы элементов в layout/ingredient_container.xml
    val indexButt = 0
    val indexTitle = 1
    val indexAmount = 2
    val indexSpinner = 3



    //создаем новое пое для ингредиента
    fun newIngredientView() {
        var view = LayoutInflater.from(context).inflate(R.layout.ingredient_container, null)
        ingredientET = (view as LinearLayout).children.elementAt(indexTitle) as EditText
        ingredientET?.addTextChangedListener(textWatcher)

        amountET = (view as LinearLayout).children.elementAt(indexAmount) as EditText
        amountET?.addTextChangedListener(textWatcher)

        delIngrButton = (view as LinearLayout).children.elementAt(indexButt) as Button
        delIngrButton?.setOnClickListener(clickListener)

        unitSpinner = (view as LinearLayout).children.elementAt(indexSpinner) as Spinner
        countIngr += 1
        listIngredients.addView(view)
    }

    fun saveIngredient(_view: View){
        var view = _view as LinearLayout
        var title = (view.children.elementAt(indexTitle) as EditText).text.toString()
        var amount = (view.children.elementAt(indexAmount) as EditText).text.toString().toDouble()
        var unit = (view.children.elementAt(indexSpinner) as Spinner).selectedItem.toString()

        if (title.isEmpty() || amount<0 || amount.isNaN() || unit.isEmpty()) {
            Toast.makeText(context, R.string.error_fild_is_empty, Toast.LENGTH_SHORT).show()
        }else{
            MainRepository.currentIngredient = Ingredient(title,amount,unit)
        }
    }
    override fun loadIngredient(title: String, amount: Double, unit: String){
        ingredientET?.setText(title)
        amountET?.setText(amount.toString())
        unitSpinner?.setSelection((unitSpinner!!.getAdapter() as ArrayAdapter<String>).getPosition(unit))
        newIngredientView()
    }
}