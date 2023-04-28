package com.example.wassalniDR.di

import com.example.wassalniDR.database.TripsRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object TripModule {

    @Provides
    @ViewModelScoped
    fun getTripRetrofit(): TripsRetrofit {
        val retrofit = Retrofit.Builder().baseUrl("").build()
        return retrofit.create(TripsRetrofit::class.java)
    }

}