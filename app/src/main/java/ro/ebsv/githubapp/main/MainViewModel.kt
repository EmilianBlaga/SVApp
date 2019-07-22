package ro.ebsv.githubapp.main

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ro.ebsv.githubapp.base.BaseViewModel
import ro.ebsv.githubapp.room.database.GithubDataBase
import javax.inject.Inject
import javax.inject.Named

class MainViewModel: BaseViewModel() {

    private val TAG = "MainViewModel"

    @Inject
    @field:Named("SharedPreferences")
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    @field:Named("GithubDataBase")
    lateinit var dataBase: GithubDataBase

    private val compositeDisposable = CompositeDisposable()

    private val clearDataLiveData = MutableLiveData<Boolean>()

    fun onClearData(): LiveData<Boolean> = clearDataLiveData

    fun clearData() {
        sharedPreferences.edit().clear().apply()

        val deleteReposDisp  = dataBase.reposDao().deleteAll().subscribeOn(Schedulers.io()).subscribe {

            val deleteUserDisp = dataBase.userDao().deleteAll().subscribe {

                clearDataLiveData.postValue(true)
            }

            compositeDisposable.add(deleteUserDisp)
        }

        compositeDisposable.add(deleteReposDisp)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }

}