package edu.vt.cs3714.spring2023.tutorial03

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer

class AllFragment : Fragment() {
    private val model: Model by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_all, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val startButton = view.findViewById<Button>(R.id.start)
        val stopButton = view.findViewById<Button>(R.id.stop)
        val leftText = view.findViewById<TextView>(R.id.left)
        val middleText = view.findViewById<TextView>(R.id.middle)
        val rightText = view.findViewById<TextView>(R.id.right)

        model.values.observe(viewLifecycleOwner, Observer { values ->
            leftText.text = values[0].toString()
            middleText.text = values[1].toString()
            rightText.text = values[2].toString()
        })

        startButton.setOnClickListener {
            model.start()
        }

        stopButton.setOnClickListener {
            model.stop()
        }
    }

    override fun onStop() {
        super.onStop()
        model.stop()
    }
}
