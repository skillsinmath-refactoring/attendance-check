package com.refactoring_android.math_skill.studentList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.refactoring_android.math_skill.R
import com.refactoring_android.math_skill.data.response.Attendance
import com.refactoring_android.math_skill.databinding.ItemTop3Binding
import com.refactoring_android.math_skill.util.formatSecondsToTime

class Top3Adapter :
    ListAdapter<Attendance, Top3Adapter.ItemTop3ViewHolder>(diffCallback) {

    inner class ItemTop3ViewHolder(val binding: ItemTop3Binding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemTop3ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTop3Binding.inflate(inflater, parent, false)
        return ItemTop3ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ItemTop3ViewHolder,
        position: Int
    ) {
        with(holder) {
            binding.rankIv.setImageResource(
                when (position) {
                    0 -> R.drawable.rank1
                    1 -> R.drawable.rank2
                    2 -> R.drawable.rank3
                    else -> R.drawable.rank3
                }
            )
            val attendance = getItem(position)
            if(attendance.user?.name == null) {
                binding.studentNameTv.text = "${position + 1}등이 되어보세요!"
            } else
                binding.studentNameTv.text = "${position + 1}등 : ${attendance.user?.name} (${attendance.seconds?.formatSecondsToTime()})"
        }
    }

    override fun submitList(list: List<Attendance>?) {
        val newList = mutableListOf<Attendance>()

        val validList = list?.toMutableList() ?: mutableListOf()

        if (validList.size < 3) {
            val dummyCount = 3 - validList.size
            repeat(dummyCount) {
                validList.add(
                    Attendance(
                        id = -1,
                        userId = null,
                        name = null,
                        checkIn = null,
                        checkOut = null,
                        seconds = null,
                        user = null,
                        className = null,
                    )
                )
            }
        }
        // 최대 3개까지만 보여주기
        newList.addAll(validList.take(3))

        super.submitList(newList)
    }


    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Attendance>() {
            override fun areItemsTheSame(oldItem: Attendance, newItem: Attendance): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Attendance, newItem: Attendance): Boolean {
                return false
            }
        }
    }
}