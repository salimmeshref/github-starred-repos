package com.salim.maticcodingchallenge.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.salim.maticcodingchallenge.R
import com.salim.maticcodingchallenge.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: ListViewModel
    private val repoListAdapter = ReposListAdapter(arrayListOf())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //instantiate the ViewModel
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)

        //get repositories
        viewModel.loadMore()


        //initialize recycler View
        repos_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = repoListAdapter
        }

        //add scroll listener in order to automate api hit
        // to load more repositories when the end of the list is reached
        repos_list.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)){// end of list reached
                    val oldCount = repoListAdapter.itemCount
                    // Only the first 1000 search results are available
                    if (repoListAdapter.itemCount<1000 && viewModel.reposLoadingError.value==false) {
                        viewModel.loadMore()
                    }
                }
            }
        })

        observeViewModel()

    }


    //observe changes in the ViewModel's data variables
    //in order to update the view
    private fun observeViewModel(){
        viewModel.response.observe(this, Observer { repos->repos?.let {
            repos_list.visibility= View.VISIBLE
            repoListAdapter.updateRepos(it.repos)
            }
        })

        viewModel.reposLoadingError.observe(this,Observer{
            isError -> isError?.let{
                list_error.visibility= if(it) View.VISIBLE else View.GONE
            }
        })

        viewModel.loading.observe(this, Observer { isLoading->
            isLoading?.let{
                loading_view.visibility = if(it)View.VISIBLE else View.GONE
                if(it){
                    list_error.visibility= View.GONE
                }
            }
        })

    }
}
