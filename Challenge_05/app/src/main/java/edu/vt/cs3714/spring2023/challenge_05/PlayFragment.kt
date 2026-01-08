package edu.vt.cs3714.spring2023.challenge_05

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import edu.vt.cs3714.spring2023.challenge_05.databinding.FragmentPlayBinding


class PlayFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentPlayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.selectedMusic.observe(viewLifecycleOwner, { musicName ->
            updateName(musicName)
        })

        binding.playPauseButton.setOnClickListener {
            sharedViewModel.togglePlayPause()
        }

        binding.restartButton.setOnClickListener {
            sharedViewModel.restartPlayback()
        }
    }

    fun updateName(musicName: String?) {
        binding.songName.text = "You are listening to $musicName"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
