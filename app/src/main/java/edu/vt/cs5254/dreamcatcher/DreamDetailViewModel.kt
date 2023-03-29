package edu.vt.cs5254.dreamcatcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

class DreamDetailViewModel(dreamId: UUID) : ViewModel() {

    fun handleDeferredClick() {
        updateDream { oldDream ->
            oldDream.copy().apply {
                entries =
                    if(oldDream.isDeferred) {
                        oldDream.entries.filterNot { it.kind == DreamEntryKind.DEFERRED }
                    } else {
                        oldDream.entries + DreamEntry(
                            kind = DreamEntryKind.DEFERRED,
                            dreamId = oldDream.id
                        )
                    }
            }

        }
    }

    fun handleFulfilledClick() {
        updateDream { oldDream ->
            oldDream.copy().apply {
                entries =
                    if(oldDream.isFulfilled) {
                        oldDream.entries.filterNot { it.kind == DreamEntryKind.FULFILLED }
                    } else {
                        oldDream.entries + DreamEntry(
                            kind = DreamEntryKind.FULFILLED,
                            dreamId = oldDream.id
                        )
                    }
            }
        }
    }
    fun updateDream(onUpdateDream: (Dream) -> Dream) {
        _dream.update { oldDream ->
            val newDream = oldDream?.let { onUpdateDream(it) }  ?: return
            if(newDream == oldDream && newDream.entries == oldDream.entries) {
                return
            }
            newDream.copy(lastUpdated = Date()). apply { entries = newDream.entries }
        }
    }

    private val _dream: MutableStateFlow<Dream?> = MutableStateFlow(null)
    val dream = _dream.asStateFlow()

    init {
        viewModelScope.launch {
            _dream.value = DreamRepository.get().getDream(dreamId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        _dream.value?.let { DreamRepository.get().updateDream(it) }
    }

}

class DreamDetailViewModelFactory(private val dreamId: UUID) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DreamDetailViewModel(dreamId) as T
    }
}