package edu.vt.cs5254.dreamcatcher

import androidx.lifecycle.ViewModel

class DreamListViewModel : ViewModel() {

    val dreams = mutableListOf<Dream>()

    init {
        (0..99).forEach {
            val dream = Dream(
                title = "Dream #$it",
            )
            repeat(it % 4) { reflectionNum ->
                dream.entries += DreamEntry(
                    kind = DreamEntryKind.REFLECTION,
                    text = "Reflection $reflectionNum",
                    dreamId = dream.id
                )
            }
            if (it % 3 == 1) {
                dream.entries += DreamEntry(
                    kind = DreamEntryKind.DEFERRED,
                    dreamId = dream.id
                )
            }
            if (it % 3 == 2) {
                dream.entries += DreamEntry(
                    kind = DreamEntryKind.FULFILLED,
                    dreamId = dream.id
                )
            }
            dreams += dream
        }
    }
}