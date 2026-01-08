package edu.vt.cs3714.spring2023.challenge_03

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.lifecycle.Observer

/**
 * Main fragment that shows all three numbers.
 */
class AllFragment : Fragment() {

    private lateinit var left: TextView
    private lateinit var middle: TextView
    private lateinit var right: TextView

    private val model: Model by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_all, container, false)

        // Handles to the left, right and middle textviews
        left = v.findViewById(R.id.left)
        middle = v.findViewById(R.id.middle)
        right = v.findViewById(R.id.right)

        // Set onclick listeners on each of the text views, so that the subfragments are loaded when
        // the views are clicked.
        left.setOnClickListener {
            it.findNavController().navigate(R.id.action_allFragment_to_leftFragment)
        }

        middle.setOnClickListener {
            it.findNavController().navigate(R.id.action_allFragment_to_middleFragment)
        }

        right.setOnClickListener {
            it.findNavController().navigate(R.id.action_allFragment_to_rightFragment)
        }

        //Match the hex signature of the model used, to insure that it is the same instance
        //of the model object. If they don't match, then there are differences between the model
        //instances.
        Log.i("all_model", model.toString())

        //Sets the on click listener for the start button to start the selection
        (v.findViewById(R.id.start) as Button).setOnClickListener {
            model.slotMachineDraw()
        }

        //Sets the on click listener for the stop button
        (v.findViewById(R.id.stop) as Button).setOnClickListener {
            model.stop()
        }


        model.getValues().observe(this, Observer<List<Int>> { list ->

            left.text = list[0].toString()
            middle.text = list[1].toString()
            right.text = list[2].toString()

        })

        return v
    }

}