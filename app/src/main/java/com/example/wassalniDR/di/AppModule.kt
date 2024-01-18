package com.example.wassalniDR.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.wassalniDR.data.LoggedInDriver
import com.example.wassalniDR.database.DriversRetrofit
import com.example.wassalniDR.util.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    @Singleton
    fun providesLoggedInDriver(sharedPreferences: SharedPreferences): LoggedInDriver {
        val email = sharedPreferences.getString("email", "")
        val token = sharedPreferences.getString("token", "")
        return LoggedInDriver(email!!, token!!)
    }

    @Provides
    @Singleton
    fun providesDriverRetrofit(okHttpClient: OkHttpClient): DriversRetrofit {

        val retrofit = Retrofit.Builder().baseUrl(Constant.BASEURL)
            .addConverterFactory(
                MoshiConverterFactory.create()
            )
            .client(okHttpClient)
            .build()

        return retrofit.create(DriversRetrofit::class.java)
    }

    @Provides
    @Singleton
    fun providesHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .readTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(2, TimeUnit.MINUTES)
            .addInterceptor(loggingInterceptor)
            .build()
    }

}