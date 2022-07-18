package com.kerencev.notes.ui.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.kerencev.notes.AppState
import com.kerencev.notes.databinding.FragmentToDoListBinding
import com.kerencev.notes.logic.memory.todo.TodoEntity
import com.kerencev.notes.ui.ToolbarHolder
import org.koin.androidx.viewmodel.ext.android.viewModel

class ToDoListFragment : Fragment() {

    private val viewModel: TodoViewModel by viewModel()
    private var _binding: FragmentToDoListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ToDoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentToDoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (requireActivity() is ToolbarHolder) {
            (requireActivity() as ToolbarHolder).setToolbar(binding.toolBar)
        }
        adapter = ToDoAdapter()
        binding.recyclerView.adapter = adapter
        val observer = Observer<AppState> { renderData(it) }
        viewModel.data.observe(viewLifecycleOwner, observer)
        viewModel.getAllTodo()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success<*> -> {
                adapter.setData(appState.data as List<TodoEntity>)
            }
            is AppState.Loading -> {

            }
            is AppState.Error -> {

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
