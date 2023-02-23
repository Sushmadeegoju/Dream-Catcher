package edu.vt.cs5254.dreamcatcher

import androidx.lifecycle.ViewModel

class DreamDetailViewModel : ViewModel() {

    var dream: Dream

    init {
        dream = Dream(title = "My First Dream")
        dream.entries += listOf(
            DreamEntry(
                kind = DreamEntryKind.REFLECTION,
                text = "Reflection One",
                dreamId = dream.id
            ),
            DreamEntry(
                kind = DreamEntryKind.REFLECTION,
                text = "Reflection Two",
                dreamId = dream.id
            ),
            DreamEntry(
                kind = DreamEntryKind.DEFERRED,
                dreamId = dream.id
            )
        )
    }
}