package com.example.queue.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.queue.R
import com.example.queue.databinding.FragmentLoginBinding
import com.example.queue.models.Key
import com.example.queue.models.LoginRequest
import com.example.queue.ui.MainActivity
import com.example.queue.ui.MainViewModel
import com.example.queue.util.Resource
import com.google.android.material.navigation.NavigationView
import retrofit2.Response

class SignInFragment : Fragment() {

    lateinit var viewModel: MainViewModel
    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        viewModel = (activity as MainActivity).viewModel

        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_nav_login_to_signUpFragment)
        }

        binding.btnSignIn.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            viewModel.login(
                LoginRequest(
                    binding.etUserName.text.toString(),
                    binding.etPassword.text.toString()
                )
            ).observe(viewLifecycleOwner, Observer {
                when(it) {
                    is Resource.Success -> {
                        hideProgressBar()
                        findNavController().navigateUp()
                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            })
        }
        return binding.root
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}