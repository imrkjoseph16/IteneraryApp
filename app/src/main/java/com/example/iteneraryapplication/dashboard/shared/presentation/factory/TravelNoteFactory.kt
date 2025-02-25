package com.example.iteneraryapplication.dashboard.shared.presentation.factory

import com.example.iteneraryapplication.R
import com.example.iteneraryapplication.app.shared.component.TextLine
import com.example.iteneraryapplication.app.shared.dto.data.NoteListItem
import com.example.iteneraryapplication.app.shared.dto.layout.NoteItemViewDto
import com.example.iteneraryapplication.app.shared.dto.layout.SpaceItemViewDto
import com.example.iteneraryapplication.app.util.DateUtil.Companion.convertDateFormat
import com.example.iteneraryapplication.app.util.DateUtil.Companion.convertStringDateToCalendar
import com.example.iteneraryapplication.app.util.Default.Companion.DATE_AND_TIME_NAMED
import com.example.iteneraryapplication.dashboard.shared.domain.data.Notes
import com.example.iteneraryapplication.dashboard.shared.presentation.DashboardState
import com.example.iteneraryapplication.dashboard.shared.presentation.GetNotesTypeData
import javax.inject.Inject

class TravelNoteFactory @Inject constructor() {

    fun createOverview(
        state: DashboardState
    ) = when(state) {
        is GetNotesTypeData -> state.listNotes
            ?.toMutableList()
            ?.removeDuplicateNotes()
            ?.sortListByPinnedAndDate()
            ?.let { prepareList(it) } ?: emptyList()
        else -> listOf(SpaceItemViewDto(R.dimen.grid_0))
    }

    /**
     * (prepareList) creating the layout component.
     * */
    private fun prepareList(notes: List<Notes>) = setupNotesDetails(notes)

    private fun setupNotesDetails(notes: List<Notes>)= notes.mapIndexed { _, data ->
        NoteListItem(
            dto = NoteItemViewDto(
                itemId = data.itemId.orEmpty(),
                itemTitle = TextLine(text = data.notesTitle),
                itemSubtitle = TextLine(text = data.notesSubtitle),
                itemNoteColor = data.notesColor,
                itemNoteImage = data.notesImage,
                itemNoteWebLink = data.notesWebLink,
                itemNote = TextLine(text = data.notesDesc),
                itemListOfExpenses = data.listOfExpenses,
                canPinnedNote = data.canPinnedNote,
                itemDateSaved = TextLine(
                    text = convertDateFormat(
                        dateValue = data.notesDateSaved.orEmpty(),
                        newDateFormat = DATE_AND_TIME_NAMED
                    )
                ),
            )
        )
    }

    private fun MutableList<Notes>.removeDuplicateNotes() : MutableList<Notes> {
        val hashSet = HashSet<Notes>()
        hashSet.addAll(this)
        this.clear()
        this.addAll(hashSet)

        return this
    }

    private fun List<Notes>.sortListByPinnedAndDate() = sortedWith(
        // These will be sorted by top pinned notes,
        // and based on the scheduled date.
        compareBy(
            Notes::canPinnedNote,
            { it.notesDateSaved?.let { date ->
                convertStringDateToCalendar(
                    dateValue = date,
                    currentDateFormat = DATE_AND_TIME_NAMED
                ).time }
            }
        )
    ).reversed().toMutableList()
}