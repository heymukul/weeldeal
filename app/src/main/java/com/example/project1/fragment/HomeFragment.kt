package com.example.project1.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.project1.Constants
import com.example.project1.Models.Category
import com.example.project1.R
import com.example.project1.adapter.AdapterCategory
import com.example.project1.databinding.FragmentHomeBinding
import com.example.project1.viewmodel.UserViewModels


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    val viewModel : UserViewModels by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        setAllCategory()
        navigatingToSearchFragment()
        onProfileClicked()
        return binding.root
    }

    private fun onProfileClicked() {
        binding.ivProfile.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_profileFreagment)
        }
    }


    private fun navigatingToSearchFragment() {
        binding.searchCv.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun setAllCategory() {
        val categoryList = ArrayList<Category>()
        for ( i in 0 until Constants.allProductCategory.size){
            categoryList.add(Category(Constants.allProductCategory[i],Constants.allProductIcon[i]))
        }
        binding.rvCategory.adapter = AdapterCategory(categoryList, ::onCategoryIconClicked)
    }
  fun onCategoryIconClicked(category: Category){
      val bundle = Bundle()
      bundle.putString("category" , category.title)
      findNavController().navigate(R.id.action_homeFragment_to_categoryFragment ,bundle)
  }

}