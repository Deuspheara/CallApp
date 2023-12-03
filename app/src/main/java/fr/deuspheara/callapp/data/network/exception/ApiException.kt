package fr.deuspheara.callapp.data.network.exception

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.network.exception.ApiException
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Api exception
 *
 */

data class ApiException(
    val code: Int,
    val error: String?,
) : Exception(error)
