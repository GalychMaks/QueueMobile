package com.example.queue.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.queue.R
import com.example.queue.databinding.FragmentSignUpBinding
import com.example.queue.models.RegistrationRequest
import com.example.queue.ui.MainActivity
import com.example.queue.ui.MainViewModel
import com.example.queue.util.Resource
import retrofit2.Response

class SignUpFragment : Fragment() {

    lateinit var viewModel: MainViewModel
    private var _binding: FragmentSignUpBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        viewModel = (activity as MainActivity).viewModel

        binding.tvSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_nav_login)
        }

        binding.btnSignUp.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            viewModel.registration(
                RegistrationRequest(
                    binding.etUserName.text.toString(),
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString(),
                    binding.etPasswordConfirm.text.toString()
                )
            ).observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Success -> {
                        hideProgressBar()
                        Toast.makeText(context, "now you can login", Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_signUpFragment_to_nav_login)
                    }

                    is Resource.Error -> {
                        hideProgressBar()
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }

                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            }
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