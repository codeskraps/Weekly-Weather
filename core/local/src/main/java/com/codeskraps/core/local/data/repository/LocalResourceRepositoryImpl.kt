package com.codeskraps.core.local.data.repository

import android.content.Context
import com.codeskraps.core.local.domain.repository.LocalResourceRepository

class LocalResourceRepositoryImpl(
    private val context: Context
) : LocalResourceRepository {
    override fun getCurrentLocationString(): String {
        return "Current Location"
    }

    override fun getLocationCanNotBeBlankString(): String {
        return "Location name cannot be blank"
    }

    override fun getNoResultString(): String {
        return "No results found"
    }

    override fun getIssueLoadingCache(): String {
        return "Issue loading cache"
    }

    override fun getIssueSaving(): String {
        return "Issue saving location"
    }

    override fun getIssueDeleting(): String {
        return "Issue deleting location"
    }

    override fun getUnknownErrorString(): String {
        return "An unknown error occurred"
    }

    override fun getCheckInternetString(): String {
        return "Please check your internet connection"
    }

    override fun getCheckGPSString(): String {
        return "Please enable GPS"
    }
}