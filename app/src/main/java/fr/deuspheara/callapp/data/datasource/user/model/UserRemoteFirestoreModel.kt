package fr.deuspheara.callapp.data.datasource.user.model

import com.google.firebase.firestore.DocumentId
import fr.deuspheara.callapp.core.model.text.Email

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
    val uid : String,
    val displayName : String,
    val firstName : String,
    val lastName : String,
    val email : String,
    val photoUrl : String,
    val bio : String,
    val contactList : List<String>,
){
    constructor() : this(
        uid = "",
        displayName = "",
        firstName = "",
        lastName = "",
        email = "",
        photoUrl = "",
        bio = "",
        contactList = listOf()
    )
}