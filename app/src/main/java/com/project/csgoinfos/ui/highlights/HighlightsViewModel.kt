package com.project.csgoinfos.ui.highlights

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.csgoinfos.data.CSGORepository
import com.project.csgoinfos.model.Highlight
import com.project.csgoinfos.ui.filters.HighlightFilters
import kotlinx.coroutines.launch

class HighlightsViewModel(private val repo: CSGORepository = CSGORepository()) : ViewModel() {
    private val _all = MutableLiveData<List<Highlight>>(emptyList())
    val all: LiveData<List<Highlight>> = _all
    private val _filtered = MutableLiveData<List<Highlight>>(emptyList())
    val filtered: LiveData<List<Highlight>> = _filtered
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading
    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error
    private val _availablePlayers = MutableLiveData<List<String>>(emptyList())
    val availablePlayers: LiveData<List<String>> = _availablePlayers
    private val _availableTeams = MutableLiveData<List<String>>(emptyList())
    val availableTeams: LiveData<List<String>> = _availableTeams
    private val _availableEvents = MutableLiveData<List<String>>(emptyList())
    val availableEvents: LiveData<List<String>> = _availableEvents
    private val _availableMaps = MutableLiveData<List<String>>(emptyList())
    val availableMaps: LiveData<List<String>> = _availableMaps
    var currentFilters = HighlightFilters()
        private set

    fun load() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val items = repo.fetchHighlights()
                _all.value = items
                _filtered.value = items
                _availablePlayers.value = items.mapNotNull { it.player }.toSet().sorted()
                _availableTeams.value = items.mapNotNull { it.team }.toSet().sorted()
                _availableEvents.value = items.mapNotNull { it.event }.toSet().sorted()
                _availableMaps.value = items.mapNotNull { it.map }.toSet().sorted()
            } catch (e: Exception) {
                _error.value = e.message ?: "Erro"
            } finally {
                _loading.value = false
            }
        }
    }

    fun applyFilters(f: HighlightFilters) {
        currentFilters = f
        val base = _all.value.orEmpty()
        val q = f.query?.trim()?.lowercase()
        _filtered.value = base.filter { h ->
            (q.isNullOrBlank() || (h.title ?: "").lowercase().contains(q) || (h.player ?: "").lowercase().contains(q)) &&
                    (f.players.isEmpty() || h.player in f.players) &&
                    (f.teams.isEmpty() || h.team in f.teams) &&
                    (f.events.isEmpty() || h.event in f.events) &&
                    (f.maps.isEmpty() || h.map in f.maps)
        }
    }

    fun clearFilters() = applyFilters(HighlightFilters())
}


