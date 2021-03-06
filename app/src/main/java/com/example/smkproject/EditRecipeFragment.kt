package com.example.smkproject


import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.MultiAutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smkproject.common.MainRepository
import com.example.smkproject.databinding.FragmentEditRecipeBinding
import com.example.smkproject.presenters.EditRecipePresenter
import com.example.smkproject.presenters.ErrorSaveCode
import com.example.smkproject.views.EditRecipeView
import kotlinx.android.synthetic.main.fragment_edit_recipe.*


class EditRecipeFragment : Fragment(), EditRecipeView{
    private var presenter: EditRecipePresenter? = null

    private var _binding: FragmentEditRecipeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter = EditRecipePresenter(this)


        binding.addIngredientsButton.setOnClickListener{view -> onButtonClick(view)}
        binding.addRecipeButton.setOnClickListener{view -> onButtonClick(view)}
        binding.backButton.setOnClickListener{view -> onButtonClick(view)}
        binding.titleRecipeET.addTextChangedListener(textWatcher)
        binding.describRecipeET.addTextChangedListener(textWatcher)
        binding.editTags.addTextChangedListener(textWatcher)

        var adapter: ArrayAdapter<String>? = context?.let { ArrayAdapter<String>(it, android.R.layout.simple_spinner_item, presenter!!.getTags()) }
        binding.editTags.setAdapter(adapter)
        binding.editTags.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())

        if(presenter!!.isRecipeNotNull()){
            binding.titleRecipeET.setText(presenter!!.recipe?.title)
            binding.describRecipeET.setText(presenter!!.recipe?.describe)
            binding.editTags.setText(presenter!!.recipe?.tags)
            var list = presenter!!.recipe?.convertIngredients()
            var ingredients = String()
            if (list != null) {
                for (i in list){
                    ingredients+="${list.indexOf(i)+1}. ${i.title}: ${i.amount} ${i.unit} \n"
                }
            }
            ingredients = ingredients.dropLast(1)
            binding.ingredientsTVInEdtitRecipe.setText(ingredients)
            binding.ingredientsTVInEdtitRecipe.setTextColor(Color.BLACK)
        }
    }


    fun setFocus(et: EditText){
        et.requestFocus();
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
    fun onButtonClick(v: View?) {
        when(v){
            binding.addRecipeButton ->{
                val errorCode = presenter?.saveRecipe()
                if ( errorCode == null){
                    MainRepository.selectedRecipe = null
                    findNavController().popBackStack(R.id.recipesFragment, false)
                    MainRepository.updateMenu?.invoke()
                }else{
                    var text: String = ""
                    when(errorCode){
                        ErrorSaveCode.ERROR_TITLE ->{
                            setFocus(binding.titleRecipeET)
                            text = "Set title"
                        }
                        ErrorSaveCode.ERROR_DESCRIBE ->{
                            setFocus(binding.describRecipeET)
                            text = "Set describe"
                        }
                        ErrorSaveCode.ERROR_INGR ->{
                            binding.addIngredientsButton.callOnClick()
                            text = "Set ingr"
                        }
                        ErrorSaveCode.ERROR_TAGS ->{
                            setFocus(binding.editTags)
                            text = "Set tags"
                        }
                    }
                    Toast.makeText(context,"ERROR: $text",Toast.LENGTH_LONG).show()
                }
            }
            binding.backButton ->{
                findNavController().popBackStack(R.id.recipesFragment, false)
                MainRepository.selectedRecipe = null
            }
            binding.addIngredientsButton ->{
                MainRepository.selectedRecipe = presenter?.recipe
                findNavController().navigate(R.id.editIngredientsFragment)
            }
            binding.editTags ->{
                binding.editTags.showDropDown()
            }
        }
    }
    private var textWatcher = object : TextWatcher{
        override fun afterTextChanged(s: Editable?) {
            if(binding.titleRecipeET.getText().hashCode() == s.hashCode()){
                presenter?.recipe?.title  = binding.titleRecipeET.text.toString()
            }
            if(binding.describRecipeET.getText().hashCode() == s.hashCode()){
                presenter?.recipe?.describe = describRecipeET.text.toString()
            }
            if(binding.editTags.getText().hashCode() == s.hashCode()){
                presenter?.recipe?.tags = binding.editTags.text.toString()
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
}