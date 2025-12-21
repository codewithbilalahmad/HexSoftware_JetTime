package com.muhammad.jettime.di

import com.muhammad.jettime.data.TimeZoneRepositoryImp
import com.muhammad.jettime.domain.repository.TimeZoneRepository
import com.muhammad.jettime.presentation.screens.home.HomeViewModel
import com.muhammad.jettime.presentation.screens.timezone_converter.TimezoneConverterViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    singleOf(::TimeZoneRepositoryImp).bind<TimeZoneRepository>()
    viewModelOf(::HomeViewModel)
    viewModelOf(::TimezoneConverterViewModel)
}
