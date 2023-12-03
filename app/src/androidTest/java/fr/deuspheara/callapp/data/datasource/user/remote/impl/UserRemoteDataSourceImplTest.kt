package fr.deuspheara.callapp.data.datasource.user.remote.impl

import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import fr.deuspheara.callapp.core.firebase.cleanBeforeTesting
import fr.deuspheara.callapp.data.DataModelProvider
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.datasource.user.remote.impl.UserRemoteDataSourceImplTest
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * User Remote Datasource test
 *
 */
@HiltAndroidTest
class UserRemoteDataSourceImplTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var testDispatcher: TestDispatcher

    @Inject
    lateinit var firestore: FirebaseFirestore

    @Inject
    lateinit var dataSource: UserRemoteDataSourceImpl

    @Before
    fun setUp() {
        hiltRule.inject()
        runBlocking {
            firestore.cleanBeforeTesting()
        }
    }

    @Test
    fun createUser_success() = runTest(testDispatcher) {
        val actual = dataSource.registerUser(
            uid = "testing",
            email = "johndoe@example.com",
            displayName = "John Doe",
            firstName = "John",
            lastName = "Doe",
            phoneNumber = "1234567890",
            photoUrl = "https://image.testing/profile/testing",
            bio = "I'm a test"
        ).first()
        assertEquals("testing", actual)
    }

    @Test
    fun createUser_success_noPhoneNumber() = runTest(testDispatcher) {
        val actual = dataSource.registerUser(
            uid = "testing",
            email = "johndoe@example.com",
            firstName = "John",
            lastName = "Doe",
            phoneNumber = "",
            photoUrl = "https://image.testing/profile/testing",
            bio = "I'm a test",
            displayName = "John Doe"
        ).first()
        assertEquals("testing", actual)
    }

    @Test
    fun updateUser_email() = runTest(testDispatcher) {
        dataSource.registerUser(
            uid = "testing",
            email = "johndoe@example.com",
            firstName = "John",
            lastName = "Doe",
            phoneNumber = "1234567890",
            photoUrl = "https://image.testing/profile/testing",
            bio = "I'm a test",
            displayName = "John Doe"
        ).first()
        val actual = dataSource.updateUserDetails(
            uid = "testing",
            email = "new_value",
            firstName = "John",
            lastName = "Doe",
            profilePictureUrl = "https://image.testing/profile/testing",
            bio = "I'm a test",
            displayName = "John Doe"
        ).first()
        assertEquals(
            DataModelProvider.providerFirestoreRemoteUser().copy(email = "new_value"),
            actual
        )
    }


    @Test
    fun getUser() = runTest(testDispatcher) {
        dataSource.registerUser(
            uid = "testing",
            displayName = "John Doe",
            lastName = "Doe",
            firstName = "John",
            email = "johndoe@example.com",
            phoneNumber = "1234567890",
            photoUrl = "https://image.testing/profile/testing",
            bio = "I'm a test",
        ).first()

        val actual = dataSource.getUserDetails(
            uid = "testing"
        ).first()

        assertEquals(DataModelProvider.providerFirestoreRemoteUser(), actual)
    }

    @Test
    fun addContactToUser() = runTest(testDispatcher) {
        dataSource.registerUser(
            uid = "testing",
            displayName = "John Doe",
            lastName = "Doe",
            firstName = "John",
            email = "johndoe@example.com",
            phoneNumber = "1234567890",
            photoUrl = "https://image.testing/profile/testing",
            bio = "I'm a test",
        ).first()

        val actual = dataSource.addContactToUser(
            uid = "testing",
            contactUid = "testing"
        ).first()

        assertEquals(
            DataModelProvider.providerFirestoreRemoteUser().copy(contacts = listOf("testing")),
            actual
        )
    }


    @Test
    fun getUserContacts() = runTest(testDispatcher) {
        dataSource.registerUser(
            uid = "testing",
            displayName = "John Doe",
            lastName = "Doe",
            firstName = "John",
            email = "johndoe@example.com",
            phoneNumber = "1234567890",
            photoUrl = "https://image.testing/profile/testing",
            bio = "I'm a test",
        ).first()

        dataSource.addContactToUser(
            uid = "testing",
            contactUid = "testing"
        ).first()

        val actual = dataSource.getUserContacts(
            uid = "testing"
        ).first()

        assertEquals(listOf("testing"), actual)
    }

    @Test
    fun removeContactFromUser() = runTest(testDispatcher) {
        dataSource.registerUser(
            uid = "testing",
            displayName = "John Doe",
            lastName = "Doe",
            firstName = "John",
            email = "johndoe@example.com",
            phoneNumber = "1234567890",
            photoUrl = "https://image.testing/profile/testing",
            bio = "I'm a test",
        ).first()

        dataSource.addContactToUser(
            uid = "testing",
            contactUid = "testing"
        ).first()

        val actual = dataSource.removeContactFromUser(
            uid = "testing",
            contactUid = "testing"
        ).first()

        assertEquals(
            DataModelProvider.providerFirestoreRemoteUser().copy(contacts = emptyList()),
            actual
        )
    }


}