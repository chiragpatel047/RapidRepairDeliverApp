package com.chirag047.rapiddeliver.Repository

import android.content.SharedPreferences
import android.net.Uri
import com.chirag047.rapiddeliver.Api.NotificationApi
import com.chirag047.rapiddeliver.Common.ResponseType
import com.chirag047.rapiddeliver.Model.Coordinates
import com.chirag047.rapiddeliver.Model.FirebaseNotificationModel
import com.chirag047.rapiddeliver.Model.NotificationModel
import com.chirag047.rapiddeliver.Model.OrderModel
import com.chirag047.rapiddeliver.Model.PushNotification
import com.chirag047.rapiddeliver.Model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

class DataRepository @Inject constructor(
    val auth: FirebaseAuth,
    val firestore: FirebaseFirestore,
    val storage: FirebaseStorage,
    val notificationApi: NotificationApi
) {

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

    suspend fun startMechanicService(
        orderId: String,
        centerId: String,
        ownerName: String,
        userId: String
    ): Flow<ResponseType<String>> =
        callbackFlow {

            trySend(ResponseType.Loading())

            firestore.collection("orders")
                .document(orderId)
                .update("orderStatus", "Live")
                .await()

            firestore.collection("mechanicUsers")
                .document(auth.currentUser!!.uid)
                .update("mechanicStatus", "On service")
                .await()

            val notify = withContext(Dispatchers.IO) {

                val notification = PushNotification(
                    FirebaseNotificationModel(
                        ownerName + "'s service is stared by mechanic",
                        "Click here for live track mechanic"
                    ), "/topics/" + centerId
                )

                try {
                    val respose = notificationApi.postNotification(notification)
                    trySend(ResponseType.Success("Started successfully"))

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            val notify2 = withContext(Dispatchers.IO) {

                val notification = PushNotification(
                    FirebaseNotificationModel(
                        "Your service is started by mechanic",
                        "Click here for live track mechanic"
                    ), "/topics/" + userId
                )

                try {
                    val respose = notificationApi.postNotification(notification)
                    trySend(ResponseType.Success("Started successfully"))

                } catch (e: Exception) {
                    e.printStackTrace()
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


    suspend fun doneMechanicService(
        orderId: String,
        centerId: String,
        userId: String,
        ownerName: String
    ): Flow<ResponseType<String>> =
        callbackFlow {
            trySend(ResponseType.Loading())

            val sdf = SimpleDateFormat("hh:mm a | dd MMMM yyyy ")
            val currentDate = sdf.format(Date())

            firestore.collection("orders")
                .document(orderId)
                .update("orderStatus", "Done", "orderInfo", "Done at : " + currentDate)
                .await()

            firestore.collection("mechanicUsers")
                .document(auth.currentUser!!.uid)
                .update("mechanicStatus", "Available")
                .await()

            val notify = withContext(Dispatchers.IO) {

                val notification = PushNotification(
                    FirebaseNotificationModel(
                        "Service done successfully",
                        ownerName + "'s service is done by mechanic"
                    ), "/topics/" + centerId
                )

                try {
                    val respose = notificationApi.postNotification(notification)
                    trySend(ResponseType.Success("Started successfully"))

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }


            val notify2 = withContext(Dispatchers.IO) {

                val notification = PushNotification(
                    FirebaseNotificationModel(
                        "Service done successfully",
                        "Your service is done by mechanic"
                    ), "/topics/" + userId
                )

                try {
                    val respose = notificationApi.postNotification(notification)
                    trySend(ResponseType.Success("Started successfully"))

                } catch (e: Exception) {
                    e.printStackTrace()
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

    suspend fun updateUserProfilePictureAndPhone(
        userImage: String, userName: String, phoneNo: String, sharedPreferences: SharedPreferences
    ): Flow<ResponseType<String>> = callbackFlow {
        trySend(ResponseType.Loading())

        if (!userImage.equals("")) {

            val ref = storage.reference.child("mechanicUser").child("userProfilePhotos")
                .child(System.currentTimeMillis().toString())

            ref.putFile(Uri.parse(userImage)).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    sharedPreferences.edit().putString("profileImage", it.toString()).apply()

                    firestore.collection("mechanicUsers").document(auth.currentUser!!.uid)
                        .update("userImage", it, "userName", userName, "phoneNo", phoneNo)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                trySend(ResponseType.Success("Updated successfully"))
                            }
                        }
                }
            }
        } else {

            firestore.collection("mechanicUsers").document(auth.currentUser!!.uid)
                .update("userName", userName, "phoneNo", phoneNo).addOnCompleteListener {
                    if (it.isSuccessful) {
                        trySend(ResponseType.Success("Updated successfully"))
                    }
                }
        }
        awaitClose {
            close()
        }
    }


    suspend fun getMyAllNotifications(): Flow<ResponseType<List<NotificationModel>?>> =
        callbackFlow {

            trySend(ResponseType.Loading())

            firestore.collection("mechanicUsers")
                .document(auth.currentUser!!.uid)
                .collection("notifications")
                .addSnapshotListener { value, error ->
                    trySend(ResponseType.Success(value!!.toObjects(NotificationModel::class.java))!!)
                }

            awaitClose {
                close()
            }
        }

}