package fr.deuspheara.callapp.data.repository.user

import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import fr.deuspheara.callapp.core.firebase.cleanBeforeTesting
import fr.deuspheara.callapp.core.model.text.Email
import fr.deuspheara.callapp.core.model.text.PhoneNumber
import fr.deuspheara.callapp.data.DataModelProvider
import fr.deuspheara.callapp.data.repository.user.impl.UserRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.repository.user.UserRepositoryImplTest
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * User repository implementation test
 *
 */
@HiltAndroidTest
class UserRepositoryImplTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var testDispatcher: TestDispatcher

    @Inject
    lateinit var repository: UserRepositoryImpl

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Before
    fun setUp() {
        hiltRule.inject()
        runBlocking {
            firebaseAuth.cleanBeforeTesting()
        }
    }

    @Test
    fun registerUser_success() = runTest(testDispatcher) {
        val actual = repository.registerUser(
            uid = "testing",
            email = Email("johndoe@example.com"),
            firstName = "John",
            lastName = "Doe",
            phoneNumber = PhoneNumber("0123456789"),
            profilePictureUrl = "https://image.testing/profile/testing",
            bio = "This is a test",
            pseudonym = "John Doe"
        ).first()

        Assert.assertEquals("testing", actual)
    }

    @Test
    fun updateUserDetails_success() = runTest(testDispatcher) {
        repository.registerUser(
            uid = "testing",
            email = Email("johndoe@example.com"),
            firstName = "John",
            lastName = "Doe",
            phoneNumber = PhoneNumber("0123456789"),
            profilePictureUrl = "https://image.testing/profile/testing",
            bio = "I'm a test",
            pseudonym = "John Doe"
        ).first()
        val actual = repository.updateUserDetails(
            uid = "testing",
            email = "new_value",
            firstName = "John",
            lastName = "Doe",
            profilePictureUrl = "https://image.testing/profile/testing",
            bio = "I'm a test",
            displayName = "new_value"
        ).first()
        Assert.assertEquals(
            DataModelProvider.provideUserLightModel().copy(displayName = "new_value"), actual
        )
    }

    @Test
    fun addContactToUser_success() = runTest(testDispatcher) {
        repository.registerUser(
            uid = "testing",
            email = Email("johndoe@example.com"),
            firstName = "John",
            lastName = "Doe",
            phoneNumber = PhoneNumber("0123456789"),
            profilePictureUrl = "https://image.testing/profile/testing",
            bio = "I'm a test",
            pseudonym = "John Doe"
        ).first()

        val actual = repository.addContactToUser(
            uid = "testing",
            contactUid = "testing"
        ).first()

        Assert.assertEquals(DataModelProvider.provideUserLightModel(), actual)
    }

    @Test
    fun removeContactFromUser_success() = runTest(testDispatcher) {
        repository.registerUser(
            uid = "testing",
            email = Email("johndoe@example.com"),
            firstName = "John",
            lastName = "Doe",
            phoneNumber = PhoneNumber("0123456789"),
            profilePictureUrl = "https://image.testing/profile/testing",
            bio = "I'm a test",
            pseudonym = "John Doe"
        ).first()

        repository.addContactToUser(
            uid = "testing",
            contactUid = "testing"
        ).first()

        val actual = repository.removeContactFromUser(
            uid = "testing",
            contactUid = "testing"
        ).first()

        Assert.assertEquals(DataModelProvider.provideUserLightModel(), actual)

    }

    @Test
    fun getUserDetails_success() = runTest(testDispatcher) {
        repository.registerUser(
            uid = "testing",
            email = Email("johndoe@example.com"),
            firstName = "John",
            lastName = "Doe",
            phoneNumber = PhoneNumber("0123456789"),
            profilePictureUrl = "https://image.testing/profile/testing",
            bio = "I'm a test",
            pseudonym = "John Doe"
        ).first()

        val actual = repository.getUserDetails(
            uid = "testing"
        ).first()

        Assert.assertEquals(DataModelProvider.provideUserFullModel(), actual)
    }

    @Test
    fun getUserContacts_success() = runTest(testDispatcher) {
        repository.registerUser(
            uid = "testing",
            email = Email("johndoe@example.com"),
            firstName = "John",
            lastName = "Doe",
            phoneNumber = PhoneNumber("0123456789"),
            profilePictureUrl = "https://image.testing/profile/testing",
            bio = "I'm a test",
            pseudonym = "John Doe"
        ).first()

        repository.addContactToUser(
            uid = "testing",
            contactUid = "testing"
        ).first()

        val actual = repository.getUserContacts(
            uid = "testing"
        ).first()

        Assert.assertEquals(listOf("testing"), actual)
    }

}