package fr.deuspheara.callapp.data.datasource.authentication.remote.impl

import androidx.test.filters.SmallTest
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import fr.deuspheara.callapp.core.firebase.cleanBeforeTesting
import fr.deuspheara.callapp.core.model.CoreModelProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.AssertionError
import java.time.Instant
import javax.inject.Inject
import kotlin.Exception

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
        val expected = firebaseAuth.currentUser?.uid!!
        Assert.assertEquals(expected, actual)
    }

    @Test(expected = IllegalStateException::class)
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

    @Test(expected = IllegalStateException::class)
    fun signInWithPassword_failure_badPassword() = runTest(testDispatcher) {
        dataSource.signUpWithPassword(CoreModelProvider.email, CoreModelProvider.password).first()
        dataSource.signInWithPassword(CoreModelProvider.email, "bad_password").first()
    }

    @Test(expected = IllegalStateException::class)
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
    fun sendPasswordResetEmail_success() = runTest(testDispatcher) {
        dataSource.signUpWithPassword(CoreModelProvider.email, CoreModelProvider.password).first()
        Assert.assertTrue(dataSource.sendPasswordResetEmail(CoreModelProvider.email).first())
    }

    @Test
    fun sendPasswordResetEmail_failure() = runTest(testDispatcher) {
        val result = dataSource.sendPasswordResetEmail(CoreModelProvider.email).first()
        Assert.assertFalse(result)
    }

    @Test
    fun confirmResetPassword_wrong_code() = runTest(testDispatcher) {
        val result = dataSource.confirmResetPassword("wrongCode", CoreModelProvider.password).first()
        Assert.assertFalse(result)
    }

    @Test
    fun checkActionCode_wrongCode() = runTest(testDispatcher) {
        dataSource.checkActionCode("wrongCode").first()
    }

    @Test
    fun resetPassword_success() = runTest(testDispatcher) {
        dataSource.signUpWithPassword(CoreModelProvider.email, CoreModelProvider.password).first()
        val result = dataSource.resetPassword(CoreModelProvider.password).first()
        Assert.assertNotNull(result)
    }

}