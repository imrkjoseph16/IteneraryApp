package com.example.iteneraryapplication.dashboard.shared.domain.stream

import com.example.iteneraryapplication.dashboard.shared.data.DashboardRepository
import dagger.Reusable
import javax.inject.Inject

@Reusable
class GetDashboardStream @Inject constructor(
    private val repository: DashboardRepository
) {
    operator fun invoke() = repository.getDashboardStream()
}