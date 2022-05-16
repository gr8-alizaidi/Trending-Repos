package com.alizaidi.trendingrepos.ui.repos

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.SearchAutoComplete
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alizaidi.trendingrepos.R
import com.alizaidi.trendingrepos.data.model.Query
import com.alizaidi.trendingrepos.databinding.FragmentReposBinding
import com.alizaidi.trendingrepos.ui.MainActivity
import com.alizaidi.trendingrepos.ui.repos.adapter.ReposAdapter
import com.alizaidi.trendingrepos.ui.repos.adapter.ReposLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class ReposFragment : Fragment(R.layout.fragment_repos) {

    private val viewModel by viewModels<ReposViewModel>()

    private var _binding: FragmentReposBinding? = null
    private val binding get() = _binding!!
    private var searchedquery="github"
    private var flag=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        _binding = FragmentReposBinding.bind(view)
         val adapter = ReposAdapter()

        val swipeRefreshLayout = (activity as MainActivity).findViewById<SwipeRefreshLayout>(R.id.refreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            Log.e("referesh","force ${isOnline(requireContext())}")
            if(isOnline(requireContext())) {
                if(!flag) {
                    Toast.makeText(context, "Showing remote repo", Toast.LENGTH_SHORT)
                        .show()
                    flag=true
                }
                viewModel.searchRepos(searchedquery)
                viewModel.repos.observe(viewLifecycleOwner) {
                    adapter.submitData(viewLifecycleOwner.lifecycle, it)
                }
            }
            else
            {
                if(flag) {
                    Toast.makeText(context, "No Internet! Showing saved repo", Toast.LENGTH_SHORT)
                        .show()
                    flag = false
                }
//                adapter.submitData(viewLifecycleOwner.lifecycle, PagingData.empty())
                viewModel.dataDb()
                viewModel.dbResult.observe(viewLifecycleOwner)
                {
                    adapter.submitData(viewLifecycleOwner.lifecycle, it)
                }
            }
            swipeRefreshLayout.isRefreshing=false
        }
        binding.apply {

            recycler.apply {
                setHasFixedSize(true)
                itemAnimator = null
                this.adapter = adapter.withLoadStateHeaderAndFooter(
                    header = ReposLoadStateAdapter { adapter.retry() },
                    footer = ReposLoadStateAdapter { adapter.retry() }
                )
                postponeEnterTransition()
                viewTreeObserver.addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
            }

            btnRetry.setOnClickListener {
                adapter.retry()
            }
        }

        if(isOnline(requireContext())) {
            if(!flag) {
                Toast.makeText(context, "Showing remote repo", Toast.LENGTH_SHORT)
                    .show()
                flag=true
            }
            viewModel.repos.observe(viewLifecycleOwner) {
                adapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
        }
        else {
            if(flag) {
                Toast.makeText(context, "No Internet! Showing saved repo", Toast.LENGTH_SHORT)
                    .show()
                flag=false
            }
            viewModel.dataDb()
            viewModel.dbResult.observe(viewLifecycleOwner)
            {
                adapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
        }
        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progress.isVisible = loadState.source.refresh is LoadState.Loading
                recycler.isVisible = loadState.source.refresh is LoadState.NotLoading
                btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                error.isVisible = loadState.source.refresh is LoadState.Error

                // no results found
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    recycler.isVisible = false
                    emptyTv.isVisible = true
                } else {
                    emptyTv.isVisible = false
                }
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_repos, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val deleteItem=menu.findItem(R.id.action_delete)
        val adapter= ReposAdapter()
        if(!isOnline(requireContext()))
        {
            searchItem.setVisible(false)
            deleteItem.setVisible(true)
            deleteItem.setOnMenuItemClickListener {
                viewModel.deleteDb()
                Toast.makeText(context,"All entries deleted",Toast.LENGTH_SHORT).show()
                requireFragmentManager().beginTransaction().detach(this).attach(this).commit();
                true
            }

        }else {
            searchItem.setVisible(true)
            deleteItem.setVisible(false)
            val searchView = searchItem.actionView as SearchView
            val searchAutoComplete: SearchAutoComplete =
                searchView.findViewById(androidx.appcompat.R.id.search_src_text)

            // Get SearchView autocomplete object
            searchAutoComplete.setTextColor(Color.WHITE)
            searchAutoComplete.setDropDownBackgroundResource(R.color.colorPrimary)

            val newsAdapter: ArrayAdapter<String> = ArrayAdapter(
                this.requireContext(),
                R.layout.dropdown_item,
                Query.data
            )
            searchAutoComplete.setAdapter(newsAdapter)

            // Listen to search view item on click event
            searchAutoComplete.onItemClickListener =
                OnItemClickListener { adapterView, _, itemIndex, _ ->
                    val queryString = adapterView.getItemAtPosition(itemIndex) as String
                    searchAutoComplete.setText(
                        String.format(
                            getString(R.string.search_query),
                            queryString
                        )
                    )
                    binding.recycler.scrollToPosition(0)
//                val languageQuery = String.format(getString(R.string.query), queryString)
                    viewModel.searchRepos(queryString)
                    searchView.clearFocus()
                    searchedquery = queryString
                    (activity as AppCompatActivity).supportActionBar?.title =
                        "Trending Repositories"
                }

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.searchRepos(query!!)
                    searchView.clearFocus()
                    searchedquery = query
                    (activity as AppCompatActivity).supportActionBar?.title =
                        "Trending Repositories"
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
}