package edu.vt.cs3714.spring2023.Challenge04

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {

    private lateinit var adapter: MovieListAdapter
    private lateinit var model: MovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MovieListAdapter(requireActivity() as MainActivity, this)

        val recyclerView = view.findViewById<RecyclerView>(R.id.movie_list)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        model = ViewModelProvider(requireActivity())[MovieViewModel::class.java]

        model.allMovies.observe(viewLifecycleOwner, Observer<List<MovieItem>> { movies ->
            movies?.let {
                adapter.setMovies(it)
                when (model.sortOrder) {
                    1 -> adapter.sortByTitle()
                    2 -> adapter.sortByRating()
                    else -> adapter.restore()
                }
            }
        })

        view.findViewById<Button>(R.id.refresh_title).setOnClickListener {
            model.sortOrder = 1
            model.startRefreshMovies()
        }

        view.findViewById<Button>(R.id.refresh_rating).setOnClickListener {
            model.sortOrder = 2
            model.startRefreshMovies()
        }

        val searchView = view.findViewById<SearchView>(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.search(newText)
                return true
            }
        })
    }
}
