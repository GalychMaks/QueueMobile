package com.example.queue.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.queue.R
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
        setupMenu()
        setUpRecyclerView()
        setTitle(args.queue.name)

        viewModel = (activity as MainActivity).viewModel

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

    private fun setTitle(title: String) {
        requireActivity().findViewById<Toolbar>(R.id.toolbar)?.title = title
        requireActivity().invalidateOptionsMenu()
    }

    private fun deleteQueue() {
        viewModel.deleteQueue(args.queue.id).observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    hideProgressBar()
                    Toast.makeText(context, getString(R.string.queue_deleted), Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigateUp()
                }

                is Resource.Error -> {
                    hideProgressBar()
                    Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
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

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // TODO: ("menu.findItem(R.id.menu_delete_queue).isVisible =
                //  loggedInUser.id == creator.id")
                super.onPrepareMenu(menu)
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.queue_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_delete_queue -> {
                        deleteQueue()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setUpRecyclerView() {
        memberAdapter = MemberAdapter()
        binding.rvMembers.apply {
            adapter = memberAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
