package kz.test.moview

import android.app.SearchManager
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.text.BoringLayout
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import kz.test.moview.models.Result
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.movie_item.*


class MainActivity : AppCompatActivity(),MainContract.MainView,AddFavListener {
    lateinit var presenter:MainPresenter
    var isLoading = false
    var isLastPage = false
    var searchView:SearchView?=null
    var page = 1
    lateinit var items:ArrayList<Result>
    lateinit var faves:ArrayList<Int>
    lateinit var movieAdapter:MovieListAdapter
    override fun addtoFav(id: Int) {
        faves.add(id)
        presenter.saveFavList(faves)
    }

    override fun deleteFav(id: Int) {
        faves.remove(id)
        presenter.saveFavList(faves)
    }
    override fun setMovieList(movies: ArrayList<Result>) {
        items.clear()
        items.addAll(movies)
        movieAdapter = MovieListAdapter(this,items,faves)
        movieList.adapter = movieAdapter
        movieList.layoutManager = GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false)
    }
    override fun setLastPage() {
        isLastPage = true
    }
    override fun addMovieList(movies: ArrayList<Result>) {
        items.addAll(movies)
        movieAdapter.notifyItemRangeChanged(items.size-1,movies.size)
        isLoading = false
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        var actionBar = supportActionBar
        actionBar?.apply {
            title = "Movies"
        }
        init()

    }
    fun init(){
        faves = ArrayList<Int>()
        items = ArrayList()
        presenter = MainPresenter()
        presenter.attachView(this)
        presenter.getMovies(page)
        movieList.addOnScrollListener(object:RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                var visibleItemCount = movieList.layoutManager?.childCount
                var totalItemCount = movieList?.layoutManager?.itemCount
                var firstVisibleItemPosition = (movieList.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
                if (!isLoading && !isLastPage){
                    if ((visibleItemCount!! + firstVisibleItemPosition) >= totalItemCount!!
                        && firstVisibleItemPosition >= 0){
                        page++
                        isLoading= true
                        if (searchView?.query.isNullOrEmpty()){
                            presenter.getMovies(page)
                        }
                        else{
                            presenter.getMovies(page,searchView?.query.toString())
                        }
                    }
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu?.findItem(R.id.actionSearch)?.actionView as SearchView
        searchView!!.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setOnCloseListener {
                page = 1
                presenter.getMovies(page)
                false
            }
            setOnQueryTextListener(object:SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    page = 1
                    if (!newText?.trim().isNullOrEmpty()) {
                        presenter.getMovies(page,searchView?.query.toString())
                    }else{
                        presenter.getMovies(page)
                    }
                    return true
                }

            })
        }
        return true
    }


}
interface AddFavListener{
    fun addtoFav(id:Int)
    fun deleteFav(id:Int)
}

const val API_KEY = "7880d16bc929086b4585cf3325a27642"
