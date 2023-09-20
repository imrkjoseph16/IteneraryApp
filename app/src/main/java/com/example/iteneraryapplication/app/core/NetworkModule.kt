package com.example.iteneraryapplication.app.core

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth()  = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseUser(auth: FirebaseAuth) = auth.currentUser
}