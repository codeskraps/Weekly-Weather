package com.trifork.feature.common.util

import androidx.annotation.StringRes
import com.trifork.feature.common.R

enum class MonthString(@StringRes val stringResource: Int) {
    January(R.string.january),
    February(R.string.february),
    March(R.string.march),
    April(R.string.april),
    May(R.string.may),
    June(R.string.june),
    July(R.string.july),
    August(R.string.august),
    September(R.string.september),
    October(R.string.october),
    November(R.string.november),
    December(R.string.december),
    Unknown(R.string.unknown);

    companion object {
        fun parse(monthOfTheYear: Int): Int {
            return (if (monthOfTheYear in 1..12) {
                entries[monthOfTheYear - 1]
            } else Unknown).stringResource
        }
    }
}