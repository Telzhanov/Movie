package kz.test.moview

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.disposables.Disposables
import kz.test.moview.models.Result

class MainPresenter:MainContract.MainPresenter{
    lateinit var view:MainContract.MainView
    lateinit var repository: MainContract.MainRepository

    override fun attachView(view: MainContract.MainView) {
        this.view= view
        repository = MainRepository()

    }
    override fun detachView() {

    }
    fun setMovieList(page:Int,total_page:Int,movieList:ArrayList<Result>, favs:ArrayList<Int>){
        for (i in 0 until  movieList.size){
            for (fav in favs){
                if (movieList[i].id==fav)
                    movieList[i].fav = true
            }
        }
        if (total_page==page){
            view.setLastPage()
        }
        if (page==1){
            view.setMovieList(movieList)
        }
        else{
            view.addMovieList(movieList)
        }
    }
    override fun saveFavList(favList:ArrayList<Int>){
        repository.saveFavList(favList)
    }
    @SuppressLint("CheckResult")
    override fun getMovies(page:Int){
        repository.getMovies(page)
            .doOnSubscribe { view.showProgress() }
            .doOnComplete { view.hideProgress() }
            .subscribe ({
                setMovieList(page,it.total_pages,it.results as ArrayList<Result>,repository.getFavList())
        },{
            Log.d("ERROR_INTERNET", it.message)
        })
    }
    @SuppressLint("CheckResult")
    override fun getMovies(page:Int, query:String){
        repository.getMovies(page,query)
            .doOnSubscribe { view.showProgress() }
            .doOnComplete { view.hideProgress() }
            .subscribe({
                setMovieList(page,it.total_pages,it.results as ArrayList<Result>,repository.getFavList())
            },{

            })
    }
}