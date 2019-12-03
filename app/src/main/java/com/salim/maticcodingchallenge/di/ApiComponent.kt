package com.salim.maticcodingchallenge.di

import com.salim.maticcodingchallenge.model.ReposService
import com.salim.maticcodingchallenge.viewmodel.ListViewModel
import dagger.Component
//Component is used to inject the dependencies in their dependent classes
@Component(modules = [ApiModule::class])
interface ApiComponent {
    fun inject(service: ReposService)

    fun inject(viewModel:ListViewModel)
}