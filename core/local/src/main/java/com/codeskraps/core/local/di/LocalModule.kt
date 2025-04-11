package com.codeskraps.core.local.di

import android.content.Context
import com.codeskraps.core.local.data.db.GeocodingDB
import com.codeskraps.core.local.data.repository.LocalGeocodingRepositoryImpl
import com.codeskraps.core.local.data.repository.LocalResourceRepositoryImpl
import com.codeskraps.core.local.domain.repository.LocalGeocodingRepository
import com.codeskraps.core.local.domain.repository.LocalResourceRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localModule = module {
    single<LocalResourceRepository> { 
        LocalResourceRepositoryImpl(androidContext())
    }
    
    single { 
        GeocodingDB.getInstance(androidContext())
    }
    
    single<LocalGeocodingRepository> { 
        LocalGeocodingRepositoryImpl(
            geocodingDB = get(),
            localResource = get()
        )
    }
}