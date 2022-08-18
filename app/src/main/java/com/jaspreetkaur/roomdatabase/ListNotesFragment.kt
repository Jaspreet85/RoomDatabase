package com.jaspreetkaur.roomdatabase

import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaspreetkaur.roomdatabase.databinding.FragmentListNotesBinding
import com.jaspreetkaur.roomdatabase.databinding.NewItemAddLayoutBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListNotesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListNotesFragment : Fragment(),RecyclerClickInterface {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentListNotesBinding
    private lateinit var roomDatabase: MainActivity
    private lateinit var adapter : RecyclerClass
    lateinit var linearLayoutManager: LinearLayoutManager
    private var array: ArrayList<Notes> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        roomDatabase = activity as MainActivity

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentListNotesBinding.inflate(layoutInflater)

        adapter = RecyclerClass(array, this)
        linearLayoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.rvRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRecycler.adapter = adapter

        binding.btnfab.setOnClickListener{
            var dialogBinding= NewItemAddLayoutBinding.inflate(layoutInflater)
            var dialog = Dialog(requireContext())
            dialog.setContentView(dialogBinding.root)
            dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)
            dialogBinding.btnOk.setOnClickListener {

                if(dialogBinding.etNewTitle.text.toString().isNullOrEmpty()){
                    dialogBinding.etNewTitle.setError("Enter Title")
                }
                else if(dialogBinding.etNewDescription.text.toString().isNullOrEmpty()){
                    dialogBinding.etNewDescription.setError("Enter Description")
                }
                else{
                    saveNotes(dialogBinding.etNewTitle.text.toString(),dialogBinding.etNewDescription.text.toString(),)
                    dialog.dismiss()
                }
            }
            dialog.show()
        }
        getNotes()
        return binding.root
    }
    private fun saveNotes(title : String,description : String) {
        class saveNote : AsyncTask<Void, Void, Void>(){
            override fun doInBackground(vararg p0: Void?): Void? {
                var notes =  Notes()
                notes.title = title
                notes.description = description
                NotesDatabase.getDatabase(requireContext()).notesDao().InsertNotes(notes)
                return  null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                Toast.makeText(requireContext(), resources.getString(R.string.data_saved),Toast.LENGTH_SHORT).show()

            }
        }
        saveNote().execute()
    }
    private fun getNotes() {
        array.clear()
        class getNote : AsyncTask<Void, Void, Void>(){
            override fun doInBackground(vararg p0: Void?): Void? {
               array.addAll(NotesDatabase.getDatabase(requireContext()).notesDao().getNotes())
                return  null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                adapter.notifyDataSetChanged()
            }
        }
        getNote().execute()
    }

    /* override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         super.onViewCreated(view, savedInstanceState)
     getNotes()
     }
 */
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListNotesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListNotesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun notesClicked(notes: Notes) {
        var dialogBinding= NewItemAddLayoutBinding.inflate(layoutInflater)
        var dialog = Dialog(requireContext())
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)
        dialogBinding.etNewTitle.setText(notes.title)
        dialogBinding.etNewDescription.setText(notes.description)
        dialogBinding.btnDelete.visibility = View.VISIBLE

        dialogBinding.btnDelete.setOnClickListener {
            deleteNotes(notes)
            dialog.dismiss()
        }
        dialogBinding.btnOk.setOnClickListener {

            if(dialogBinding.etNewTitle.text.toString().isNullOrEmpty()){
                dialogBinding.etNewTitle.setError("Enter Title")
            }
            else if(dialogBinding.etNewDescription.text.toString().isNullOrEmpty()){
                dialogBinding.etNewDescription.setError("Enter Description")
            }
            else{
                saveNotes(dialogBinding.etNewTitle.text.toString(),dialogBinding.etNewDescription.text.toString(),)
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun deleteNotes(notes: Notes) {
        class deleteNote : AsyncTask<Void,Void,Void>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                NotesDatabase.getDatabase(requireContext()).notesDao().delete(notes)
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                Toast.makeText(requireContext(), "Notes Delete", Toast.LENGTH_SHORT).show()
                getNotes()
            }
        }
         deleteNote().execute()
}
}