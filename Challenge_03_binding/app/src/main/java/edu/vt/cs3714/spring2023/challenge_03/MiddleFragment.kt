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
import edu.vt.cs3714.spring2023.challenge_03.databinding.FragmentLeftBinding
import edu.vt.cs3714.spring2023.challenge_03.databinding.FragmentMiddleBinding


/**
 * A simple [Fragment] subclass.
 * Use the [MiddleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MiddleFragment : Fragment() {

    //Get a handle to the view model we have created.
    private val model: Model by activityViewModels()

    private var _binding: FragmentMiddleBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMiddleBinding.inflate(inflater, container, false)
        val v = binding.root

        //Match the hex signature of the model used, to insure that it is the same instance
        //of the model object. If they don't match, then there are differences between the model
        //instances.
        Log.i("middle_model", model.toString())

        model.getValues().observe(viewLifecycleOwner, Observer<List<Int>> { values ->
            binding.middle.text = values[1].toString()
        })

        return v
    }
}