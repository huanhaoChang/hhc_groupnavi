package com.example.hhc_groupnavi.system

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.hhc_groupnavi.data.CarData
import com.example.hhc_groupnavi.data.Event
import com.example.hhc_groupnavi.data.GROUPS
import com.example.hhc_groupnavi.data.GroupData
import com.example.hhc_groupnavi.data.USERS
import com.example.hhc_groupnavi.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class GroupNaviViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore,
    val storage: FirebaseStorage,
) : ViewModel() {

    val signedIn = mutableStateOf(false)
    val inProgress = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)
    val groupData = mutableStateOf<GroupData?>(null)
    val popupNotification = mutableStateOf<Event<String>?>(null)

    val searchGroup = mutableStateOf<GroupData?>(null)
    val searchGroupProgress = mutableStateOf(false)

    val searchMember = mutableStateOf<UserData?>(null)
    val searchMemberProgress = mutableStateOf(false)

    init {
        val currentUser = auth.currentUser
        signedIn.value = currentUser != null
        currentUser?.uid?.let { uid ->
            getUserData(uid)
        }
    }

    // EXCEPTION
    private fun handleException(
        exception: Exception? = null,
        customMessage: String = ""
    ) {
        exception?.printStackTrace()

        val errorMessage = exception?.localizedMessage ?: ""

        val message = if (customMessage.isEmpty()) {
            errorMessage
        } else {
            "$customMessage: $errorMessage"
        }

        popupNotification.value = Event(message)
    }

    // SIGNUP
    fun onSignUp(
        userid: String,
        username: String,
        email: String,
        password: String,
    ) {
        if (userid.isEmpty() or username.isEmpty() or email.isEmpty() or password.isEmpty()) {
            handleException(
                customMessage = "Please Fill in All Fields"
            )
            return
        }

        inProgress.value = true

        db.collection(USERS).whereEqualTo("userId", userid).get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                signedIn.value = true
                                createOrUpdateUserData(
                                    userid = userid,
                                    username = username
                                )
                            } else {
                                handleException(task.exception, "Signup Failed")
                            }
                        }
                } else {
                    handleException(customMessage = "ID Already Exists")
                }
                inProgress.value = false
            }
            .addOnFailureListener {
                handleException(it)
            }
    }

    // LOGIN
    fun onLogIn(
        email: String,
        password: String,
    ) {
        if (email.isEmpty() or password.isEmpty()) {
            handleException(
                customMessage = "Please Fill in All Fields"
            )
            return
        }

        inProgress.value = true

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    signedIn.value = true
                    inProgress.value = false
                    auth.currentUser?.uid?.let { uid ->
                        getUserData(uid)
                    }
                } else {
                    handleException(
                        customMessage = "Log In Failed"
                    )
                }
            }
            .addOnFailureListener {
                handleException(customMessage = "Log In Failed")
            }
    }

    // LOGOUT
    fun onLogOut() {
        auth.signOut()
        signedIn.value = false
        userData.value = null
        groupData.value = null
        popupNotification.value = Event("Logged Out!")
    }

    // GET USER DATA
    private fun getUserData(uid: String) {

        inProgress.value = true

        db.collection(USERS).document(uid).get()
            .addOnSuccessListener {
                val user = it.toObject<UserData>()
                userData.value = user
                user?.groupSystemId?.let { groupsystemid ->
                    if (groupsystemid.isNotEmpty())
                        getGroupData(groupsystemid)
                }
                inProgress.value = false
            }
            .addOnFailureListener {
                handleException(
                    it,
                    "Cannot Retrieve User Data"
                )
            }
    }

    // GET GROUP DATA
    private fun getGroupData(groupsystemid: String) {

        inProgress.value = true

        db.collection(GROUPS).document(groupsystemid).get()
            .addOnSuccessListener {
                val group = it.toObject<GroupData>()
                groupData.value = group
                inProgress.value = false
            }
            .addOnFailureListener {
                handleException(
                    it,
                    "Cannot Retrieve Group Data"
                )
            }
    }

    // CREATE OR UPDATE USERDATA
    private fun createOrUpdateUserData(
        userid: String? = null,
        username: String? = null,
        userimageurl: String? = null,
        carbrand: String? = null,
        carmodel: String? = null,
        carimageurl: String? = null,
        groupsystemid: String? = null,
    ) {
        val uid = auth.currentUser?.uid
        val newCarData = CarData(
            carBrand = carbrand ?: userData.value?.userCar?.carBrand,
            carModel = carmodel ?: userData.value?.userCar?.carModel,
            carImageUrl = carimageurl ?: userData.value?.userCar?.carImageUrl,
        )
        val newUserData = UserData(
            systemId = uid,
            userId = userid ?: userData.value?.userId,
            userName = username ?: userData.value?.userName,
            userImageUrl = userimageurl ?: userData.value?.userImageUrl,
            userCar = newCarData,
            groupSystemId = groupsystemid ?: userData.value?.groupSystemId,
        )

        uid?.let { uid ->
            inProgress.value = true
            db.collection(USERS).document(uid).get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        it.reference.update(newUserData.toMap())
                            .addOnSuccessListener {
                                this.userData.value = newUserData
                                inProgress.value = false
                            }
                            .addOnFailureListener { error ->
                                handleException(error, "Cannot Update User")
                            }
                    } else {
                        db.collection(USERS).document(uid).set(newUserData)
                        inProgress.value = false
                        getUserData(uid)
                    }
                }
                .addOnFailureListener { error ->
                    handleException(error, "Cannot Update User")
                }
        }
    }

    // UPDATE OTHER USER DATA
    private fun updateOtherUserGroupSystemId(
        usersystemid: String,
        groupsystemid: String,
    ) {
        inProgress.value = true
        db.collection(USERS).document(usersystemid).update("groupSystemId", groupsystemid)
            .addOnSuccessListener {
                inProgress.value = false
            }
            .addOnFailureListener {
                handleException(it, "Cannot Update User Group System ID")
            }
    }

    // UPDATE PROFILE INFO OF USER DATA
    fun updateProfileData(
        userid: String,
        username: String,
        carbrand: String,
        carmodel: String,
    ) {
        createOrUpdateUserData(
            userid = userid,
            username = username,
            carbrand = carbrand,
            carmodel = carmodel,
        )
    }

    // UPDATE USER IMAGE URL OF USER DATA
    fun updateUserImageData(uri: Uri) {
        uploadImageToStorage(uri) {
            createOrUpdateUserData(
                userimageurl = it.toString()
            )
        }
    }

    // UPDATE CAR IMAGE URL OF USER DATA
    fun updateCarImageData(uri: Uri) {
        uploadImageToStorage(uri) {
            createOrUpdateUserData(
                carimageurl = it.toString()
            )
        }
    }

    // UPLOAD IMAGE TO STORAGE
    private fun uploadImageToStorage(
        uri: Uri,
        onSuccess: (Uri) -> Unit,
    ) {
        inProgress.value = true
        val storageRef = storage.reference
        val uuid = UUID.randomUUID()
        val imageRef = storageRef.child("image/$uuid")
        val uploadTask = imageRef.putFile(uri)

        uploadTask
            .addOnSuccessListener {
                val result = it.metadata?.reference?.downloadUrl
                result?.addOnSuccessListener(onSuccess)
                inProgress.value = false
            }
            .addOnFailureListener {
                handleException(it, "Cannot upload image")
            }
    }

    // GET GROUP MEMBERS DATA
    suspend fun getMemberData(membersystemid: String?): UserData? {
        return membersystemid?.let {
            try {
                val memberData = db.collection(USERS).document(it).get().await()
                memberData.toObject<UserData>()
            } catch (e: Exception) {
                handleException(e, "Cannot Retrieve Member Data")
                null
            }
        }
    }

    // CREATE NEW GROUP
    fun createNewGroup(
        groupid: String,
        groupname: String,
        groupmembers: List<String>,
        groupimageUrlLocal: String? = null,
    ) {
        if (groupid.isEmpty() or groupname.isEmpty()) {
            handleException(
                customMessage = "Please Fill in All Fields"
            )
            return
        }
        db.collection(GROUPS).whereEqualTo("groupId", groupid).get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    groupimageUrlLocal?.let {
                        uploadImageToStorage(Uri.parse(it)) {
                            inProgress.value = true
                            val groupimageUrl = it.toString()
                            val systemid = db.collection(GROUPS).document().id
                            val newGroupData = GroupData(
                                systemId = systemid,
                                groupId = groupid,
                                groupName = groupname,
                                groupMembers = groupmembers,
                                groupImageUrl = groupimageUrl,
                            )
                            db.collection(GROUPS).document(systemid).set(newGroupData)
                                .addOnSuccessListener {
                                    inProgress.value = false
                                    getGroupData(systemid)
                                    createOrUpdateUserData(groupsystemid = systemid)
                                    popupNotification.value = Event("Group Created!")
                                }
                                .addOnFailureListener { error ->
                                    handleException(error, "Cannot Create Group")
                                }
                        }
                    } ?: run {
                        inProgress.value = true
                        val systemid = db.collection(GROUPS).document().id
                        val newGroupData = GroupData(
                            systemId = systemid,
                            groupId = groupid,
                            groupName = groupname,
                            groupMembers = groupmembers,
                            groupImageUrl = "",
                        )
                        db.collection(GROUPS).document(systemid).set(newGroupData)
                            .addOnSuccessListener {
                                inProgress.value = false
                                getGroupData(systemid)
                                createOrUpdateUserData(groupsystemid = systemid)
                                popupNotification.value = Event("Group Created!")
                            }
                            .addOnFailureListener { error ->
                                handleException(error, "Cannot Create Group")
                            }
                    }
                } else {
                    handleException(customMessage = "Group ID Already Exists")
                }
            }
            .addOnFailureListener {
                handleException(it)
            }
    }

    // UPDATE GROUP DATA
    private fun updateGroupData(
        groupsystemid: String? = null,
        groupid: String? = null,
        groupname: String? = null,
        groupimageurl: String? = null,
        groupmembers: List<String>? = null,
        onCallBack: (() -> Unit)? = null,
    ) {
        groupsystemid?.let { groupsystemid ->
            inProgress.value = true
            db.collection(GROUPS).document(groupsystemid).get()
                .addOnSuccessListener {
                    val group = it.toObject<GroupData>()
                    group?.let { groupData ->
                        val newGroupData = GroupData(
                            systemId = groupsystemid,
                            groupId = groupid ?: groupData.groupId,
                            groupName = groupname ?: groupData.groupName,
                            groupImageUrl = groupimageurl ?: groupData.groupImageUrl,
                            groupMembers = groupmembers ?: groupData.groupMembers,
                        )
                        db.collection(GROUPS).document(groupsystemid).update(newGroupData.toMap())
                            .addOnSuccessListener {
                                onCallBack?.invoke()
                                inProgress.value = false
                            }
                            .addOnFailureListener { error ->
                                handleException(error, "Cannot Update Group Info")
                            }
                    }
                }
                .addOnFailureListener {
                    handleException(it, "Cannot Update Group Info")
                }
        }
    }

    // UPDATE GROUP PROFILE DATA
    fun updateGroupProfileData(
        groupsystemid: String,
        groupid: String,
        groupname: String,
    ) {
        updateGroupData(
            groupsystemid = groupsystemid,
            groupid = groupid,
            groupname = groupname
        ) {
            getGroupData(groupsystemid)
        }
    }

    fun updateGroupImage(
        uri: Uri,
        groupsystemid: String,
    ) {
        uploadImageToStorage(uri) {
            updateGroupData(
                groupsystemid = groupsystemid,
                groupimageurl = it.toString()
            ) {
                getGroupData(groupsystemid)
            }
        }
    }

    // SEARCH GROUP
    fun searchGroup(searchgroupid: String) {
        searchGroupProgress.value = true
        db.collection(GROUPS).whereEqualTo("groupId", searchgroupid).get()
            .addOnSuccessListener {
                val group = it.toObjects<GroupData>().firstOrNull()
                searchGroup.value = group
                searchGroupProgress.value = false
            }
            .addOnFailureListener {
                handleException(it, "Cannot Find Group")
            }
    }

    // JOIN GROUP
    fun joinGroup(groupsystemid: String? = null) {
        groupsystemid?.let { groupsystemid ->
            createOrUpdateUserData(groupsystemid = groupsystemid)
            auth.currentUser?.uid?.let { currentusersystemid ->
                val members = arrayListOf<String>()
                db.collection(GROUPS).document(groupsystemid).get()
                    .addOnSuccessListener {
                        val group = it.toObject<GroupData>()
                        group?.groupMembers?.let { groupmembers ->
                            members.addAll(groupmembers)
                        }
                        members.add(currentusersystemid)
                        updateGroupData(
                            groupsystemid = groupsystemid,
                            groupmembers = members
                        ) {
                            getGroupData(groupsystemid)
                            searchGroup.value = null
                        }
                    }
                    .addOnFailureListener {
                        handleException(it, "Cannot Join Group")
                    }
            }
        }
    }

    // LEAVE GROUP
    fun leaveGroup(groupsystemid: String? = null) {
        groupsystemid?.let { groupsystemid ->
            createOrUpdateUserData(groupsystemid = "")
            auth.currentUser?.uid?.let { currentusersystemid ->
                val members = arrayListOf<String>()
                db.collection(GROUPS).document(groupsystemid).get()
                    .addOnSuccessListener {
                        val group = it.toObject<GroupData>()
                        group?.groupMembers?.let { groupmembers ->
                            members.addAll(groupmembers)
                        }
                        members.remove(currentusersystemid)
                        updateGroupData(
                            groupsystemid = groupsystemid,
                            groupmembers = members
                        )
                    }
                    .addOnFailureListener {
                        handleException(it, "Cannot Leave Group")
                    }
            }
            groupData.value = null
        }
    }

    // SEARCH USER FOR GROUP
    fun searchMember(userid: String) {
        searchMemberProgress.value = true
        db.collection(USERS).whereEqualTo("userId", userid).get()
            .addOnSuccessListener {
                val user = it.toObjects<UserData>().firstOrNull()
                searchMember.value = user
                searchMemberProgress.value = false
            }
            .addOnFailureListener {
                handleException(it, "Cannot Find User")
            }
    }

    // ADD USER TO GROUP
    fun addMember(
        membersystemid: String,
        groupsystemid: String,
    ) {
        val members = arrayListOf<String>()
        db.collection(GROUPS).document(groupsystemid).get()
            .addOnSuccessListener {
                val group = it.toObject<GroupData>()
                group?.groupMembers?.let { groupmembers ->
                    members.addAll(groupmembers)
                }
                members.add(membersystemid)
                updateGroupData(
                    groupsystemid = groupsystemid,
                    groupmembers = members
                ) {
                    getGroupData(groupsystemid)
                    updateOtherUserGroupSystemId(
                        usersystemid = membersystemid,
                        groupsystemid = groupsystemid,
                    )
                    searchMember.value = null
                }
            }
            .addOnFailureListener {
                handleException(it, "Cannot Add Member")
            }
    }


}