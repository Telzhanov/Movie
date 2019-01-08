package kz.test.moview

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kz.test.moview.App.Companion.sharedPreference
import kz.test.moview.models.MovieResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.google.gson.reflect.TypeToken
import kz.test.moview.models.FavList


class MainRepository:MainContract.MainRepository{
    var apiService:ApiService
    init {
        val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Log.d("API",message)})
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .addInterceptor(interceptor).build()

        apiService = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(ApiService::class.java)
    }

    override fun getMovies(page:Int):Observable<MovieResponse>{
        return apiService.getMovieList(API_KEY,page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    }

    override fun getMovies(page:Int,query:String):Observable<MovieResponse>{
        return apiService.getSearchMovies(API_KEY,page,query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun saveFavList(favList:ArrayList<Int>){
        var favObject = FavList(favList)
        var json = Gson().toJson(favObject)
        sharedPreference?.edit()?.putString(FAV_MOVIES,json)?.apply()

    }

    override fun getFavList():ArrayList<Int> {
        var json = sharedPreference?.getString(FAV_MOVIES,"No value")
        Log.d("JSON_FAV", json)
        if (json.equals("No value")){
            return ArrayList<Int>()
        }
        else{
            var newFavList = Gson().fromJson(json,FavList::class.java)
            return newFavList.favList
        }
    }
}

const val FAV_MOVIES = "FAV_MOVIES"