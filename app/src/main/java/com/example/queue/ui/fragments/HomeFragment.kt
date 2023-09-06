package com.example.queue.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.queue.R
import com.example.queue.adapters.QueueAdapter
import com.example.queue.databinding.FragmentHomeBinding
import com.example.queue.ui.MainActivity
import com.example.queue.ui.MainViewModel
import com.example.queue.util.Resource

class HomeFragment : Fragment() {

    lateinit var viewModel: MainViewModel
    private var _binding: FragmentHomeBinding? = null
    lateinit var queueAdapter: QueueAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = (activity as MainActivity).viewModel

        setUpRecyclerView()

        queueAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("queue", it)
            }
            findNavController().navigate(
                R.id.action_nav_home_to_nav_queue,
                bundle
            )
        }

        observeQueues()
        viewModel.getQueues()
        return binding.root
    }

    private fun observeQueues() {
        viewModel.queues.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        queueAdapter.differ.submitList(it)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()

                    response.message?.let {
                        Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpRecyclerView() {
        queueAdapter = QueueAdapter()
        binding.rvQueues.apply {
            adapter = queueAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}