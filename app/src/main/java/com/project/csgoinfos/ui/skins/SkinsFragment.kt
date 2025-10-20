package com.project.csgoinfos.ui.skins

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
import com.project.csgoinfos.R
import com.project.csgoinfos.databinding.FragmentListBinding
import com.project.csgoinfos.model.Skin
import com.project.csgoinfos.ui.filters.SkinFilters
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SkinsFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val vm: SkinsViewModel by viewModels()
    private lateinit var adapter: SkinAdapter
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = SkinAdapter { skin -> openDetails(skin) }
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter
        binding.recycler.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        binding.swipeRefresh.setOnRefreshListener { vm.load() }

        val searchBar: SearchBar = binding.searchBar
        val searchView: SearchView = binding.searchView
        searchBar.hint = getString(R.string.hint_search_skins)
        searchView.setupWithSearchBar(searchBar)
        searchView.editText.setText(vm.currentFilters.query ?: "")
        searchBar.setText(vm.currentFilters.query ?: "")
        searchBar.setOnClickListener { searchView.show() }
        searchView.editText.doOnTextChanged { text, _, _, _ ->
            val q = text?.toString()
            searchBar.setText(q)
            searchJob?.cancel()
            searchJob = viewLifecycleOwner.lifecycleScope.launch {
                delay(250)
                vm.applyFilters(vm.currentFilters.copy(query = q))
            }
        }
        searchView.editText.setOnEditorActionListener { _, _, _ ->
            searchView.hide()
            true
        }

        vm.filtered.observe(viewLifecycleOwner) { list ->
            adapter.submit(list)
            binding.empty.isVisible = list.isEmpty()
        }
        vm.loading.observe(viewLifecycleOwner) { binding.swipeRefresh.isRefreshing = it }
        vm.error.observe(viewLifecycleOwner) { it?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() } }
        vm.load()
        renderFilterChips()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_filter) {
            showFiltersBottomSheet()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun showFiltersBottomSheet() {
        val dlg = BottomSheetDialog(requireContext())
        val v = layoutInflater.inflate(R.layout.bottomsheet_filters_skins, null)
        dlg.setContentView(v)
        val cgRarity = v.findViewById<ChipGroup>(R.id.cgRarity)
        val cgCategory = v.findViewById<ChipGroup>(R.id.cgCategory)
        val cgWear = v.findViewById<ChipGroup>(R.id.cgWear)
        fun ChipGroup.populate(options: List<String>, pre: Set<String>) {
            removeAllViews()
            options.forEach { label ->
                val chip = Chip(context)
                chip.text = label
                chip.isCheckable = true
                chip.isChecked = pre.contains(label)
                addView(chip)
            }
        }
        cgRarity.populate(vm.availableRarities.value.orEmpty(), vm.currentFilters.rarities)
        cgCategory.populate(vm.availableCategories.value.orEmpty(), vm.currentFilters.categories)
        cgWear.populate(vm.availableWears.value.orEmpty(), vm.currentFilters.wears)
        v.findViewById<View>(R.id.btnClear).setOnClickListener {
            vm.clearFilters()
            renderFilterChips()
            dlg.dismiss()
        }
        v.findViewById<View>(R.id.btnApply).setOnClickListener {
            fun ChipGroup.selected(): Set<String> = checkedChipIds.mapNotNull { id -> findViewById<Chip>(id)?.text?.toString() }.toSet()
            vm.applyFilters(
                SkinFilters(
                    query = vm.currentFilters.query,
                    rarities = cgRarity.selected(),
                    categories = cgCategory.selected(),
                    wears = cgWear.selected()
                )
            )
            renderFilterChips()
            dlg.dismiss()
        }
        dlg.show()
    }

    private fun renderFilterChips() {
        val bar = binding.filtersBar
        val scroll = binding.filtersScroll
        bar.removeAllViews()
        val labels = buildList {
            addAll(vm.currentFilters.rarities.map { "Raridade: $it" })
            addAll(vm.currentFilters.categories.map { "Categoria: $it" })
            addAll(vm.currentFilters.wears.map { "Wear: $it" })
        }
        if (labels.isEmpty()) {
            scroll.isGone = true
            return
        }
        labels.forEach { text ->
            val chip = Chip(requireContext())
            chip.text = text
            chip.isCloseIconVisible = true
            chip.setOnCloseIconClickListener {
                val f = vm.currentFilters
                val new = when {
                    text.startsWith("Raridade: ") -> f.copy(rarities = f.rarities - text.removePrefix("Raridade: "))
                    text.startsWith("Categoria: ") -> f.copy(categories = f.categories - text.removePrefix("Categoria: "))
                    else -> f.copy(wears = f.wears - text.removePrefix("Wear: "))
                }
                vm.applyFilters(new)
                renderFilterChips()
            }
            bar.addView(chip)
        }
        scroll.isVisible = true
    }

    private fun openDetails(skin: Skin) {
        val i = Intent(requireContext(), SkinDetailActivity::class.java).apply {
            putExtra("id", skin.id)
            putExtra("name", skin.name)
            putExtra("desc", skin.description ?: "")
            putExtra("image", skin.image ?: "")
            putExtra("weapon", skin.weapon?.name ?: "")
            putExtra("rarity", skin.rarity?.name ?: "")
            putExtra("rarityColor", skin.rarity?.color ?: "#000000")
        }
        startActivity(i)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
