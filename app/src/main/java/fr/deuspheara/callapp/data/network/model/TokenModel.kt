package fr.deuspheara.callapp.data.network.model

import com.google.gson.annotations.SerializedName

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.network.model.TokenModel
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Token model
 *
 */

data class TokenResponse(
    /** The generated token. */
    @SerializedName("token")
    val token: String
)

data class TokenRequest(
    /** Type of token (e.g., "rtc", "rtm", "chat"). */
    @SerializedName("tokenType")
    val tokenType: String,

    /** Channel name for RTC and RTM tokens. */
    @SerializedName("channel")
    val channel: String,

    /** Role of the user ("publisher" or "subscriber"). */
    @SerializedName("role")
    val role: String,

    /** User ID associated with the token. */
    @SerializedName("uid")
    val uid: String,

    /** Expiration time in seconds (optional, default: 3600). */
    @SerializedName("expire")
    val expire: Int
)
