package kz.test.moview

import io.reactivex.Observable
import kz.test.moview.models.MovieResponse
import kz.test.moview.models.Result

interface MainContract{

    interface MainView{
        fun setMovieList(movies:ArrayList<Result>)
        fun addMovieList(movies:ArrayList<Result>)
        fun showProgress()
        fun hideProgress()
        fun setLastPage()
    }
    interface MainPresenter{
        fun attachView(view:MainContract.MainView)
        fun detachView()
        fun getMovies(page:Int)
        fun getMovies(page:Int, query:String)
        fun saveFavList(favList:ArrayList<Int>)
    }

    interface MainRepository{
        fun getMovies(page:Int): Observable<MovieResponse>
        fun getMovies(page:Int,query:String):Observable<MovieResponse>
        fun getFavList():ArrayList<Int>
        fun saveFavList(favList:ArrayList<Int>)
    }

}