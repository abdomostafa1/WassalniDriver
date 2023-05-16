package com.example.wassalniDR.di

import android.content.Context
import com.example.wassalniDR.util.Permission
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @Provides
    @ActivityScoped
    fun providesPermission(@ActivityContext context:Context):Permission{
        return Permission(context)
    }
}