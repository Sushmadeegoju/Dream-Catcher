package edu.vt.cs5254.dreamcatcher

import java.util.Date
import java.util.UUID

data class Dream(
    val id: UUID = UUID.randomUUID(),
    val title: String = "",
    val lastUpdated: Date = Date(),
) {
    var entries = listOf(DreamEntry(kind = DreamEntryKind.CONCEIVED, dreamId = id))
    val isFulfilled get() = entries.any { it.kind == DreamEntryKind.FULFILLED }
    val isDeferred get() = entries.any { it.kind == DreamEntryKind.DEFERRED }
}

data class DreamEntry(
    val id: UUID = UUID.randomUUID(),
    val text: String = "",
    val kind: DreamEntryKind,
    val dreamId: UUID
)

enum class DreamEntryKind {
    CONCEIVED,
    DEFERRED,
    FULFILLED,
    REFLECTION
}