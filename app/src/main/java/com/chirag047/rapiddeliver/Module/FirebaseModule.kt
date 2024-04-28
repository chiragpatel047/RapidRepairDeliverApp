package com.chirag047.rapiddeliver.Module

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object FirebaseModule {

    @Singleton
    @Provides
    fun firebaseAuthObject(): FirebaseAuth {
        return Firebase.auth
    }

    @Singleton
    @Provides
    fun firebaseFireStoreObject(): FirebaseFirestore {
        return Firebase.firestore
    }


    @Singleton
    @Provides
    fun firebaseStorageObject(): FirebaseStorage {
        return Firebase.storage
    }


}