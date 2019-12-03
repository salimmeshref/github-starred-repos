package com.salim.maticcodingchallenge

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.salim.maticcodingchallenge.model.Owner
import com.salim.maticcodingchallenge.model.Repo
import com.salim.maticcodingchallenge.model.ReposService
import com.salim.maticcodingchallenge.model.Response
import com.salim.maticcodingchallenge.viewmodel.ListViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class ViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var reposService: ReposService

    @InjectMocks
    private var listViewModel = ListViewModel()

    private var testSingle: Single<Response>?=null

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getReposSuccess(){
        val owner = Owner(username = "salim",avatarUrl = "url")
        val repo = Repo(owner= owner , name = "repo", description = "desc",starsCount = 500)

        val items = arrayListOf(repo)

        val response = Response(repos = items)

        testSingle = Single.just(response)

        `when`(reposService.getRepos(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())).thenReturn(testSingle)

        listViewModel.loadMore()

        Assert.assertEquals(1,listViewModel.response?.value?.repos?.size)
        Assert.assertEquals(false,listViewModel.reposLoadingError.value)
        Assert.assertEquals(false, listViewModel.loading.value)
    }

    @Test
    fun getReposFailure(){
        testSingle = Single.error(Throwable())

        `when`(reposService.getRepos(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())).thenReturn(testSingle)

        listViewModel.loadMore()

        Assert.assertEquals(true,listViewModel.reposLoadingError.value)
        Assert.assertEquals(false, listViewModel.loading.value)
    }

    @Before
    fun setUpRxSchedulers(){
        val immediate = object : Scheduler(){
            override fun scheduleDirect(run: Runnable?, delay: Long, unit: TimeUnit?): Disposable {
                return super.scheduleDirect(run, 0, unit)
            }
            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
            }
        }

        RxJavaPlugins.setInitIoSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { scheduler -> immediate}
        RxAndroidPlugins.setInitMainThreadSchedulerHandler{ scheduler -> immediate}
    }

}