package edu.vt.cs3714.spring2023.challenge_05

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import edu.vt.cs3714.spring2023.challenge_05.databinding.FragmentEditBinding
import android.widget.SeekBar
import androidx.navigation.fragment.findNavController

class EditFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getRawFiles(): Array<String> {
        val fields = R.raw::class.java.fields
        return fields.map { it.name }.toTypedArray()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val musicOptions = getRawFiles()

        binding.musicSelectionSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, musicOptions)
        binding.soundEffect1Spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, musicOptions)
        binding.soundEffect2Spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, musicOptions)
        binding.soundEffect3Spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, musicOptions)

        binding.musicSelectionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                sharedViewModel.selectedMusic.value = musicOptions[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.soundEffect1Spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                sharedViewModel.selectedSoundEffect1.value = musicOptions[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.soundEffect2Spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                sharedViewModel.selectedSoundEffect2.value = musicOptions[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.soundEffect3Spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                sharedViewModel.selectedSoundEffect3.value = musicOptions[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.soundEffect1SeekBar.setOnSeekBarChangeListener(object : SimpleSeekBarChangeListener() {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sharedViewModel.delayForSoundEffect1.value = progress
            }
        })

        binding.soundEffect2SeekBar.setOnSeekBarChangeListener(object : SimpleSeekBarChangeListener() {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sharedViewModel.delayForSoundEffect2.value = progress
            }
        })

        binding.soundEffect3SeekBar.setOnSeekBarChangeListener(object : SimpleSeekBarChangeListener() {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sharedViewModel.delayForSoundEffect3.value = progress
            }
        })

        binding.playButton.setOnClickListener {
            sharedViewModel.startPlayback()

            findNavController().navigate(R.id.playFragment)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

abstract class SimpleSeekBarChangeListener : SeekBar.OnSeekBarChangeListener {
    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}
