package com.kerencev.notes.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kerencev.notes.R

class ToDoListFragment : Fragment(R.layout.fragment_to_do_list) {

    lateinit var toolBar: Toolbar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialize(view)

        if (requireActivity() is ToolbarHolder) {
            (requireActivity() as ToolbarHolder).setToolbar(toolBar)
        }

        initList(view)
    }

    private fun initialize(view: View) {
        toolBar = view.findViewById(R.id.bar_main_add)
    }

    private fun initList(view: View) {

        val recyclerView: RecyclerView = view.findViewById(R.id.container)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager



    }
}
