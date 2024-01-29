package com.codeskraps.core.local.domain.repository

interface LocalResourceRepository {

    suspend fun getUnknownErrorString(): String
    suspend fun getCurrentLocationString(): String
    suspend fun getCheckInternetString(): String
    suspend fun getCheckGPSString(): String
    suspend fun getLocationCanNotBeBlankString(): String
    suspend fun getNoResultString(): String
    suspend fun getIssueLoadingCache(): String
    suspend fun getIssueSaving(): String
    suspend fun getIssueDeleting(): String
}