package com.chirag047.rapiddeliver.Model

data class OrderModel(
    val orderId: String = "",
    val userId: String = "",
    val corporateId: String = "",
    val corporateName: String = "",
    val corporateAddress: String = "",
    val vehicleOwner: String = "",
    val vehicleType: String = "",
    val vehicleCompany: String = "",
    val vehicleModel: String = "",
    val vehicleFuelType: String = "",
    val vehicleLicensePlate: String = "",
    val serviceType: String = "",
    val clientAddress: String = "",
    val clientLatitude: String = "",
    val clientLongitude: String = "",
    val clientAddedText: String = "",
    val orderStatus: String = ""
)
