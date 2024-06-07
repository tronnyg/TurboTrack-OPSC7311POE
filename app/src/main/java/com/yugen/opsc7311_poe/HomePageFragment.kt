package com.yugen.opsc7311_poe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.RelativeLayout
import com.yugen.opsc7311_poe.helpers.UserHelper
import android.widget.Toast
import com.google.firebase.firestore.auth.User

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomePageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class HomePageFragment : Fragment() {

    private lateinit var btnUpdateHours: Button
    private lateinit var btnMinHours: EditText
    private lateinit var btnMaxHours: EditText


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Views
        btnUpdateHours = view.findViewById(R.id.btn_update_hours)
        btnMinHours = view.findViewById(R.id.btn_min_hours)
        btnMaxHours = view.findViewById(R.id.btn_max_hours)

        btnMinHours.hint = "Min Hours: " + UserHelper.minHours.toString()
        btnMaxHours.hint = "Max Hours: " + UserHelper.maxHours.toString()

        // Set OnClickListener for the button
        btnUpdateHours.setOnClickListener {
            // Handle button click event here
            val minHours = btnMinHours.text.toString()
            val maxHours = btnMaxHours.text.toString()
            UserHelper.minHours = btnMinHours.text.toString().toInt()
            UserHelper.maxHours = btnMaxHours.text.toString().toInt()

            // Display toast message
            val toastMessage = "Goals have been Updated."
            Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()

            btnMinHours.hint = "Min Hours: " + minHours
            btnMaxHours.hint = "Max Hours: " + maxHours

            btnMinHours.text.clear()
            btnMaxHours.text.clear()
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomePageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomePageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}