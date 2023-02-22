package com.example.a7minutesworkout

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.a7minutesworkout.databinding.ItemExerciseViewBinding

class ExerciseStatusAdapter(private val list: ArrayList<ExerciseModel>) :
  RecyclerView.Adapter<ExerciseStatusAdapter.ViewHolder>() {

  class ViewHolder(binding: ItemExerciseViewBinding) : RecyclerView.ViewHolder(binding.root) {
    val item: TextView = binding.exerciseStatusText
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
      ItemExerciseViewBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
      ),
    )
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val model = list[position]
    holder.item.text = model.getId().toString()
    when {
      model.getIsSelected() -> {
        holder.item.background =
          ContextCompat.getDrawable(holder.item.context, R.drawable.item_circular_status_border)
        holder.item.setTextColor(Color.parseColor("#212121"))

      }
      model.getIsCompleted() -> {
        holder.item.background =
          ContextCompat.getDrawable(holder.item.context, R.drawable.item_circular_full)
        holder.item.setTextColor(Color.parseColor("#FFFFFF"))
      }
    }
  }

  override fun getItemCount(): Int {
    return list.size
  }

}