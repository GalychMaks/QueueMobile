package com.example.queue.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.queue.R
import com.example.queue.databinding.FragmentFindQueueBinding
import com.example.queue.ui.MainActivity
import com.example.queue.ui.MainViewModel
import com.example.queue.util.Resource

class FindQueueFragment : Fragment() {

    lateinit var viewModel: MainViewModel
    private var _binding: FragmentFindQueueBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFindQueueBinding.inflate(inflater, container, false)

        viewModel = (activity as MainActivity).viewModel

        binding.btnSubmit.setOnClickListener {
            viewModel.getQueue(binding.etQueueCode.text.toString()).observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Success -> {
                        hideProgressBar()
                        val bundle = Bundle().apply {
                            putSerializable("queue", it.data)
                        }
                        findNavController().navigate(
                            R.id.nav_queue,
                            bundle
                        )
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