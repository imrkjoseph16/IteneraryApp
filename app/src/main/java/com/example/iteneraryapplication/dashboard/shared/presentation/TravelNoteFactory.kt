package com.example.iteneraryapplication.dashboard.shared.presentation

import com.example.iteneraryapplication.R
import com.example.iteneraryapplication.app.shared.component.TextLine
import com.example.iteneraryapplication.app.shared.dto.data.NoteListItem
import com.example.iteneraryapplication.app.shared.dto.layout.NoteItemViewDto
import com.example.iteneraryapplication.app.shared.dto.layout.SpaceItemViewDto
import com.example.iteneraryapplication.app.util.DateUtil
import com.example.iteneraryapplication.app.util.Default.Companion.DATE_NAMED
import com.example.iteneraryapplication.dashboard.shared.domain.data.Notes
import javax.inject.Inject

class TravelNoteFactory @Inject constructor(
    private val dateUtil: DateUtil
) {

    fun createOverview(state: DashboardState) = when(state) {
        is GetNotesTypeData -> state.listNotes
            ?.toMutableList()
            ?.checkDuplicateNotes()
            ?.let { prepareList(it) } ?: emptyList()
        else -> listOf(SpaceItemViewDto(R.dimen.grid_0))
    }

    private fun MutableList<Notes>.checkDuplicateNotes() : List<Notes> {
        val hashSet = HashSet<Notes>()
        hashSet.addAll(this)
        this.clear()
        this.addAll(hashSet)

        return this.toList()
    }

    /**
     * (prepareList) creating the layout component.
     * */
    private fun prepareList(notes: List<Notes>) = notes.mapIndexed { _, data ->
        NoteListItem(
            dto = NoteItemViewDto(
                itemId = data.itemId.orEmpty(),
                itemTitle = TextLine(text = data.notesTitle),
                itemSubtitle = TextLine(text = data.notesSubtitle),
                itemNoteColor = data.notesColor,
                itemNoteImage = data.notesImage,
                itemNoteWebLink = data.notesWebLink,
                itemNote = TextLine(text = data.notesDesc),
                itemDateSaved = TextLine(text = dateUtil.convertDateFormat(
                    dateValue = data.notesDateSaved,
                    newDateFormat = DATE_NAMED)
                ),
            )
        )
    }
}