package com.salim.maticcodingchallenge.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.salim.maticcodingchallenge.R
import com.salim.maticcodingchallenge.model.Repo
import com.salim.maticcodingchallenge.util.getProgressDrawable
import com.salim.maticcodingchallenge.util.loadImage
import kotlinx.android.synthetic.main.item_repo.view.*

class ReposListAdapter(var repos :ArrayList<Repo>):RecyclerView.Adapter<ReposListAdapter.RepoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int)= RepoViewHolder (
            LayoutInflater.from(parent.context).inflate(R.layout.item_repo,parent,false)
    )

    // add new repository to the list
    //notify the adapter that the data has changed
    fun updateRepos(newRepos:List<Repo>){
        repos.addAll(newRepos)
        notifyDataSetChanged()
    }

    //returns
    override fun getItemCount()= repos.size


    //populates recycler view items row by row
    override fun onBindViewHolder(holder: RepoViewHolder, postion: Int) {
        holder.bind(repos[postion])

    }

    //defines how each row must be populated
    class RepoViewHolder (view: View):RecyclerView.ViewHolder(view){
        //views belonging to each row
        private val name = view.name
        private val description = view.description
        private val stars = view.stars
        private val owner = view.owner
        private val avatar = view.avatar

        private val progressDrawable = getProgressDrawable(view.context)
        //fills each view with correspondent data
        fun bind(repo : Repo){
            name.text= repo.name
            description.text = repo.description
            stars.text = repo.starsCount.toString()
            owner.text = repo.owner?.username
            avatar.loadImage(repo.owner?.avatarUrl,progressDrawable)

        }
    }
}