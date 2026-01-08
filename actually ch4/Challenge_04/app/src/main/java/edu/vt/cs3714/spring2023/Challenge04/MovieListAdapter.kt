package edu.vt.cs3714.spring2023.Challenge04

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import edu.vt.cs3714.spring2023.Challenge04.HomeFragmentDirections

class MovieListAdapter(private val context: MainActivity, private val fragment: Fragment) :
    RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>() {

    private var movies = emptyList<MovieItem>()
    private var moviesBackup = emptyList<MovieItem>()

    internal fun setMovies(movies: List<MovieItem>) {
        this.moviesBackup = movies
        this.movies = ArrayList(moviesBackup)
        notifyDataSetChanged()
    }

    fun search(query: String?) {
        query?.let {
            movies = moviesBackup.filter { it.title.contains(query, ignoreCase = true) }
            notifyDataSetChanged()
        } ?: run {
            movies = ArrayList(moviesBackup)
            notifyDataSetChanged()
        }
    }

    fun restore() {
        movies = ArrayList(moviesBackup)
        notifyDataSetChanged()
    }

    fun sortByTitle() {
        movies = movies.sortedBy { it.title }
        notifyDataSetChanged()
    }

    fun sortByRating() {
        movies = movies.sortedByDescending { it.vote_average }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view, parent, false)
        return MovieViewHolder(v)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        Glide.with(context)
            .load(context.resources.getString(R.string.picture_base_url) + movies[position].poster_path)
            .apply(RequestOptions().override(128, 128))
            .into(holder.view.findViewById(R.id.poster))

        holder.view.findViewById<TextView>(R.id.title).text = movies[position].title
        holder.view.findViewById<TextView>(R.id.rating).text =
            movies[position].vote_average.toString()

        holder.itemView.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToMovieDetailFragment(movies[position])
            fragment.findNavController().navigate(action)
        }
    }

    inner class MovieViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}
