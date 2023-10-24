package fr.deuspheara.callapp.core.model.user

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.core.model.user.UserFullModel
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 *
 *
 */
data class UserFullModel(
    /** uid of the user */
    val uid: String,

    /** Pseudonym of the user */
    val displayName: String,

    /** Last name of this user */
    val lastName: String = "",

    /** First name of this user */
    val firstName: String = "",


    /** Email address of the user */
    val email: String,

    /** URL pointing to the user's profile picture */
    val photoUrl: String?,

    /** Bio or status message of the user */
    val bio: String?,

    /** Date of the last update of the user */
    val contactList: List<String>
)