package fr.deuspheara.callapp.data.datasource.user.model

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
data class UserRemote(
    /** uid of the user */
    val uid: String,

    /** Pseudonym of the user */
    val pseudonym: String,

    /** Real name of the user */
    val realName: String,

    /** Email address of the user */
    val email: String,

    /** URL pointing to the user's profile picture */
    val profilePictureUrl: String?,

    /** Bio or status message of the user */
    val bio: String?,

    /** Date of the last update of the user */
    val contactList: List<String>
)
