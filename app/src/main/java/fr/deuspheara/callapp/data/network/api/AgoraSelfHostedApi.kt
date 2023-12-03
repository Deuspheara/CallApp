package fr.deuspheara.callapp.data.network.api

import fr.deuspheara.callapp.data.network.model.TokenRequest
import fr.deuspheara.callapp.data.network.model.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.network.api.AgoraSelfHostedApi
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Agora self hosted api
 *
 */
interface AgoraSelfHostedApi {
    @POST("/getToken")
    suspend fun generateToken(@Body request: TokenRequest): Response<TokenResponse>
}