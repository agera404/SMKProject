package com.example.smkproject


import android.graphics.Color
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
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.example.smkproject.R.id.titleRecipeET
import com.example.smkproject.common.MainRepository
import com.example.smkproject.databinding.FragmentEditRecipeBinding
import com.example.smkproject.databinding.FragmentRecipesBinding
import com.example.smkproject.presenters.EditRecipePresenter
import com.example.smkproject.views.EditRecipeView
import kotlinx.android.synthetic.main.fragment_edit_recipe.*
import java.lang.Exception


class EditRecipeFragment : Fragment(), EditRecipeView{
    private var presenter: EditRecipePresenter? = null

    private var _binding: FragmentEditRecipeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditRecipeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter = EditRecipePresenter(this)

        binding.addRecipeButton.setOnClickListener(clickListener)
        binding.backButton.setOnClickListener(clickListener)
        binding.addIngredientsButton.setOnClickListener(clickListener)
        binding.titleRecipeET.addTextChangedListener(textWatcher)
        binding.describRecipeET.addTextChangedListener(textWatcher)
        binding.tagsET.addTextChangedListener(textWatcher)

        if(presenter!!.isRecipeNotNull()){
            binding.titleRecipeET.setText(presenter!!.recipe?.title)
            binding.describRecipeET.setText(presenter!!.recipe?.describe)
            binding.tagsET.setText(presenter!!.recipe?.tags)
            binding.ingredientsTVInEdtitRecipe.setText(presenter!!.recipe?.ingredients)
            binding.ingredientsTVInEdtitRecipe.setTextColor(Color.BLACK)
        }
    }
    val clickListener = View.OnClickListener {v ->
        when(v){
            addRecipeButton ->{
                presenter?.saveRecipe()
                MainRepository.selectedRecipe = null
                findNavController().popBackStack(R.id.recipesFragment, false)
                MainRepository.updateMenu?.invoke()
            }
            backButton ->{
                findNavController().popBackStack(R.id.recipesFragment, false)
                MainRepository.selectedRecipe = null
            }
            addIngredientsButton ->{
                MainRepository.selectedRecipe = presenter?.recipe
                findNavController().navigate(R.id.editIngredientsFragment)
            }
        }
    }
    private var textWatcher = object : TextWatcher{
        override fun afterTextChanged(s: Editable?) {
            if(titleRecipeET.getText().hashCode() == s.hashCode()){
                presenter?.recipe?.title  = titleRecipeET.text.toString()
            }
            if(describRecipeET.getText().hashCode() == s.hashCode()){

                presenter?.recipe?.describe = describRecipeET.text.toString()
            }
            if(tagsET.getText().hashCode() == s.hashCode()){
                presenter?.recipe?.tags = tagsET.text.toString()
            }

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
}