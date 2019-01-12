package kz.test.moview.movie_detail

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kz.test.moview.R
import kz.test.moview.main.MainRepository

class MovieDetailActivity : AppCompatActivity() {
    var id:Int?=0
    var actionBar:ActionBar?=null
    lateinit var mainRepository:MainRepository
    @SuppressLint("CheckResult")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        id = intent.getIntExtra("movie_id",-1)
        setSupportActionBar(main_toolbar)
        actionBar = supportActionBar
        actionBar?.apply {
            actionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)
            actionBar?.setDisplayHomeAsUpEnabled(true)
        }
        mainRepository = MainRepository()
        mainRepository.getMovieDetail(id!!)
            .doOnSubscribe {
                progress.visibility = View.VISIBLE
            }
            .doOnComplete {
                progress.visibility = View.GONE
            }
            .subscribe {
                main_collapsing_products.title = it.title
                main_collapsing_products.contentScrim = ContextCompat.getDrawable(this,R.drawable.background_toolbar)
                genre.text = it.genres[0].name
                rate.rating = it.vote_average.toFloat()
                release.text = it.release_date
                Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w300" + it.poster_path)
                    .into(main_backdrop)
                overview.text = it.overview
            }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home->{
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
