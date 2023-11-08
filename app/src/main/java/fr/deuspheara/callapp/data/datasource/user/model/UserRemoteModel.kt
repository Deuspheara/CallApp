package fr.deuspheara.callapp.data.datasource.user.model

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentId

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.datasource.user.model.UserRemote
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Remote User
 *
 */
data class UserRemoteModel(

    /** Id of the remote resource */
    @DocumentId
    val uuid: String = "",

    /** Display name of this user */
    val displayName: String = "",

    /** Last name of this user */
    val lastName: String = "",

    /** First name of this user */
    val firstName: String = "",

    /** Email of this user */
    val email: String = "",

    /** Phone number of this user */
    val phoneNumber: String = "",

    /** Url of the profile picture */
    val photoUrl: String = "",

    /** Which provider is used to log in */
    val providerId: String = "",

    /** Check if email is verified */
    val isEmailVerified: Boolean = false
) {
    constructor(firebaseUser: FirebaseUser) : this(
        uuid = firebaseUser.uid,
        displayName = firebaseUser.displayName ?: "",
        email = firebaseUser.providerData[1].email ?: "",
        phoneNumber = firebaseUser.providerData[1].phoneNumber ?: "",
        photoUrl = firebaseUser.photoUrl.toString(),
        providerId = firebaseUser.providerData[1].providerId,
        isEmailVerified = firebaseUser.isEmailVerified
    )
}
