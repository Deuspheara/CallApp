package fr.deuspheara.callapp.data.datasource.user.model

import com.google.firebase.firestore.DocumentId
import fr.deuspheara.callapp.core.model.text.Identifier

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.datasource.user.model.UserPublicModel
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Public definition of a user, to avoid exposing sensitive information.
 *
 */
data class UserPublicModel(
    @DocumentId
    val uid : String,
    val identifier : String,
    val displayName : String,
    val profilePictureUrl : String,
    val bio : String,
){
    constructor() : this(
        uid = "",
        identifier = "",
        displayName = "",
        profilePictureUrl = "",
        bio = "",
    )
}