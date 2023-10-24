package fr.deuspheara.callapp.data.datasource.user.model

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
)