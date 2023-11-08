package fr.deuspheara.callapp.data

import fr.deuspheara.callapp.core.model.user.UserFullModel
import fr.deuspheara.callapp.core.model.user.UserLightModel
import fr.deuspheara.callapp.data.datasource.user.model.UserRemoteFirestoreModel
import fr.deuspheara.callapp.data.datasource.user.model.UserRemoteModel

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.DataModelProvider
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Data model provider
 *
 */
object DataModelProvider {

    fun provideRemoteUser(
        email: String = "johndoe@example.com"
    ): UserRemoteModel {
        return UserRemoteModel(
            uuid = "testing",
            displayName = "John Doe",
            lastName = "Doe",
            firstName = "John",
            email = "johndoe@example.com",
            phoneNumber = "1234567890",
            photoUrl = "https://image.testing/profile/testing",
            providerId = "password",
            isEmailVerified = true
        )
    }

    fun providerFirestoreRemoteUser(
        email: String = "johndoe@example.com"
    ): UserRemoteFirestoreModel {
        return UserRemoteFirestoreModel(
            uid = "testing",
            displayName = "John Doe",
            firstName = "John",
            lastName = "Doe",
            email = "johndoe@example.com",
            photoUrl = "https://image.testing/profile/testing",
            bio = "I'm a test",
            phoneNumber = "1234567890",
            contacts = emptyList()
        )
    }

    fun provideUserLightModel(): UserLightModel {
        return UserLightModel(
            uuid = "testing",
            displayName = "John Doe",
            photoUrl = "https://image.testing/profile/testing"
        )
    }

    fun provideUserFullModel(): UserFullModel {
        return UserFullModel(
            uid = "testing",
            displayName = "John Doe",
            lastName = "Doe",
            firstName = "John",
            email = "johndoe@example.com",
            bio = "I'm a test",
            photoUrl = "https://image.testing/profile/testing",
            contactList = emptyList()
        )
    }


}