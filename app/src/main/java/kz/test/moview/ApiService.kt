package kz.test.moview

import io.reactivex.Observable
import kz.test.moview.models.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface ApiService{
    @GET("movie/popular")
    fun getMovieList(@Query("api_key") api:String,
                     @Query("page") page:Int):Observable<MovieResponse>
    @GET("search/movie")
    fun getSearchMovies(@Query("api_key") api:String,
                        @Query("page") page:Int,
                        @Query("query") query: String):Observable<MovieResponse>
}