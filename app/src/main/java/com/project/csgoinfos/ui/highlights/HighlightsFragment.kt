package com.project.csgoinfos.ui.highlights

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.project.csgoinfos.R
import com.project.csgoinfos.databinding.FragmentListBinding
import com.project.csgoinfos.model.Map
import com.project.csgoinfos.ui.filters.HighlightFilters
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HighlightsFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val vm: HighlightsViewModel by viewModels()
    private lateinit var adapter: HighlightAdapter
    private var searchJob: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_list, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.action_filter) {
                    showFiltersBottomSheet()
                    return true
                }
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        adapter = HighlightAdapter { h ->
            val i = Intent(requireContext(), com.project.csgoinfos.ui.highlights.HighlightDetailActivity::class.java)
            i.putExtra("id", h.id)
            i.putExtra("title", h.title ?: h.player ?: "Highlight")
            i.putExtra("player", h.player)
            i.putExtra("team", h.team)
            i.putExtra("event", h.event)
            i.putExtra("map", h.map)
            i.putExtra("video", h.video)
            startActivity(i)
        }
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter
        binding.recycler.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        binding.swipeRefresh.setOnRefreshListener { vm.load() }

        binding.searchEditText.hint = getString(R.string.hint_search_highlights)
        binding.searchEditText.setText(vm.currentFilters.query ?: "")
        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            val q = text?.toString()
            searchJob?.cancel()
            searchJob = viewLifecycleOwner.lifecycleScope.launch {
                delay(250)
                vm.applyFilters(vm.currentFilters.copy(query = q))
            }
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
            addAll(vm.currentFilters.players.map { "Jogador: $it" })
            addAll(vm.currentFilters.teams.map { "Time: $it" })
            addAll(vm.currentFilters.events.map { "Evento: $it" })
            addAll(vm.currentFilters.maps.map { "Mapa: ${Map.getMapName(it)}" })
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
                    text.startsWith("Jogador: ") -> f.copy(players = f.players - text.removePrefix("Jogador: "))
                    text.startsWith("Time: ") -> f.copy(teams = f.teams - text.removePrefix("Time: "))
                    text.startsWith("Evento: ") -> f.copy(events = f.events - text.removePrefix("Evento: "))
                    else -> f.copy(maps = f.maps - text.removePrefix("Mapa: "))
                }
                vm.applyFilters(new)
                renderFilterChips()
            }
            bar.addView(chip)
        }
        scroll.isVisible = true
    }

    private fun showFiltersBottomSheet() {
        val dlg = BottomSheetDialog(requireContext())
        val v = layoutInflater.inflate(R.layout.bottomsheet_filters_highlights, binding.root as? ViewGroup, false)
        dlg.setContentView(v)
        val cgPlayers = v.findViewById<ChipGroup>(R.id.cgPlayers)
        val cgTeams = v.findViewById<ChipGroup>(R.id.cgTeams)
        val cgEvents = v.findViewById<ChipGroup>(R.id.cgEvents)
        val cgMaps = v.findViewById<ChipGroup>(R.id.cgMaps)
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
        cgPlayers.populate(vm.availablePlayers.value.orEmpty(), vm.currentFilters.players)
        cgTeams.populate(vm.availableTeams.value.orEmpty(), vm.currentFilters.teams)
        cgEvents.populate(vm.availableEvents.value.orEmpty(), vm.currentFilters.events)
        cgMaps.populate(vm.availableMaps.value.orEmpty().map { Map.getMapName(it) }, vm.currentFilters.maps.map { Map.getMapName(it) }.toSet())

        v.findViewById<View>(R.id.btnClear).setOnClickListener {
            vm.clearFilters()
            renderFilterChips()
            dlg.dismiss()
        }
        v.findViewById<View>(R.id.btnApply).setOnClickListener {
            fun ChipGroup.selected(): Set<String> = checkedChipIds.mapNotNull { id -> findViewById<Chip>(id)?.text?.toString() }.toSet()
            vm.applyFilters(
                HighlightFilters(
                    query = vm.currentFilters.query,
                    players = cgPlayers.selected(),
                    teams = cgTeams.selected(),
                    events = cgEvents.selected(),
                    maps = cgMaps.selected()
                )
            )
            renderFilterChips()
            dlg.dismiss()
        }
        dlg.show()
    }
}
