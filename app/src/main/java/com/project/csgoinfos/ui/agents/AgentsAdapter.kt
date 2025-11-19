package com.project.csgoinfos.ui.agents

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
// A linha problemática foi removida daqui
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.csgoinfos.R
import com.project.csgoinfos.model.Agent

class AgentsAdapter(private val agents: List<Agent>) :
    RecyclerView.Adapter<AgentsAdapter.AgentViewHolder>() {

    class AgentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgAgent: ImageView = view.findViewById(R.id.imgAgent)
        val txtAgentName: TextView = view.findViewById(R.id.txtAgentName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_agent, parent, false)
        return AgentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AgentViewHolder, position: Int) {
        val agent = agents[position]
        holder.txtAgentName.text = agent.name

        // Agora o 'with' será reconhecido corretamente como parte do Glide
        Glide.with(holder.itemView.context)
            .load(agent.image)
            .into(holder.imgAgent)
    }

    override fun getItemCount() = agents.size
}
