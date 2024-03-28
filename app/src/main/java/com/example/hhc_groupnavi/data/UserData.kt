package com.example.hhc_groupnavi.data

data class UserData(
    var systemId: String? = null,
    var userId: String? = null,
    var userName: String? = null,
    var userImageUrl: String? = null,
    var userCar: CarData? = null,
    var groupSystemId: String? = null,
) {
    fun toMap() = mapOf(
        "systemId" to systemId,
        "userId" to userId,
        "userName" to userName,
        "userImageUrl" to userImageUrl,
        "userCar" to userCar,
        "groupSystemId" to groupSystemId,
    )
}
