package com.example.smkproject

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smkproject.common.MainRepository
import com.example.smkproject.databinding.FragmentEditIngredientsBinding
import com.example.smkproject.databinding.FragmentEditRecipeBinding
import com.example.smkproject.models.Ingredient
import com.example.smkproject.presenters.EditIngredientsPresenter
import com.example.smkproject.views.EditIngredientsView
import kotlinx.android.synthetic.main.fragment_edit_ingredients.*


class EditIngredientsFragment : Fragment(), EditIngredientsView {

    var presenter: EditIngredientsPresenter? = null

    private var _binding: FragmentEditIngredientsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditIngredientsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    var ingredientET: EditText? = null //ингредиент
    var amountET: EditText? = null //кол-во ингредиента
    var unitSpinner: Spinner? = null
    var delIngrButton: Button? = null //кнопка для удаения ингредиента
    var countIngr: Int = 1 //считаем кол-во полей для ввода ингредиентов

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter = EditIngredientsPresenter(this)



        ingredientET = (binding.listIngredients.children.elementAt(0) as LinearLayout).children.elementAt(indexTitle) as EditText
        ingredientET?.addTextChangedListener(textWatcher)

        amountET = (binding.listIngredients.children.elementAt(0) as LinearLayout).children.elementAt(indexAmount) as EditText
        amountET?.addTextChangedListener(textWatcher)

        unitSpinner = (binding.listIngredients.children.elementAt(0) as LinearLayout).children.elementAt(indexSpinner) as Spinner

        delIngrButton = (binding.listIngredients.children.elementAt(0) as LinearLayout).children.elementAt(indexButt) as Button
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
                (delIngrButton?.parent as ViewGroup).removeView(delIngrButton?.parent as ViewGroup)
            }
            binding.addNewIngredientButton->{
                newIngredientView()
            }
            binding.saveIngredientsButton->{
                presenter?.ingredients = arrayListOf()
                for (element in listIngredients.children){
                    saveIngredient(element)
                }
                presenter?.onDestroy()
                findNavController().popBackStack(R.id.editRecipeFragment, false)
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

        amountET = view.children.elementAt(indexAmount) as EditText
        amountET?.addTextChangedListener(textWatcher)

        delIngrButton = view.children.elementAt(indexButt) as Button
        delIngrButton?.setOnClickListener(clickListener)

        unitSpinner = view.children.elementAt(indexSpinner) as Spinner
        countIngr += 1
        listIngredients.addView(view)
    }

    fun saveIngredient(_view: View){
        var view = _view as LinearLayout
        var title = (view.children.elementAt(indexTitle) as EditText).text.toString()
        Log.d("mLog", "${(view.children.elementAt(indexAmount) as EditText).text.toString()}")
        var amount = (view.children.elementAt(indexAmount) as EditText).text.toString().toDouble()
        var unit = (view.children.elementAt(indexSpinner) as Spinner).selectedItem.toString()


        presenter?.ingredients?.add(Ingredient(null,title,amount,unit))
    }
    override fun loadIngredient(title: String, amount: Double, unit: String){
        ingredientET?.setText(title)
        amountET?.setText(amount.toString())
        unitSpinner?.setSelection((unitSpinner!!.getAdapter() as ArrayAdapter<String>).getPosition(unit))
        newIngredientView()
    }
}