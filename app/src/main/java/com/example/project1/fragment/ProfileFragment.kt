package com.example.project1.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.project1.Activity.WelcomeScreen
import com.example.project1.R
import com.example.project1.databinding.FragmentProfileFreagmentBinding
import com.example.project1.viewmodel.UserViewModels


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileFreagmentBinding
    private val viewModel : UserViewModels by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileFreagmentBinding.inflate(layoutInflater)
        onBackButtonClicked()
        onOrderLayoutClicked()
        //addressViewLayout()
        onLogoutButtonClicked()
        return binding.root
    }

    private fun onLogoutButtonClicked() {
        binding.llLogout.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val alertDialog = builder.create()
                builder.setTitle("Logout")
                .setMessage("Do you want to logout?")
                .setPositiveButton("yes"){
                    _,_ ->
                    viewModel.logoutUser()
                    startActivity(Intent(requireContext(), WelcomeScreen::class.java))
                    requireActivity().finish()
                }
                    .setNegativeButton("No"){
                        _,_ ->
                        alertDialog.dismiss()
                    }
                    .show()
        }
    }


    private fun onOrderLayoutClicked() {
        binding.llOrders.setOnClickListener{
            findNavController().navigate(R.id.action_profileFreagment_to_orderFragment)
        }
    }

    private fun onBackButtonClicked() {
        binding.tbProfile.setNavigationOnClickListener{
            findNavController().navigate(R.id.action_profileFreagment_to_homeFragment)
        }
    }
}