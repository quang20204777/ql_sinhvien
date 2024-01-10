package com.hust.edu.vn.studentmanagement

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.hust.edu.vn.studentmanagement.databinding.FragmentUpdateStudentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class UpdateStudentFragment : Fragment() {
    private lateinit var binding: FragmentUpdateStudentBinding
    private lateinit var studentDao: StudentDao
    private lateinit var student: Student
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateStudentBinding.inflate(inflater, container, false)
        studentDao = StudentDatabase.getInstance(requireContext()).studentDao()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getInt("id")
        if (id != null) {
            lifecycleScope.launch(Dispatchers.IO) {
                student = studentDao.findStudentById(id)
                Log.v("TAG", "ret = $student")
                withContext(Dispatchers.Main) {
                    binding.code.setText(student.code)
                    binding.name.setText(student.name)
                    binding.homeTown.setText(student.homeTown)
                    binding.birthDay.setText(student.birthDay)
                }
            }
        }

        binding.birthDay.setOnClickListener {
            showDatePickerDialog()
        }
        val provinceList = resources.getStringArray(R.array.province)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            provinceList
        )
        binding.homeTown.setAdapter(adapter)
        binding.homeTown.setOnClickListener {
            binding.homeTown.showDropDown()
        }
        binding.delete.setOnClickListener {
            if (id != null) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val id: Int = studentDao.deleteById(id)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            requireContext(),
                            "Xóa sinh viên thành công",
                            Toast.LENGTH_SHORT
                        )
                        findNavController().navigate(R.id.action_updateStudentFragment_to_studentListFragment)
                    }
                    Log.v("TAG", "ret = $id")
                }
            }
        }
        binding.update.setOnClickListener {
            val newCode = binding.code.text.toString()
            val newName = binding.name.text.toString()
            val newBirthDay = binding.birthDay.text.toString()
            val newHomeTown = binding.homeTown.text.toString()
            // Tạo một đối tượng Student mới
            if (id != null) {
                val newStudent = Student(
                    _id = id,  // Giữ nguyên ID của sinh viên cũ
                    code = newCode,
                    name = newName,
                    birthDay = newBirthDay,
                    homeTown = newHomeTown
                )

                // Thực hiện cập nhật trong Database
                lifecycleScope.launch(Dispatchers.IO) {
                    val rowsAffected = studentDao.update(newStudent)
                    withContext(Dispatchers.Main) {
                        if (rowsAffected > 0) {
                            // Nếu cập nhật thành công, hiển thị Toast thông báo
                            Toast.makeText(
                                requireContext(),
                                "Cập nhật thành công",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {
                            // Nếu có lỗi, hiển thị Toast thông báo lỗi
                            Toast.makeText(
                                requireContext(),
                                "Cập nhật thất bại",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                        findNavController().navigate(R.id.action_updateStudentFragment_to_studentListFragment)
                    }

                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePickerDialog() {
        val datePicker = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth)
                binding.birthDay.setText(selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            },
            LocalDate.now().year,
            LocalDate.now().monthValue - 1,
            LocalDate.now().dayOfMonth
        )
        datePicker.show()
    }
}