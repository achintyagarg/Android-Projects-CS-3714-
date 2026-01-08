package edu.vt.cs3714.spring2023.Challenge04

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.*

class MovieDetailFragment : Fragment() {

    private lateinit var movie: MovieItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movie = it.getSerializable("movie") as MovieItem
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = view.findViewById<TextView>(R.id.detail_title)
        val overview = view.findViewById<TextView>(R.id.detail_overview)
        val releaseDate = view.findViewById<TextView>(R.id.detail_release_date)
        val poster = view.findViewById<ImageView>(R.id.detail_poster)

        title.text = movie.title
        overview.text = movie.overview
        releaseDate.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(movie.release_date)
        Glide.with(this)
            .load(getString(R.string.picture_base_url) + movie.poster_path)
            .apply(RequestOptions().override(512, 512))
            .into(poster)
    }

    companion object {
        @JvmStatic
        fun newInstance(movie: MovieItem) = MovieDetailFragment().apply {
            arguments = Bundle().apply {
                putSerializable("movie", movie)
            }
        }
    }
}
