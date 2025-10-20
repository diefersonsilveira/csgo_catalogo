package com.project.csgoinfos.ui.skins

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.csgoinfos.data.CSGORepository
import com.project.csgoinfos.model.Skin
import com.project.csgoinfos.ui.filters.SkinFilters
import kotlinx.coroutines.launch

class SkinsViewModel(private val repo: CSGORepository = CSGORepository()) : ViewModel() {
    private val _all = MutableLiveData<List<Skin>>(emptyList())
    val all: LiveData<List<Skin>> = _all
    private val _filtered = MutableLiveData<List<Skin>>(emptyList())
    val filtered: LiveData<List<Skin>> = _filtered
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading
    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error
    private val _availableRarities = MutableLiveData<List<String>>(emptyList())
    val availableRarities: LiveData<List<String>> = _availableRarities
    private val _availableCategories = MutableLiveData<List<String>>(emptyList())
    val availableCategories: LiveData<List<String>> = _availableCategories
    private val _availableWears = MutableLiveData<List<String>>(emptyList())
    val availableWears: LiveData<List<String>> = _availableWears
    var currentFilters = SkinFilters()
        private set

    fun load() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val items = repo.fetchSkins()
                _all.value = items
                _filtered.value = items
                _availableRarities.value = items.mapNotNull { it.rarity?.name }.toSet().sorted()
                _availableCategories.value = items.mapNotNull { it.category?.name }.toSet().sorted()
                _availableWears.value = items.flatMap { it.wears?.mapNotNull { w -> w.name } ?: emptyList() }.toSet().sorted()
            } catch (e: Exception) {
                _error.value = e.message ?: "Erro"
            } finally {
                _loading.value = false
            }
        }
    }

    fun applyFilters(f: SkinFilters) {
        currentFilters = f
        val base = _all.value.orEmpty()
        val q = f.query?.trim()?.lowercase()
        _filtered.value = base.filter { s ->
            (q.isNullOrBlank() || s.name.lowercase().contains(q)) &&
                    (f.rarities.isEmpty() || s.rarity?.name in f.rarities) &&
                    (f.categories.isEmpty() || s.category?.name in f.categories) &&
                    (f.wears.isEmpty() || (s.wears?.any { it.name in f.wears } == true))
        }
    }

    fun clearFilters() = applyFilters(SkinFilters())
}
