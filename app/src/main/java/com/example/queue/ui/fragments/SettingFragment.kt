package com.example.queue.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.queue.R
import com.example.queue.databinding.FragmentSettingBinding
import com.example.queue.ui.MainActivity
import com.example.queue.ui.MainViewModel

class SettingFragment : Fragment() {

    lateinit var viewModel: MainViewModel
    private var _binding: FragmentSettingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        viewModel = (activity as MainActivity).viewModel

        setButtonsVisibility(viewModel.authorizationToken.value != null) // TODO:

        binding.btnSignIn.setOnClickListener {
            findNavController().navigate(R.id.nav_sign_in)
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            setButtonsVisibility(isUserLoggedIn = false)
        }

        return binding.root
    }

    private fun setButtonsVisibility(isUserLoggedIn: Boolean) {
        binding.btnSignIn.isVisible = !isUserLoggedIn
        binding.btnLogout.isVisible = isUserLoggedIn
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}