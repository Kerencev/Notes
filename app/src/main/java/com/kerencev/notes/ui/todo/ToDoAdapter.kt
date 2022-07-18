package com.kerencev.notes.ui.todo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kerencev.notes.databinding.ItemToDoRecyclerBinding
import com.kerencev.notes.logic.memory.todo.TodoEntity

class ToDoAdapter : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    private var data: List<TodoEntity> = listOf()

    fun setData(newData: List<TodoEntity>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding = ItemToDoRecyclerBinding.inflate(LayoutInflater.from(parent.context))
        return ToDoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ToDoViewHolder(private val binding: ItemToDoRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: TodoEntity) = with(binding) {
            tvTodo.text = data.message
            tvDate.text = data.myDate
        }
    }
}
