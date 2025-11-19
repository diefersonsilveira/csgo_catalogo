package com.project.csgoinfos.ui.agents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.csgoinfos.R
import com.project.csgoinfos.data.RetrofitClient
import kotlinx.coroutines.launch

class AgentsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_agents, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewAgents)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBarAgents)

        // Configura o layout manager (obrigatório para RecyclerView funcionar)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Mostra o carregando
        progressBar.visibility = View.VISIBLE

        // Chama a API dentro de uma Coroutine
        lifecycleScope.launch {
            try {
                // CORREÇÃO AQUI: Geralmente o RetrofitClient é acessado via .api ou .instance
                // Se o seu RetrofitClient usa 'val api', use esta linha:
                val response = RetrofitClient.api("en").getAgents()

                if (response.isNotEmpty()) {
                    recyclerView.adapter = AgentsAdapter(response)
                } else {
                    Toast.makeText(context, "Nenhum agente encontrado", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Erro ao carregar: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                // Esconde o progress bar independentemente de erro ou sucesso
                progressBar.visibility = View.GONE
            }
        }
    }
}
