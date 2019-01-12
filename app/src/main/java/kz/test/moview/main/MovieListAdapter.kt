package kz.test.moview.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.movie_item.view.*
import kz.test.moview.R
import kz.test.moview.models.Result

class MovieListAdapter(var context: Context, var movieList:ArrayList<Result>):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return MovieHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.movie_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            (context as OnMovieClickListener).showMovieDetail(movieList[position].id)
        }
        holder.itemView.idMovie.text = movieList[position].id.toString()
        holder.itemView.nameMovie.text = movieList[position].title
        if (movieList[position].poster_path != "") {
            Glide.with(context)
                .load("https://image.tmdb.org/t/p/w200" + movieList[position].poster_path)
                .into(holder.itemView.poster)
        }
        if (movieList[position].fav == true){
            holder.itemView.favTrue.visibility = View.VISIBLE
        }else{
            holder.itemView.favTrue.visibility = View.GONE
        }
        holder.itemView.favTrue.setOnClickListener {
            (context as AddFavListener).deleteFav(movieList[position].id)
            holder.itemView.favTrue.visibility = View.GONE
            holder.itemView.favFalse.visibility = View.VISIBLE
        }
        holder.itemView.favFalse.setOnClickListener {
            (context as AddFavListener).addtoFav(movieList[position].id)
            holder.itemView.favFalse.visibility = View.GONE
            holder.itemView.favTrue.visibility = View.VISIBLE
        }
    }
    class MovieHolder(v:View):RecyclerView.ViewHolder(v)
}