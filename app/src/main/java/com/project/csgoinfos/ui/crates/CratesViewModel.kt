package com.project.csgoinfos.ui.crates

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.csgoinfos.data.CSGORepository
import com.project.csgoinfos.model.Crate
import com.project.csgoinfos.ui.filters.CrateFilters
import kotlinx.coroutines.launch

class CratesViewModel(private val repo: CSGORepository = CSGORepository()) : ViewModel() {
    private val _all = MutableLiveData<List<Crate>>(emptyList())
    val all: LiveData<List<Crate>> = _all
    private val _filtered = MutableLiveData<List<Crate>>(emptyList())
    val filtered: LiveData<List<Crate>> = _filtered
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading
    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error
    private val _availableRarities = MutableLiveData<List<String>>(emptyList())
    val availableRarities: LiveData<List<String>> = _availableRarities
    var currentFilters = CrateFilters()
        private set

    fun load() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val items = repo.fetchCrates()
                _all.value = items
                _filtered.value = items
                _availableRarities.value = items.mapNotNull { it.rarity?.name }.toSet().sorted()
            } catch (e: Exception) {
                _error.value = e.message ?: "Erro"
            } finally {
                _loading.value = false
            }
        }
    }

    fun applyFilters(f: CrateFilters) {
        currentFilters = f
        val base = _all.value.orEmpty()
        val q = f.query?.trim()?.lowercase()
        _filtered.value = base.filter { c ->
            (q.isNullOrBlank() || (c.name ?: "").lowercase().contains(q)) &&
                    (f.rarities.isEmpty() || c.rarity?.name in f.rarities)
        }
    }

    fun clearFilters() = applyFilters(CrateFilters())
}


