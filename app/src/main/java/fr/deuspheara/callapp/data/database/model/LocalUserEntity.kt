package fr.deuspheara.callapp.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.database.model.LocalUserEntity
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * User dataclass to describe a user locally
 */
@Entity(tableName = "user")
data class LocalUserEntity(

    @PrimaryKey
    val localId: Long = 0,

    @ColumnInfo(name = "firestore_uuid")
    val fireStoreUUID: String,

    @ColumnInfo(name = "display_name")
    val displayName: String,

    @ColumnInfo(name = "first_name")
    val firstName: String,

    @ColumnInfo(name = "last_name")
    val lastName: String,

    @ColumnInfo(name = "identifier")
    val identifier: String,

    @ColumnInfo(name = "photo_url")
    val photoUrl: String,

    @ColumnInfo(name = "bio")
    val bio: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "phone_number")
    val phoneNumber: String,

    @ColumnInfo(name = "contact_list")
    val contactList: List<String>,

    @ColumnInfo(name = "provider_id")
    val providerId: String,

    @ColumnInfo(name = "is_email_verified")
    val isEmailVerified: Boolean,
)