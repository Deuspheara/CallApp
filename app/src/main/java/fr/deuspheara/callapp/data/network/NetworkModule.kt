package fr.deuspheara.callapp.data.network

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.deuspheara.callapp.BuildConfig
import fr.deuspheara.callapp.data.network.api.AgoraSelfHostedApi
import fr.deuspheara.callapp.data.network.exception.ApiException
import fr.deuspheara.callapp.data.network.json.parse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.network.NetworkModule
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Network module
 *
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides the OkHttpClient object.
     *
     * @return The [OkHttpClient] object
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
        }.build()
    }

    /**
     * Provides the Gson object.
     *
     * @return The [Gson] object
     */
    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }

    /**
     * Provides the Retrofit object.
     *
     * @param okHttpClient The [OkHttpClient] object used to instantiate the Retrofit object
     * @param gson The [Gson] object used to instantiate the Retrofit object
     *
     * @return The [Retrofit] object
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.AGORA_SELF_HOSTED_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }


    @Provides
    fun provideAgoraSelfHostedApi(retrofit: Retrofit): AgoraSelfHostedApi {
        return retrofit.create(AgoraSelfHostedApi::class.java)
    }

    @Throws(ApiException::class)
    fun <Remote> Response<Remote>.safeUnwrap(): Remote {
        return if (this.isSuccessful) {
            this.body() ?: throw ApiException(
                code = this.code(),
                error = null,

                )
        } else throw ApiException(
            code = this.code(),
            error = this.errorBody()?.string()?.let(Gson()::parse)
        )

    }
}