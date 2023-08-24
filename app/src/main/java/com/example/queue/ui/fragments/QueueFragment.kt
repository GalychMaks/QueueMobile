package com.example.queue.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.queue.adapters.MemberAdapter
import com.example.queue.databinding.FragmentQueueBinding
import com.example.queue.ui.MainActivity
import com.example.queue.ui.MainViewModel
import com.example.queue.util.Resource

class QueueFragment : Fragment() {

    lateinit var viewModel: MainViewModel
    private var _binding: FragmentQueueBinding? = null
    private val args: QueueFragmentArgs by navArgs()
    lateinit var memberAdapter: MemberAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQueueBinding.inflate(inflater, container, false)

        viewModel = (activity as MainActivity).viewModel
        setUpRecyclerView()

        viewModel.members.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        memberAdapter.differ.submitList(it)
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

        viewModel.getMembers(args.queue.id)

        return binding.root
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
        memberAdapter = MemberAdapter()
        binding.rvMembers.apply {
            adapter = memberAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}
