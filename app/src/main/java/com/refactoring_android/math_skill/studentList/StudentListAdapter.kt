package com.refactoring_android.math_skill.studentList

import android.os.Build
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.refactoring_android.math_skill.data.response.Attendance
import com.refactoring_android.math_skill.databinding.ItemStudentBinding
import com.refactoring_android.math_skill.util.formatSecondsToTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.Duration
import java.time.LocalTime

class StudentListAdapter :
    ListAdapter<Attendance, StudentListAdapter.ItemStudentViewHolder>(diffCallback) {

    inner class ItemStudentViewHolder(val binding: ItemStudentBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemStudentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemStudentBinding.inflate(inflater, parent, false)
        return ItemStudentViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(
        holder: ItemStudentViewHolder,
        position: Int
    ) {
        with(holder) {
            binding.speechTv?.apply {
                alpha = 0f
                visibility = GONE
            }

            val attendance = getItem(position)
            binding.studentNameTv.text = attendance.user?.name + "\n${attendance.user?.checkInId}"
            val checkInTime = attendance.checkIn?.substringAfter(" ") ?: ""
            val checkOutTime = attendance.checkOut?.substringAfter(" ") ?: ""
            binding.checkInTv.text = checkInTime
            binding.checkOutTv.text = checkOutTime
            val transaction = attendance.user?.transactions?.lastOrNull()
            binding.speechTv?.text = transaction?.amount?.toString()?.plus("P") ?: ""
            binding.classNameTv?.text = attendance.className?.className?: "자습"
           // binding.speechTv?.text = attendance.user?.transactions?.get(0)?.amount.toString() +"P"

            binding.studentNameTv.setOnClickListener {
                binding.speechTv?.apply {
                    alpha = 0f
                    visibility = VISIBLE

                    // 1초 동안 서서히 나타남
                    animate()
                        .alpha(1f)
                        .setDuration(300)
                        .withEndAction {
                            // 1초 유지 후
                            postDelayed({
                                // 1초 동안 서서히 사라짐
                                animate()
                                    .alpha(0f)
                                    .setDuration(1000)
                                    .withEndAction {
                                        visibility = GONE
                                    }
                            }, 1500)
                        }
                }
            }
            binding.pointIv?.setOnClickListener {
                binding.speechTv?.apply {
                    alpha = 0f
                    visibility = VISIBLE

                    // 1초 동안 서서히 나타남
                    animate()
                        .alpha(1f)
                        .setDuration(300)
                        .withEndAction {
                            // 1초 유지 후
                            postDelayed({
                                // 1초 동안 서서히 사라짐
                                animate()
                                    .alpha(0f)
                                    .setDuration(1000)
                                    .withEndAction {
                                        visibility = GONE
                                    }
                            }, 1500)
                        }
                }
            }
            binding.studyTimeTv.text =
                if (attendance.seconds == 0) null else attendance.seconds?.formatSecondsToTime()
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Attendance>() {
            override fun areItemsTheSame(oldItem: Attendance, newItem: Attendance): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Attendance, newItem: Attendance): Boolean {
                return oldItem.checkOut == newItem.checkOut
                        && oldItem.checkIn == newItem.checkIn
                        && oldItem.seconds == newItem.seconds
            }
        }
    }
}
