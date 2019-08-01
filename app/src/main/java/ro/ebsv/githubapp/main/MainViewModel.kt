package ro.ebsv.githubapp.main

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ro.ebsv.githubapp.base.BaseViewModel
import ro.ebsv.githubapp.data.Constants
import ro.ebsv.githubapp.managers.UserManager
import ro.ebsv.githubapp.room.database.GithubDataBase
import ro.ebsv.githubapp.room.entities.UserEntity
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

    val userLiveData = MutableLiveData<UserEntity>()

    private val sendEmailLiveData = MutableLiveData<String>()
    fun getEmailLiveData() = sendEmailLiveData


    private val openReposLiveData = MutableLiveData<Constants.Repository.Filters.Visibility>()
    fun getOpenReposLiveData() = openReposLiveData

    init {
        userLiveData.value = UserManager.user
    }

    fun onReposClicked(visibility: Constants.Repository.Filters.Visibility) {
        openReposLiveData.value = visibility
    }

    fun onSendEmailClicked() {
        sendEmailLiveData.value = userLiveData.value?.email
    }

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