package edu.vt.cs3714.spring2023.challenge_03

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer


/**
 * A simple [Fragment] subclass.
 * Use the [LeftFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LeftFragment : Fragment() {

    //Get a handle to the view model we have created.
    private val model: Model by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_left, container, false)

        //Match the hex signature of the model used, to insure that it is the same instance
        //of the model object. If they don't match, then there are differences between the model
        //instances.
        Log.i("left_model", model.toString())

        model.getValues().observe(viewLifecycleOwner, Observer<List<Int>> { values ->
            v.findViewById<TextView>(R.id.left).text = values[0].toString()
        })

        return v
    }
}