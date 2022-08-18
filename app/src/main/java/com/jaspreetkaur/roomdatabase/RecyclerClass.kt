package com.jaspreetkaur.roomdatabase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerClass(var notes : ArrayList<Notes>,var recyclerClickInterface: RecyclerClickInterface):RecyclerView.Adapter<RecyclerClass.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerClass.ViewHolder{
        var view = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_notes, parent, false)
        return ViewHolder(view)
    }
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        var title = view.findViewById<TextView>(R.id.tvTitle)
        var description= view.findViewById<TextView>(R.id.tvDescription)

    }


    override fun onBindViewHolder(holder: RecyclerClass.ViewHolder, position: Int) {
        holder.title.setText(notes[position].title)
        holder.description.setText(notes[position].description)
        holder.itemView.setOnClickListener{
            recyclerClickInterface.notesClicked(notes[position])
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }
}