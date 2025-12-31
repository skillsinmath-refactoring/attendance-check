package com.refactoring_android.math_skill

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.refactoring_android.math_skill.data.response.ClassInfo
import com.refactoring_android.math_skill.databinding.ItemClassChoiceBinding
import java.time.LocalTime
import java.time.format.DateTimeFormatter
@SuppressLint("NewApi", "NotifyDataSetChanged")
class ClassSelectAdapter(
    items: List<ClassInfo>,
    private val onItemSelected: (ClassInfo) -> Unit
) : RecyclerView.Adapter<ClassSelectAdapter.ClassViewHolder>() {

    private val itemList = items.toMutableList()
    private var selectedItemId: Int? = null
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")


    init {
        setupDefaultSelection()
        notifyDataSetChanged()
    }

    private fun setupDefaultSelection() {
        val selfStudy = ClassInfo(
            id = -1, // null 말고 명시적 id 주자
            name = "자습시간",
            startTime = "00:00",
            endTime = "00:30",
            day = null,
            note = null
        )
        itemList.add(selfStudy)

        val now = LocalTime.now()

        for (item in itemList) {
            try {
                val start = LocalTime.parse(item.startTime, timeFormatter).minusMinutes(40)
                val end = LocalTime.parse(item.endTime, timeFormatter).minusMinutes(40)

                if (now.isAfter(start) && now.isBefore(end)) {
                    selectedItemId = item.id
                }
            } catch (e: Exception) {
                // 시간 파싱 실패 시 무시
            }
        }

        if (selectedItemId == null) {
            selectedItemId = selfStudy.id
        }

        notifyDataSetChanged()
    }

    inner class ClassViewHolder(val binding: ItemClassChoiceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ClassInfo) {
            binding.tvClassName.text = item.name
           if(item.id == selectedItemId) {
               binding.tvClassName.setTextColor(
                   binding.root.context.getColor(R.color.black)
               )
               binding.classContainer.backgroundTintList =
                   binding.root.context.getColorStateList(R.color.red3)
            } else {
               binding.tvClassName.setTextColor(
                   binding.root.context.getColor(R.color.gray)
               )
               binding.classContainer.backgroundTintList =
                   binding.root.context.getColorStateList(R.color.white)
            }


            binding.root.setOnClickListener {
                selectedItemId = item.id
                notifyDataSetChanged()
                onItemSelected(item)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemClassChoiceBinding.inflate(inflater, parent, false)
        return ClassViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    fun getSelectedClass(): ClassInfo? {
        return itemList.find { it.id == selectedItemId }
    }
}
