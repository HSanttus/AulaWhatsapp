package com.example.aulawhatsapp.abapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.aulawhatsapp.fragments.ContatoFragment
import com.example.aulawhatsapp.fragments.ConversasFragment

class ViewPagerAdapter(
    private val abas: List<String>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return abas.size // São o listOf(0-> "CONVERSAS",1-> "CONTATOS")
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            1 -> return ContatoFragment()
        }
        return ConversasFragment()
    }

}