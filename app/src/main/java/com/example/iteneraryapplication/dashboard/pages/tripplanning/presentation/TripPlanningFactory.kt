package com.example.iteneraryapplication.dashboard.pages.tripplanning.presentation

import com.example.iteneraryapplication.R
import com.example.iteneraryapplication.app.shared.component.TextLine
import com.example.iteneraryapplication.app.shared.dto.data.NoteListItem
import com.example.iteneraryapplication.app.shared.dto.layout.NoteItemViewDto
import com.example.iteneraryapplication.app.shared.dto.layout.SpaceItemViewDto
import com.example.iteneraryapplication.dashboard.shared.domain.data.Notes
import com.example.iteneraryapplication.dashboard.shared.presentation.DashboardState
import com.example.iteneraryapplication.dashboard.shared.presentation.ShowGetNoteSuccess
import javax.inject.Inject

class TripPlanningFactory @Inject constructor() {

    fun createOverview(state: DashboardState) = when(state) {
        is ShowGetNoteSuccess -> state.notes?.let { prepareList(it) } ?: emptyList()
        else -> listOf(SpaceItemViewDto(R.dimen.grid_0))
    }

    /**
     * (prepareList) creating the layout component.
     * */
    private fun prepareList(notes: List<Notes>) = notes.mapIndexed { _, data ->
        NoteListItem(
            dto = NoteItemViewDto(
                itemTitle = TextLine(text = data.notesTitle),
                itemDateSaved = TextLine(text = data.notesDateSaved),
                itemSubtitle = TextLine(text = data.notesSubtitle),
                itemNote = TextLine(text = data.description)
            )
        )
    }
}