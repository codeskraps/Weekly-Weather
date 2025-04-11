package com.codeskraps.core.local.domain.repository

interface LocalResourceRepository {
    fun getCurrentLocationString(): String
    fun getLocationCanNotBeBlankString(): String
    fun getNoResultString(): String
    fun getIssueLoadingCache(): String
    fun getIssueSaving(): String
    fun getIssueDeleting(): String
    fun getUnknownErrorString(): String
    fun getCheckInternetString(): String
    fun getCheckGPSString(): String
}