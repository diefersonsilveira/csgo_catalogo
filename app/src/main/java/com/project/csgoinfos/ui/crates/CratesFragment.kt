package com.project.csgoinfos.ui.crates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.project.csgoinfos.R
import com.project.csgoinfos.databinding.FragmentListBinding
import com.project.csgoinfos.ui.filters.CrateFilters

class CratesFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val vm: CratesViewModel by viewModels()
    private lateinit var adapter: CrateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = CrateAdapter()
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter
        binding.recycler.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        binding.swipeRefresh.setOnRefreshListener { vm.load() }

        binding.searchEditText.hint = getString(R.string.hint_search_crates)
        binding.searchEditText.setText(vm.currentFilters.query ?: "")
        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            val q = text?.toString()
            vm.applyFilters(vm.currentFilters.copy(query = q))
        }

        vm.filtered.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.empty.isVisible = list.isEmpty()
        }
        vm.loading.observe(viewLifecycleOwner) { binding.swipeRefresh.isRefreshing = it }

        vm.load()
        renderFilterChips()
    }

    private fun renderFilterChips() {
        val bar = binding.filtersBar
        val scroll = binding.filtersScroll
        bar.removeAllViews()
        val labels = buildList {
            addAll(vm.currentFilters.rarities.map { "Raridade: $it" })
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
                val new = f.copy(rarities = f.rarities - text.removePrefix("Raridade: "))
                vm.applyFilters(new)
                renderFilterChips()
            }
            bar.addView(chip)
        }
        scroll.isVisible = true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_filter) {
            showFiltersBottomSheet()
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun showFiltersBottomSheet() {
        val dlg = BottomSheetDialog(requireContext())
        val v = layoutInflater.inflate(R.layout.bottomsheet_filters_stickers, null)
        // Reutilizamos o layout de stickers para um único grupo genérico
        dlg.setContentView(v)
        val cgRarity = v.findViewById<ChipGroup>(R.id.cgRarity)
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
        v.findViewById<View>(R.id.btnClear).setOnClickListener {
            vm.clearFilters()
            renderFilterChips()
            dlg.dismiss()
        }
        v.findViewById<View>(R.id.btnApply).setOnClickListener {
            fun ChipGroup.selected(): Set<String> = checkedChipIds.mapNotNull { id -> findViewById<Chip>(id)?.text?.toString() }.toSet()
            vm.applyFilters(
                CrateFilters(
                    query = vm.currentFilters.query,
                    rarities = cgRarity.selected()
                )
            )
            renderFilterChips()
            dlg.dismiss()
        }
        dlg.show()
    }
}
