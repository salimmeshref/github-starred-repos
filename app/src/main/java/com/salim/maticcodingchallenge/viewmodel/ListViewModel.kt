package com.salim.maticcodingchallenge.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.salim.maticcodingchallenge.di.DaggerApiComponent
import com.salim.maticcodingchallenge.model.ReposService
import com.salim.maticcodingchallenge.model.Response
import com.salim.maticcodingchallenge.util.toSimpleString
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.util.*
import javax.inject.Inject

class ListViewModel : ViewModel() {

    // define ReposService instance as dependency injection
    //lateinit var to assure that the variable will be initialized before use
    @Inject
    lateinit var reposService: ReposService
   
    private val disposable = CompositeDisposable()

    //instantiate data that affects the view
    private var page = 1
    val response = MutableLiveData<Response>()
    val reposLoadingError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    val apiLimitReached = MutableLiveData<Boolean>()


    init {
        //inject repos  Service
        DaggerApiComponent.create().inject(this)
    }


    //prepares the Http Get Query with the necessary parameters
    fun loadMore() {
        //get date condition to be used in query as follows: cretated:>date
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -30)
        val datePattern = "yyyy-MM-dd"
        val createdGreaterThan = "created:>"
        val date = createdGreaterThan + toSimpleString(calendar.time, datePattern)


        loadRepos(date, "stars", "desc", page)//pass the query params to loadRepos

        //increment the page parameter for the next hit
        page++

    }

    //calls the ReposService.getRepos to get the repos
    //populate the data containers
    //handles error
    private fun loadRepos(date: String, sort: String, order: String, page: Int) {

        // when we are at page 10 api is returning Http error code 403 forbiden with the following response
        // {
        //      "message": "API rate limit exceeded for 178.135.95.8. (But here's the good news: Authenticated requests get a higher rate limit. Check out the documentation for more details.)",
        //      "documentation_url": "https://developer.github.com/v3/#rate-limiting"
        // }
        if (page>10) {
            apiLimitReached.value = true
            return
        }
        loading.value = true
        disposable.add(reposService.getRepos(date, sort, order, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Response>() {
                    override fun onSuccess(value: Response?) {
                        response.value = value
                        reposLoadingError.value = false
                        loading.value = false
                    }

                    override fun onError(e: Throwable?) {

                        loading.value = false

                        if (e is HttpException) {
                            Log.d("debug ", "page=$page")
                            Log.d("error", "error code = ${e.code()} error response = ${e.response()}")
                            if(e.code()==403){
                                reposLoadingError.value = false
                                apiLimitReached.value= true
                                return
                            }

                            reposLoadingError.value = true
                        }
                    }
                }))
    }

    //This method will be called when this ViewModel is no longer used and will be destroyed.
    //clears disposable
    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}