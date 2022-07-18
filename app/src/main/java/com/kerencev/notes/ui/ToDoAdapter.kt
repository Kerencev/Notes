package com.kerencev.notes.ui

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.kerencev.notes.R
import com.kerencev.notes.logic.Note

class ToDoAdapter: RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    internal interface OnNoteClicked {
        fun onNoteClicked(note: Note?)
        fun onLongNoteClicked(note: Note?)
    }

    private val onNoteClicked: OnNoteClicked? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.title)
        val date: TextView = itemView.findViewById(R.id.date)
        val radioButton: ImageView = itemView.findViewById(R.id.radio_button)
        val pin: ImageView = itemView.findViewById(R.id.pin)

        val card: MaterialCardView = itemView.findViewById(R.id.root)

//        init {
//            card.setOnClickListener(View.OnClickListener {
//                if (onNoteClicked != null) {
//                    val clickedPosition = adapterPosition
//                    onNoteClicked.onNoteClicked(data.get(clickedPosition))
//                }
//            })
//
//            card.setOnLongClickListener(View.OnLongClickListener {
//                if (onNoteClicked != null) {
//                    val clickedPosition = adapterPosition
//                    onNoteClicked.onLongNoteClicked(data.get(clickedPosition))
//                    true
//                }
//                false
//            })
//        }
    }
}
