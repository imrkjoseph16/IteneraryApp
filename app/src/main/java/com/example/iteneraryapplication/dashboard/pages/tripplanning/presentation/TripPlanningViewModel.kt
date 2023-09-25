package com.example.iteneraryapplication.dashboard.pages.tripplanning.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.iteneraryapplication.dashboard.shared.domain.stream.GetDashboardStream
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class TripPlanningViewModel @Inject constructor(
    private val tripPlanningFactory: TripPlanningFactory,
    getDashboardStream: GetDashboardStream
) : ViewModel() {

    val items: LiveData<List<Any>> = getDashboardStream().map { data ->
        tripPlanningFactory.createOverview(data)
    }.asLiveData()
}