package fr.deuspheara.callapp.core.model.user

import fr.deuspheara.callapp.data.datasource.user.model.UserRemoteModel

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.core.model.user.UserLightModel
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Light model of UserRemoteModel
 *
 */
data class UserLightModel(
    val uuid: String,
    val displayName: String,
    val photoUrl: String
)