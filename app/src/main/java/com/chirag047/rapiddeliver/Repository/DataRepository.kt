package com.chirag047.rapiddeliver.Repository

import com.chirag047.rapiddeliver.Common.ResponseType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class DataRepository @Inject constructor(val auth: FirebaseAuth, val firestore: FirebaseFirestore) {

    suspend fun updateUserCity(city: String, mechanicId: String): Flow<ResponseType<String>> =
        callbackFlow {
            trySend(ResponseType.Loading())

            firestore.collection("mechanicUsers")
                .document(auth.currentUser!!.uid)
                .update("city", city).addOnCompleteListener {
                    if (it.isSuccessful) {
                        trySend(ResponseType.Success("Added"))
                    }
                }
            awaitClose {
                close()
            }
        }

}