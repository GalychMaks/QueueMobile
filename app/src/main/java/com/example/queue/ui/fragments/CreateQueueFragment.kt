package com.example.queue.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.queue.R
import com.example.queue.databinding.FragmentCreateQueueBinding
import com.example.queue.models.CreateQueueRequestModel
import com.example.queue.ui.MainActivity
import com.example.queue.ui.MainViewModel
import com.example.queue.util.Resource

class CreateQueueFragment : Fragment() {

    lateinit var viewModel: MainViewModel
    private var _binding: FragmentCreateQueueBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateQueueBinding.inflate(inflater, container, false)

        viewModel = (activity as MainActivity).viewModel

        binding.btnSubmit.setOnClickListener {
            viewModel.createQueue(
                CreateQueueRequestModel(
                    binding.etQueueName.text.toString(),
                    binding.etDescription.text.toString()
                )
            ).observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Success -> {
                        hideProgressBar()
                        Toast.makeText(context, "Queue created", Toast.LENGTH_LONG).show()
                        val bundle = Bundle().apply {
                            putSerializable("queue", it.data)
                        }
                        findNavController().navigate(
                            R.id.action_nav_create_queue_to_queueFragment,
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