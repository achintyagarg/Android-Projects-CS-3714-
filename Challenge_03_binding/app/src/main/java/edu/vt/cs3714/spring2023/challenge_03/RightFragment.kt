package edu.vt.cs3714.spring2023.challenge_03

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import edu.vt.cs3714.spring2023.challenge_03.databinding.FragmentMiddleBinding
import edu.vt.cs3714.spring2023.challenge_03.databinding.FragmentRightBinding

/**
 * A simple [Fragment] subclass.
 * Use the [RightFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RightFragment : Fragment() {

    //Get a handle to the view model we have created.
    private val model: Model by activityViewModels()

    private var _binding: FragmentRightBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRightBinding.inflate(inflater, container, false)
        val v = binding.root

        //Match the hex signature of the model used, to insure that it is the same instance
        //of the model object. If they don't match, then there are differences between the model
        //instances.
        Log.i("right_model", model.toString())

        model.getValues().observe(viewLifecycleOwner) { values ->
            binding.right.text = values[2].toString()
        }

        return v
    }
}