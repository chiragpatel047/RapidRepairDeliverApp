package com.chirag047.rapiddeliver.Repository

import com.chirag047.rapiddeliver.Common.ResponseType
import com.chirag047.rapiddeliver.Model.Coordinates
import com.chirag047.rapiddeliver.Model.OrderModel
import com.chirag047.rapiddeliver.Model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
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

    suspend fun getUserDetail(): Flow<ResponseType<UserModel?>> =
        callbackFlow {
            trySend(ResponseType.Loading())

            firestore.collection("mechanicUsers")
                .document(auth.currentUser!!.uid)
                .addSnapshotListener { value, error ->
                    trySend(ResponseType.Success(value!!.toObject(UserModel::class.java)))
                }

            awaitClose {
                close()
            }
        }


    suspend fun getPendingRequest(mechanicId: String): Flow<ResponseType<List<OrderModel>>> =
        callbackFlow {
            trySend(ResponseType.Loading())

            firestore.collection("orders")
                .whereEqualTo("mechanicId", mechanicId)
                .whereEqualTo("orderStatus", "Mechanic Pending")
                .addSnapshotListener { value, error ->
                    trySend(ResponseType.Success(value!!.toObjects(OrderModel::class.java)))
                }

            awaitClose {
                close()
            }
        }


    suspend fun getLiveRequest(mechanicId: String): Flow<ResponseType<List<OrderModel>>> =
        callbackFlow {

            trySend(ResponseType.Loading())

            firestore.collection("orders")
                .whereEqualTo("mechanicId", mechanicId)
                .whereEqualTo("orderStatus", "Live")
                .addSnapshotListener { value, error ->
                    trySend(ResponseType.Success(value!!.toObjects(OrderModel::class.java)))
                }

            awaitClose {
                close()
            }
        }

    suspend fun startMechanicService(orderId: String): Flow<ResponseType<String>> =
        callbackFlow {

            trySend(ResponseType.Loading())

            firestore.collection("orders")
                .document(orderId)
                .update("orderStatus", "Live")
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        firestore.collection("mechanicUsers")
                            .document(auth.currentUser!!.uid)
                            .update("mechanicStatus", "On service")
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    trySend(ResponseType.Success("Started now"))
                                }
                            }
                    } else {
                        trySend(ResponseType.Error("Something went wrong"))
                    }
                }

            awaitClose {
                close()
            }
        }

    suspend fun getMyOrdersRequest(mechanicId: String): Flow<ResponseType<List<OrderModel>>> =
        callbackFlow {

            trySend(ResponseType.Loading())

            firestore.collection("orders")
                .whereEqualTo("mechanicId", mechanicId)
                .whereEqualTo("orderStatus", "Done")
                .addSnapshotListener { value, error ->
                    trySend(ResponseType.Success(value!!.toObjects(OrderModel::class.java)))
                }

            awaitClose {
                close()
            }
        }


    suspend fun trackLiveLocation(orderId: String): Flow<ResponseType<Coordinates?>> =
        callbackFlow {

            trySend(ResponseType.Loading())

            firestore.collection("liveTrack")
                .document(orderId)
                .addSnapshotListener { value, error ->
                    trySend(ResponseType.Success(value!!.toObject(Coordinates::class.java))!!)
                }

            awaitClose {
                close()
            }
        }


    suspend fun doneMechanicService(orderId: String): Flow<ResponseType<String>> =
        callbackFlow {
            trySend(ResponseType.Loading())

            firestore.collection("orders")
                .document(orderId)
                .update("orderStatus", "Done")
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        firestore.collection("mechanicUsers")
                            .document(auth.currentUser!!.uid)
                            .update("mechanicStatus", "Available")
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    trySend(ResponseType.Success("Done"))
                                }
                            }
                    } else {
                        trySend(ResponseType.Error("Something went wrong"))
                    }
                }

            awaitClose {
                close()
            }
        }


    suspend fun updateMechanicStatus(status: String): Flow<ResponseType<String>> =
        callbackFlow {
            trySend(ResponseType.Loading())

            firestore.collection("mechanicUsers")
                .document(auth.currentUser!!.uid)
                .update("mechanicStatus", status)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        trySend(ResponseType.Success("Status Updated"))
                    } else {
                        trySend(ResponseType.Success("Something went wrong"))
                    }
                }

            awaitClose {
                close()
            }
        }

}