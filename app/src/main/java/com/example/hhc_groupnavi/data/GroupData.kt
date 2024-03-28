package com.example.hhc_groupnavi.data

data class GroupData(
    var systemId: String? = null,
    var groupId: String? = null,
    var groupName: String? = null,
    var groupImageUrl: String? = null,
    var groupMembers: List<String>? = null,
) {
    fun toMap() = mapOf(
        "systemId" to systemId,
        "groupId" to groupId,
        "groupName" to groupName,
        "groupImageUrl" to groupImageUrl,
        "groupMembers" to groupMembers,
    )
}