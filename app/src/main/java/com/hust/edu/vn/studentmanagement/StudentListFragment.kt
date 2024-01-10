package com.hust.edu.vn.studentmanagement

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hust.edu.vn.studentmanagement.databinding.FragmentStudentListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StudentListFragment : Fragment(), MenuProvider {
    private lateinit var binding: FragmentStudentListBinding
    private lateinit var studentDao: StudentDao
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudentListBinding.inflate(inflater, container, false)
        studentDao = StudentDatabase.getInstance(requireContext()).studentDao()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        var students: MutableList<Student> = mutableListOf()
        lifecycleScope.launch(Dispatchers.IO) {
            students = studentDao.getAllStudents()
            Log.v("TAG", "ret = $students")
            withContext(Dispatchers.Main) {
                val recyclerView: RecyclerView = binding.studentList
                val studentAdapter = StudentAdapter(this@StudentListFragment, students)
                recyclerView.adapter = studentAdapter
                recyclerView.layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.action_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.action_add -> {
                findNavController().navigate(R.id.action_studentListFragment_to_addStudentFragment)
            }

            else -> {
                //todo...
            }
        }
        return true
    }
}