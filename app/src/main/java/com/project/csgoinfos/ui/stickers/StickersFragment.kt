package com.project.csgoinfos.ui.stickers

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.toColorInt
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
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.project.csgoinfos.R
import com.project.csgoinfos.databinding.FragmentListBinding
import com.project.csgoinfos.model.Sticker
import com.project.csgoinfos.ui.filters.StickerFilters
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StickersFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val vm: StickersViewModel by viewModels()
    private lateinit var adapter: StickerAdapter
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

        adapter = StickerAdapter({ s -> openDetails(s) }, { s -> showStickerInfo(s) })
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter
        binding.recycler.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        binding.swipeRefresh.setOnRefreshListener { vm.load() }

        binding.searchInputLayout.hint = getString(R.string.hint_search_stickers)
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
        vm.error.observe(viewLifecycleOwner) { it?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() } }

        vm.load()
        renderFilterChips()
    }

    private fun showFiltersBottomSheet() {
        val dlg = BottomSheetDialog(requireContext())
        val v = layoutInflater.inflate(R.layout.bottomsheet_filters_stickers, binding.root as? ViewGroup, false)
        dlg.setContentView(v)
        val cgRarity = v.findViewById<ChipGroup>(R.id.cgRarity)
        val cgType = v.findViewById<ChipGroup>(R.id.cgType)
        val cgEvent = v.findViewById<ChipGroup>(R.id.cgEvent)
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
        cgType.populate(vm.availableTypes.value.orEmpty(), vm.currentFilters.types)
        cgEvent.populate(vm.availableEvents.value.orEmpty(), vm.currentFilters.events)
        v.findViewById<View>(R.id.btnClear).setOnClickListener {
            vm.clearFilters()
            renderFilterChips()
            dlg.dismiss()
        }
        v.findViewById<View>(R.id.btnApply).setOnClickListener {
            fun ChipGroup.selected(): Set<String> = checkedChipIds.mapNotNull { id -> findViewById<Chip>(id)?.text?.toString() }.toSet()
            vm.applyFilters(
                StickerFilters(
                    query = vm.currentFilters.query,
                    rarities = cgRarity.selected(),
                    types = cgType.selected(),
                    events = cgEvent.selected()
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
            addAll(vm.currentFilters.types.map { "Tipo: $it" })
            addAll(vm.currentFilters.events.map { "Evento: $it" })
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
                    text.startsWith("Tipo: ") -> f.copy(types = f.types - text.removePrefix("Tipo: "))
                    else -> f.copy(events = f.events - text.removePrefix("Evento: "))
                }
                vm.applyFilters(new)
                renderFilterChips()
            }
            bar.addView(chip)
        }
        scroll.isVisible = true
    }

    private fun showStickerInfo(s: Sticker) {
        val dlg = BottomSheetDialog(requireContext())
        val v = layoutInflater.inflate(R.layout.bottomsheet_info_sticker, binding.root as? ViewGroup, false)
        v.findViewById<ImageView>(R.id.image).load(s.image)
        v.findViewById<TextView>(R.id.title).text = s.name
        val chip = v.findViewById<Chip>(R.id.badge)
        chip.text = s.rarity?.name ?: ""
        val color = s.rarity?.color?.let { runCatching { it.toColorInt() }.getOrNull() }
            ?: requireContext().getColor(R.color.brand_primary)
        chip.chipBackgroundColor = android.content.res.ColorStateList.valueOf(color)
        chip.setTextColor(if (androidx.core.graphics.ColorUtils.calculateLuminance(color) < 0.5) android.graphics.Color.WHITE else android.graphics.Color.BLACK)
        v.findViewById<TextView>(R.id.desc).text = s.description ?: ""
        v.findViewById<View>(R.id.btnDetails).setOnClickListener { openDetails(s); dlg.dismiss() }
        v.findViewById<View>(R.id.btnClose).setOnClickListener { dlg.dismiss() }
        dlg.setContentView(v)
        dlg.show()
    }

    private fun openDetails(s: Sticker) {
        val i = Intent(requireContext(), StickerDetailActivity::class.java).apply {
            putExtra("id", s.id)
            putExtra("name", s.name)
            putExtra("desc", s.description ?: "")
            putExtra("image", s.image ?: "")
            putExtra("rarity", s.rarity?.name ?: "")
            putExtra("rarityColor", s.rarity?.color ?: "#000000")
            putExtra("event", s.tournamentEvent ?: "")
            putExtra("team", s.tournamentTeam ?: "")
            putExtra("type", s.type ?: "")
            putExtra("effect", s.effect ?: "")
        }
        startActivity(i)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
