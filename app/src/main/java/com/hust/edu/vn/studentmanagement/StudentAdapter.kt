package com.hust.edu.vn.studentmanagement

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(private val studentListFragment: StudentListFragment,private val students: MutableList<Student>) :
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val code: TextView
        val name: TextView
        val email: TextView

        init {
            code = itemView.findViewById(R.id.code)
            name = itemView.findViewById(R.id.name)
            email = itemView.findViewById(R.id.email)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_item, parent, false)

        return StudentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return students.size
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.name.text = student.name
        holder.code.text = student.code
        holder.email.text = createEmailFromNameAndCode(student.name, student.code)
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("id", student._id)
            studentListFragment.findNavController().navigate(R.id.action_studentListFragment_to_updateStudentFragment, bundle)
        }
    }

    fun createEmailFromNameAndCode(fullName: String, code: String): String {
        val parts = fullName.split(" ")
        val firstName = parts.lastOrNull()?.lowercase() ?: ""
        val remainingParts = parts.dropLast(1)
        val firstChars = remainingParts.map { it.firstOrNull()?.lowercase() ?: "" }
        val concatenatedFirstChars = firstChars.joinToString("")

        val lastSixChars = code.takeLast(6)

        return "$firstName.$concatenatedFirstChars$lastSixChars@sis.hust.edu.vn"
    }
}