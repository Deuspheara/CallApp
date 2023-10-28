package fr.deuspheara.callapp.data.datasource.authentication.remote.impl

import androidx.test.filters.SmallTest
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import fr.deuspheara.callapp.core.firebase.cleanBeforeTesting
import fr.deuspheara.callapp.core.model.CoreModelProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception
import java.time.Instant
import javax.inject.Inject

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.datasource.authentication.remote.impl.AuthenticationRemoteDataSourceImplTest
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Authentication remote datasource implementation tests
 *
 */
@SmallTest
@HiltAndroidTest
class AuthenticationRemoteDataSourceImplTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var testDispatcher: TestDispatcher

    @Inject
    lateinit var dataSource: AuthenticationRemoteDataSourceImpl

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
    fun signUpWithPassword_success() = runTest(testDispatcher) {
        val actual = dataSource.signUpWithPassword(CoreModelProvider.email, CoreModelProvider.password).first()
        val expected = true
        Assert.assertEquals(expected, actual)
    }

    @Test(expected = Exception::class)
    fun signUpWithPassword_failure_collision() = runTest(testDispatcher) {
        dataSource.signUpWithPassword(CoreModelProvider.email, CoreModelProvider.password).first()
        dataSource.signUpWithPassword(CoreModelProvider.email, CoreModelProvider.password).first()
    }


    @Test
    fun signInWithPassword_success() = runTest(testDispatcher) {
        dataSource.signUpWithPassword(CoreModelProvider.email, CoreModelProvider.password).first()
        firebaseAuth.signOut()
        dataSource.signInWithPassword(CoreModelProvider.email, CoreModelProvider.password).first()
        Assert.assertNotNull(firebaseAuth.currentUser)
    }

    @Test(expected = Exception::class)
    fun signInWithPassword_failure_badPassword() = runTest(testDispatcher) {
        dataSource.signUpWithPassword(CoreModelProvider.email, CoreModelProvider.password).first()
        dataSource.signInWithPassword(CoreModelProvider.email, "bad_password").first()
    }

    @Test(expected = Exception::class)
    fun signInWithEmailAndPassword_failure_noUser() = runTest(testDispatcher) {
        dataSource.signInWithPassword(CoreModelProvider.email, CoreModelProvider.password).first()
    }

    @Test
    fun isUserAuthenticated_notAuthenticated() = runTest(testDispatcher) {
        firebaseAuth.signOut()
        Assert.assertFalse(dataSource.isUserAuthenticated().first())
    }

    @Test
    fun isUserAuthenticated_authenticated() = runTest(testDispatcher) {
        dataSource.signUpWithPassword(CoreModelProvider.email, CoreModelProvider.password).first()
        Assert.assertTrue(dataSource.isUserAuthenticated().first())
    }

    @Test
    fun resetPassword_success() = runTest(testDispatcher) {
        dataSource.signUpWithPassword(CoreModelProvider.email, CoreModelProvider.password).first()
        Assert.assertTrue(dataSource.sendPasswordResetEmail(CoreModelProvider.email).first())
    }

    @Test
    fun sendPasswordResetEmail_failure() = runTest(testDispatcher) {
        dataSource.sendPasswordResetEmail(CoreModelProvider.email).first()
    }

}