package ro.ebsv.githubapp.main

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import ro.ebsv.githubapp.base.BaseViewModel
import javax.inject.Inject
import javax.inject.Named

class MainViewModel: BaseViewModel() {

    private val TAG = "MainViewModel"

    @Inject
    @field:Named("SharedPreferences")
    lateinit var sharedPreferences: SharedPreferences

    private val compositeDisposable = CompositeDisposable()

    private val clearPrefsLiveData = MutableLiveData<Boolean>()

    fun onClearPrefs(): LiveData<Boolean> = clearPrefsLiveData

    fun clearSharedPreferences() {
        sharedPreferences.edit().clear().apply()
        clearPrefsLiveData.value = true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }

}