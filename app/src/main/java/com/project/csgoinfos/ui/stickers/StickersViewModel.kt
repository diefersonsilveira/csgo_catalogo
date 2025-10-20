package com.project.csgoinfos.ui.stickers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.csgoinfos.data.CSGORepository
import com.project.csgoinfos.model.Sticker
import com.project.csgoinfos.ui.filters.StickerFilters
import kotlinx.coroutines.launch

class StickersViewModel(private val repo: CSGORepository = CSGORepository()) : ViewModel() {
    private val _all = MutableLiveData<List<Sticker>>(emptyList())
    val all: LiveData<List<Sticker>> = _all
    private val _filtered = MutableLiveData<List<Sticker>>(emptyList())
    val filtered: LiveData<List<Sticker>> = _filtered
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading
    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error
    private val _availableRarities = MutableLiveData<List<String>>(emptyList())
    val availableRarities: LiveData<List<String>> = _availableRarities
    private val _availableTypes = MutableLiveData<List<String>>(emptyList())
    val availableTypes: LiveData<List<String>> = _availableTypes
    private val _availableEvents = MutableLiveData<List<String>>(emptyList())
    val availableEvents: LiveData<List<String>> = _availableEvents
    var currentFilters = StickerFilters()
        private set

    fun load() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val items = repo.fetchStickers()
                _all.value = items
                _filtered.value = items
                _availableRarities.value = items.mapNotNull { it.rarity?.name }.toSet().sorted()
                _availableTypes.value = items.mapNotNull { it.type }.toSet().sorted()
                _availableEvents.value = items.mapNotNull { it.tournamentEvent }.toSet().sorted()
            } catch (e: Exception) {
                _error.value = e.message ?: "Erro"
            } finally {
                _loading.value = false
            }
        }
    }

    fun applyFilters(f: StickerFilters) {
        currentFilters = f
        val base = _all.value.orEmpty()
        val q = f.query?.trim()?.lowercase()
        _filtered.value = base.filter { s ->
            (q.isNullOrBlank() || s.name.lowercase().contains(q)) &&
                    (f.rarities.isEmpty() || s.rarity?.name in f.rarities) &&
                    (f.types.isEmpty() || s.type in f.types) &&
                    (f.events.isEmpty() || s.tournamentEvent in f.events)
        }
    }

    fun clearFilters() = applyFilters(StickerFilters())
}
