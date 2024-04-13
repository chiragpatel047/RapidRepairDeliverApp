package com.chirag047.rapiddeliver.Repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class DataRepository @Inject constructor(val auth: FirebaseAuth, val firestore: FirebaseFirestore) {

}