package com.codeskraps.core.local.data.repository

import android.content.res.Resources
import com.codeskraps.feature.common.R
import com.codeskraps.core.local.domain.repository.LocalResourceRepository
import javax.inject.Inject

class LocalResourceRepositoryImpl @Inject constructor(
    private val resources: Resources
) : LocalResourceRepository {
    override suspend fun getUnknownErrorString(): String {
        return resources.getString(R.string.unknown_error)
    }

    override suspend fun getCurrentLocationString(): String {
        return resources.getString(R.string.current_location)
    }

    override suspend fun getCheckInternetString(): String {
        return resources.getString(R.string.check_internet_connection)
    }

    override suspend fun getCheckGPSString(): String {
        return resources.getString(R.string.check_gps)
    }

    override suspend fun getLocationCanNotBeBlankString(): String {
        return resources.getString(R.string.location_can_not_be_blank)
    }

    override suspend fun getNoResultString(): String {
        return resources.getString(R.string.no_results)
    }

    override suspend fun getIssueLoadingCache(): String {
        return resources.getString(R.string.issue_loading_cache)
    }

    override suspend fun getIssueSaving(): String {
        return resources.getString(R.string.issue_saving)
    }

    override suspend fun getIssueDeleting(): String {
        return resources.getString(R.string.issue_deleting)
    }
}