package com.codeskraps.feature.common.util

import androidx.annotation.StringRes
import com.codeskraps.feature.common.R

enum class WeekString(@StringRes val stringResource: Int) {
    Monday(R.string.monday),
    Tuesday(R.string.tuesday),
    Wednesday(R.string.wednesday),
    Thursday(R.string.thursday),
    Friday(R.string.friday),
    Saturday(R.string.saturday),
    Sunday(R.string.sunday),
    Unknown(R.string.unknown);

    companion object {
        fun parse(dayOfTheWeek: Int): Int {
            return (if (dayOfTheWeek in 1..7) {
                WeekString.entries[dayOfTheWeek - 1]
            } else Unknown).stringResource
        }
    }
}