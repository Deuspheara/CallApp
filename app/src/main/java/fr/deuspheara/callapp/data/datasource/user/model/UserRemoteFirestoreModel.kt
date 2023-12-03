package fr.deuspheara.callapp.data.datasource.user.model

import com.google.firebase.firestore.DocumentId

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.datasource.user.model.UserRemoteFirestoreModel
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Remote firestore user model
 *
 */
data class UserRemoteFirestoreModel(
    @DocumentId
    val uid: String,
    val identifier: String,
    val displayName: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val photoUrl: String,
    val bio: String,
    val phoneNumber: String,
    val contacts: List<String>,
) {
    constructor() : this(
        uid = "",
        identifier = "",
        displayName = "",
        firstName = "",
        lastName = "",
        email = "",
        photoUrl = "",
        bio = "",
        phoneNumber = "",
        contacts = emptyList()
    )
}