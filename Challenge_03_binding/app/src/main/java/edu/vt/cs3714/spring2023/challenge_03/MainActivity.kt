package edu.vt.cs3714.spring2023.challenge_03

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.vt.cs3714.spring2023.challenge_03.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}